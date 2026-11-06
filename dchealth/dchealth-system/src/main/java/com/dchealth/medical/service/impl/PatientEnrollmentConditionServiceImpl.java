package com.dchealth.medical.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.common.constant.FollowTipStatusConstants;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.es.domain.form.TdHealthIndex;
import com.dchealth.medical.contants.SearchTypeConstants;
import com.dchealth.medical.domain.PatientEnrollmentCondition;
import com.dchealth.medical.domain.PatientEnrollmentConditionTree;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.domain.ResearchPatient;
import com.dchealth.medical.domain.vo.PatientEnrollmentSearchVo;
import com.dchealth.medical.mapper.PatientEnrollmentConditionMapper;
import com.dchealth.medical.mapper.PatientEnrollmentConditionTreeMapper;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.mapper.ResearchPatientMapper;
import com.dchealth.medical.service.IPatientEnrollmentConditionService;
import com.dchealth.system.domain.PatientCardInfo;
import com.dchealth.system.mapper.PatientCardInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;

import javax.annotation.Resource;

@Service
@Slf4j
public class PatientEnrollmentConditionServiceImpl implements IPatientEnrollmentConditionService {
    private static final List<String> ENROLLMENT_TIME_TYPE = Arrays.asList("门诊时间", "入院时间", "出院时间");
    private static final List<String> DEPT_TYPE = Arrays.asList("包含", "不包含");
    private static final List<String> SEX = Arrays.asList("男", "女", "不限");
    private static final List<String> AGE_TYPE = Arrays.asList("区间", "等于", "大于", "小于", "大于等于", "小于等于");
    @Autowired
    private PatientEnrollmentConditionMapper patientEnrollmentConditionMapper;
    @Autowired
    private PatientEnrollmentConditionTreeMapper patientEnrollmentConditionTreeMapper;
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Resource
    private PatientInfoMapper patientInfoMapper;
    @Resource
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Resource
    private PatientRecordMapper patientRecordMapper;
    @Resource
    private ResearchPatientMapper researchPatientMapper;

    @Autowired
    private PatientCardInfoMapper patientCardInfoMapper;
    /**
     * 保存纳排条件
     * @param patientEnrollmentCondition
     * @return
     * @throws ServiceException
     */
    @Override
    public int insertPatientEnrollmentCondition(PatientEnrollmentCondition patientEnrollmentCondition) throws ServiceException {
        if (Objects.isNull(patientEnrollmentCondition.getResearchId())) {
            throw new ServiceException("队列id不能为空");
        }
        List<Map<String,String>> paramList = patientEnrollmentCondition.getParamList();
        if(paramList!=null && paramList.size()>0){
            patientEnrollmentCondition.setConditions(JSONObject.toJSONString(paramList));
        }

        PatientEnrollmentCondition patientEnrollmentCondition1 = patientEnrollmentConditionMapper.selectPatientEnrollmentConditionByResearchId(patientEnrollmentCondition.getResearchId());
        if (Objects.nonNull(patientEnrollmentCondition1)) {
            /*A.更新纳排条件*/
            if (StringUtils.isNotEmpty(patientEnrollmentCondition.getImportTimes())) {
                patientEnrollmentCondition.setImportTime(DateUtils.getTimeFromDate(patientEnrollmentCondition.getImportTimes()));
            }
            patientEnrollmentCondition1.setUpdateTime(DateUtils.getNowDate());
            try {
                importPatient(patientEnrollmentCondition);
            } catch (Exception e) {
                log.error("导入数据失败", e);
                throw new ServiceException(e.getMessage());
            }
            return patientEnrollmentConditionMapper.updatePatientEnrollmentConditionByResearchId(patientEnrollmentCondition);
        }else {
            /*B.插入纳排条件*/
            if (StringUtils.isNotEmpty(patientEnrollmentCondition.getImportTimes())) {
                patientEnrollmentCondition.setImportTime(DateUtils.getTimeFromDate(patientEnrollmentCondition.getImportTimes()));
            }
            patientEnrollmentCondition.setUpdateTime(DateUtils.getNowDate());
            patientEnrollmentCondition.setCreateTime(DateUtils.getNowDate());

            try {
                importPatient(patientEnrollmentCondition);
            } catch (Exception e) {
                log.error("导入数据失败", e);
                throw new ServiceException(e.getMessage());
            }
            return patientEnrollmentConditionMapper.insertPatientEnrollmentCondition(patientEnrollmentCondition);
        }
    }

    private String importPatient(PatientEnrollmentCondition condition) throws Exception {
        long logNum = 0;
        if(condition!=null && org.apache.commons.lang3.StringUtils.isNotBlank(condition.getConditions()) && condition.getResearchId()!=null){
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(condition.getResearchId());

            //将json条件串转换
            JSONArray jsonArray = JSONArray.parse(condition.getConditions());
            List<Map<String,String>> paramList = new ArrayList<>();
            jsonArray.forEach(json->{
                paramList.add(JSONObject.parseObject(json.toString(),Map.class));
            });
            condition.setParamList(paramList);

            //查询出已经入组的患者id
            PatientInfo pi = new PatientInfo();
            pi.setResearchId(condition.getResearchId());
            List<String> personIds = patientInfoMapper.queryPatientInfoBaseIds(pi);

            boolean isNext = true;
            //记录查询出条数
            long count = 0;
            long totalSize = 0;
            int pageNum = 1;
            int pageSize = 300;
            condition.setPageSize(pageSize);
            log.info("=================开始循环处理队列{}==================",condition.getResearchId());
            //每300条循环查询处理，知道查询结果为空
            while (isNext){
                log.info("当前es查询pageNum={}",pageNum);
                condition.setPageNum(pageNum);
                Map rtMap = this.conditionSearchByES(condition,false);
                totalSize = (long)rtMap.getOrDefault("totalSize",0L);
                List<Map> list = (List)rtMap.getOrDefault("list",new ArrayList());
                log.info("当前es查询totalSize={},list.size={}",totalSize,list.size());
                //要插入的患者信息
                List<PatientInfo> insertPatientInfos = new ArrayList<>();
                //要插入的基线信息
                List<PatientRecord> insertPatientRecords = new ArrayList<>();
                List<ResearchPatient> insertResearchPatients = new ArrayList<>();

                Set<String> one = new HashSet<>();
                //增量写入patientInfo表, 已经纳排过的数据过滤掉
                for(Map map : list){
                    String patientId = (String)map.get("patientId"); // es库中
                    // 切换主号
                    PatientCardInfo patientCardInfo = new PatientCardInfo();
                    patientCardInfo.setPersonId(patientId);
                    List<PatientCardInfo> patientCardInfos = patientCardInfoMapper.selectPatientCardInfoList(patientCardInfo);
                    if (!CollectionUtils.isEmpty(patientCardInfos)) {
                        String idCard = patientCardInfos.get(0).getIdCard();
                        if (StringUtils.isNotEmpty(idCard)) {
                            PatientCardInfo patientCardInfoQuery = new PatientCardInfo();
                            patientCardInfoQuery.setIdCard(idCard);
                            patientCardInfoQuery.setPatientMasterNo(1);
                            List<PatientCardInfo> patientCardInfosMaster = patientCardInfoMapper.selectPatientCardInfoList(patientCardInfoQuery);
                            if (!CollectionUtils.isEmpty(patientCardInfosMaster)) {
                                // 主号
                                String personId = patientCardInfosMaster.get(0).getPersonId();
                                patientId = personId;
                                map.put("patientId", personId);
                            }
                        }
                    }
                    if (!one.contains(patientId)) {
                        one.add(patientId);
                        // personIds 数据库中personId, 这个优先更新
                        if(!personIds.contains(patientId)){
                            Map infoMap = this.getPatientInfo(condition,map,patientRecordTemplates);
                            insertPatientInfos.add((PatientInfo)infoMap.get("patientInfo"));
                            logNum++;
                        }
                    }else{
                        //跳过
                    }
                }

                //保存数据
                if(insertPatientInfos.size()>0){
                    patientInfoMapper.saveBatachPatientInfo(insertPatientInfos);

                    for(PatientInfo p : insertPatientInfos){
                        //生成research_patient表数据
                        ResearchPatient researchPatient = new ResearchPatient();
                        researchPatient.setResearchId(p.getResearchId());
                        researchPatient.setPersonId(p.getPersonId());
                        researchPatient.setPatientId(p.getPatientId());
                        researchPatient.setCreateTime(DateUtils.getNowDate());
                        insertResearchPatients.add(researchPatient);
                        //生成基线
                        Long followId = null;
                        PatientRecord patientRecord = new PatientRecord();
                        if (patientRecordTemplates.size() > 0) {
                            followId = patientRecordTemplates.get(0).getFollowId();
                            patientRecord.setLockVisitTime(p.getCreateTime());
                        }
                        patientRecord.setFollowId(followId);
                        patientRecord.setName("基线");
                        patientRecord.setCreateTime(DateUtils.getNowDate());
                        patientRecord.setResearchId(condition.getResearchId());
                        patientRecord.setPatientId(p.getPatientId());
                        patientRecord.setVisitTime(DateUtils.getNowDate());
                        patientRecord.setFollowTipStatus(FollowTipStatusConstants.BASELINE_REMINDER);
                        patientRecord.setUpdateBy("adminJob");
                        patientRecord.setFollowStatus("0");
                        patientRecord.setStageNum(1);
                        patientRecord.setStageAddNum(1);
                        insertPatientRecords.add(patientRecord);
                    }

                }
                if(insertPatientRecords.size()>0){
                    patientRecordMapper.batchInsertPatientRecord(insertPatientRecords);
                }
                if (insertResearchPatients.size()>0){
                    researchPatientMapper.batchInsert(insertResearchPatients);
                }

                count += pageSize;
                if(count>= totalSize){
                    isNext = false;
                }

                pageNum++;
            }
            log.info("-----------------结束循环处理队列{}---------------",condition.getResearchId());

        }else {
            log.error("导入失败，请填写纳排条件");
            throw new ServiceException("请填写纳排条件");
        }
        log.info("#########队列:{}导入成功，共导入患者:{}人#######",condition.getResearchId(),logNum);
        return "导入成功";
    }

    /**
     * 组装PatientInfo
     * @param condition
     * @param esMap
     * @return
     */
    private Map getPatientInfo(PatientEnrollmentCondition condition,Map esMap,List<PatientRecordTemplate> patientRecordTemplates){
        Map rtMap =new HashMap();
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPersonId(String.valueOf(esMap.get("personId")));
        patientInfo.setAccountId(Long.parseLong(String.valueOf(esMap.getOrDefault("accountId",0))));
        patientInfo.setResearchId(condition.getResearchId());
        patientInfo.setPlanResearchId(condition.getResearchId());
        patientInfo.setPatientName(String.valueOf(esMap.get("name")));
        patientInfo.setDeptId(org.apache.commons.lang3.StringUtils.isBlank(condition.getDeptIdArr())?0:Long.valueOf(condition.getDeptIdArr()));
        patientInfo.setPatientMobile(String.valueOf(esMap.get("mobile")));
        patientInfo.setPatientStatus("0");
        patientInfo.setCreateTime(DateUtils.getNowDate());
        patientInfo.setEnrollmentTime(DateUtils.getNowDate());
        //入组状态0待分配1已入组2已分配3已完成4失访
        patientInfo.setEnrollmentStatus("1");
        patientInfo.setPersonId(String.valueOf(esMap.get("patientId")));
        //渠道来源1后台2扫码
        patientInfo.setChannelType("1");
        patientInfo.setAdjustType("1");
        patientInfo.setCreateBy("adminJob");
        //入组结果0待分配1已入组2已分配3已完成4失访
        patientInfo.setEnrollmentResult("3");
        //入组处理状态0待处理1已处理2已取消3已完成4失访
        patientInfo.setEnrollmentHandlerStatus("3");
        //分配处理状态0待处理1已处理2已取消3已完成4失访
        patientInfo.setAllocatedHandlerStatus("3");
        patientInfo.setEnrollmentResultContent("确认入组");
        patientInfo.setAllocatedStatus("0");
        patientInfo.setLossCause("0");
        patientInfo.setIsEndCreateRecord("0");
        rtMap.put("patientInfo",patientInfo);
        return rtMap;
    }

    /**
     * 获取es结果转换
     * @param obj
     * @param searchResponse
     * @return
     */
    private List getSearchResponse(Object obj, SearchResponse searchResponse){
        List list = new ArrayList<>();
        if(searchResponse!=null ) {
            SearchHits hits = searchResponse.getHits();
            hits.forEach(p -> {
                String str = p.getSourceAsString();
                if(obj.getClass().isInstance(new HashMap())){
                    Map map = JSONObject.parseObject(str, HashMap.class);
                    map.put("highlightFields",p.getHighlightFields());
                    list.add(map);
                }else{
                    list.add(JSONObject.parseObject(str, obj.getClass()));
                }
            });
        }
        return list;
    }

    private void getResultMap(Map rtMap,SearchResponse searchResponse,List<Map<String,String>> paramList,Object obj,boolean searchTextFlag){
//        List<TdHealthIndex> list = getSearchResponse(obj,searchResponse);
        List<Map> list = getSearchResponse(new HashMap(),searchResponse);
        Aggregations aggregations = searchResponse.getAggregations();
        ParsedCardinality cardinality = aggregations.get("totalSize");
//        ParsedStringTerms patientGroup = aggregations.get("patientGroup");
//        List<? extends Terms.Bucket> buckets = patientGroup.getBuckets();

        List<Map> rtList = new ArrayList<>();

        if(searchTextFlag){
            for(Map thi : list){
                //组装检索内容
                Map map = new HashMap();
//            for(Terms.Bucket bucket : buckets){
//                if(String.valueOf(thi.get("patientId")).equalsIgnoreCase(bucket.getKeyAsString())){
//                    map.put("docCount",bucket.getDocCount());
//                    break;
//                }
//            }
                map.put("baseId",thi.get("baseId"));
                map.put("patientId",thi.get("patientId"));
                map.put("name",thi.get("name"));
                map.put("birthday",thi.get("birthday"));
                map.put("age",thi.get("age"));
                map.put("sex",thi.get("sex"));
                map.put("personId",thi.get("personId"));
                map.put("mobile",thi.get("mobile"));

                //获取高亮信息
                Map highlightFields = (Map) thi.get("highlightFields");
                if(highlightFields!=null){
                    highlightFields.forEach((key,value)->{
                        HighlightField hf = (HighlightField)value;
                        thi.put(key,hf.getFragments()[0]);
                    });
                }
                //检索内容
                StringBuffer sb = new StringBuffer("");
                for(Map<String,String> param : paramList){
                    sb.append("(")
                            //.append(param.get("tableName")).append("-")
                            .append(param.get("fieldName")).append(":")
                            .append(thi.get(param.get("field")))
                            .append(")")
                            .append("|");
                }

                if(sb.length()>0){
                    map.put("searchText",sb.toString().substring(0,sb.length()-1));
                }else{
                    map.put("searchText","");
                }
                rtList.add(map);
            }
        }

        rtMap.put("totalSize",Long.valueOf(cardinality.getValue()));
        rtMap.put("list",list);
        rtMap.put("rtList",rtList);
    }

    /**
     * 通过查询条件查询es数据
     * 队列设置-导入随访患者es检索
     * @param pec
     * @param searchTextFlag 是否包含检索内容
     * @return
     * @throws IOException
     */
    @Override
    public Map conditionSearchByES(PatientEnrollmentCondition pec,boolean searchTextFlag) throws IOException {
        int pageNo = 0;
        int pageSize =10;
        Map rtMap = new HashMap();
        if(pec.getPageNum()!=null){
            pageNo = pec.getPageNum()-1;
        }
        if(pec.getPageSize()!=null){
            pageSize = pec.getPageSize();
        }
        // 创建查询请求对象，将查询对象配置到其中
        SearchRequest searchRequest = new SearchRequest("td_health_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

//        time=12.2 and ((name 包括 '张三' and name 包括 '李四' ) or name 包括 王武 or name 包括 赵六)

        //基础条件-健康档案信息
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        boolQueryBuilder.filter(QueryBuilders.existsQuery("patientId"));
        boolQueryBuilder.must(QueryBuilders.existsQuery("diagnosisDesc"));
        //纳排时间
        if(pec.getEnrollmentTimeStart()==null || pec.getEnrollmentTimeEnd()==null){
            rtMap.put("errorMsg","纳排时间参数不正确");
            log.info("conditionSearchByES查询参数错误，纳排时间参数不正确，pec={}",JSONObject.toJSONString(pec));
            return rtMap;
        }
        //处理纳排时间
        String enrollmentTimeStart = DateUtil.format(pec.getEnrollmentTimeStart(),"yyyy-MM-dd");
        String enrollmentTimeEnd = DateUtil.format(pec.getEnrollmentTimeEnd(),"yyyy-MM-dd");
        String enrollmentTimeField = "diagnosisDate";
//        if(SearchTypeConstants.admissionHospital.equals(pec.getEnrollmentTimeType())){
//            enrollmentTimeField = "visitStartDate";
//        }else if(SearchTypeConstants.discharge.equals(pec.getEnrollmentTimeType())){
//            enrollmentTimeField = "visitEndDate";
//        }
        BoolQueryBuilder timeBoolQueryBuilder = QueryBuilders.boolQuery();
        timeBoolQueryBuilder.should(QueryBuilders.rangeQuery(enrollmentTimeField).gte(enrollmentTimeStart).lte(enrollmentTimeEnd));
        boolQueryBuilder.must().add(timeBoolQueryBuilder);

        //设置高亮
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.preTags("<font color='red'>");
//        highlightBuilder.postTags("</font>");

        BoolQueryBuilder boolQueryBuilder1 = QueryBuilders.boolQuery();
        BoolQueryBuilder diagnosisDsecAndBoolQueryBuilder = QueryBuilders.boolQuery();
        //循环检索条件
        if(pec.getParamList()!=null){
            pec.getParamList().stream().filter(y-> StringUtils.isNotBlank(y.get("value"))).forEach(map -> {
                if(StringUtils.isBlank(map.get("relation"))){
                    map.put("relation","AND");
                }
                if(SearchTypeConstants.contain.equalsIgnoreCase(map.get("condition"))
                || SearchTypeConstants.notContain.equalsIgnoreCase(map.get("condition"))){
                    //包含
//                    MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery(map.get("field"),map.get("value"));
                    WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery(map.get("field"), "*"+map.get("value")+"*");

                    if("AND".equalsIgnoreCase(map.get("relation"))){

                        if(SearchTypeConstants.notContain.equalsIgnoreCase(map.get("condition"))){
                            diagnosisDsecAndBoolQueryBuilder.mustNot().add(wildcardQueryBuilder);
                            boolQueryBuilder1.mustNot(wildcardQueryBuilder);
                            boolQueryBuilder.mustNot(boolQueryBuilder1);
                        }else{
                            diagnosisDsecAndBoolQueryBuilder.must(wildcardQueryBuilder);
                        }
                    }else{
                        if(SearchTypeConstants.notContain.equalsIgnoreCase(map.get("condition"))){
                            boolQueryBuilder1.mustNot(wildcardQueryBuilder);
                            boolQueryBuilder.mustNot(boolQueryBuilder1);
                        }else{
                            boolQueryBuilder1.should().add(wildcardQueryBuilder);
//                            boolQueryBuilder.must().add(boolQueryBuilder1);
                        }
                    }
                    //设置高亮字段
//                    highlightBuilder.field(map.get("field"),1000,1,500);
                }else if(SearchTypeConstants.equal.equalsIgnoreCase(map.get("condition")) ||
                        SearchTypeConstants.notEqual.equalsIgnoreCase(map.get("condition"))){
                        TermQueryBuilder queryBuilder = QueryBuilders.termQuery(map.get("field"),map.get("value"));
                    if("AND".equalsIgnoreCase(map.get("relation"))){
                        //等于
                        if(SearchTypeConstants.equal.equalsIgnoreCase(map.get("condition"))){
                            boolQueryBuilder.must(queryBuilder);
                        }else{
                            boolQueryBuilder.mustNot(queryBuilder);
                        }
                    }else{
                        boolQueryBuilder.should(queryBuilder);
                    }
                }else if(SearchTypeConstants.buliderRangeType.contains(map.get("condition"))){
                    //区间、大于、大于等于、区间外
                    RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery(map.get("field"));
                    //此处，如果是日期型数据需传yyyy-MM-dd字符串
                    if(SearchTypeConstants.range.equalsIgnoreCase(map.get("condition"))){
                        //区间
                        queryBuilder.gte(map.get("value")).lte(map.get("endValue"));
                    }else if(SearchTypeConstants.gte.equalsIgnoreCase(map.get("condition"))){
                        queryBuilder.gte(map.get("value"));
                    }else if(SearchTypeConstants.gt.equalsIgnoreCase(map.get("condition"))){
                        queryBuilder.gt(map.get("value"));
                    }else if(SearchTypeConstants.lte.equalsIgnoreCase(map.get("condition"))){
                        queryBuilder.lte(map.get("value"));
                    }else if(SearchTypeConstants.lt.equalsIgnoreCase(map.get("condition"))){
                        queryBuilder.lt(map.get("value"));
                    }else if(SearchTypeConstants.rangeOut.equalsIgnoreCase(map.get("condition"))){
                        queryBuilder.lt(map.get("value")).gt(map.get("endValue"));
                    }
                    if("AND".equalsIgnoreCase(map.get("relation"))){
                        boolQueryBuilder.must(queryBuilder);
                    }else{
                        boolQueryBuilder.should(queryBuilder);
                    }
                }
            });
        }
        //多字段时，需要设置为false
//        highlightBuilder.requireFieldMatch(false);
//        searchSourceBuilder.highlighter(highlightBuilder);
        boolQueryBuilder1.should().add(diagnosisDsecAndBoolQueryBuilder);
        boolQueryBuilder.must(boolQueryBuilder1);
        searchSourceBuilder.query(boolQueryBuilder);
        // 将重复数据折叠
        CollapseBuilder collapseBuilder = new CollapseBuilder("patientId");
        AggregationBuilder aggregation = AggregationBuilders
                .cardinality("totalSize").field("patientId")
                .precisionThreshold(40000);

        searchSourceBuilder.collapse(collapseBuilder);
        searchSourceBuilder.aggregation(aggregation);

//        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("patientGroup").field("patientId");
//        searchSourceBuilder.aggregation(termsAggregationBuilder);

        searchSourceBuilder.sort("diagnosisDate", SortOrder.DESC);
        searchSourceBuilder.from(pageSize * pageNo);
        searchSourceBuilder.size(pageSize);
        searchRequest.source(searchSourceBuilder);
        // 执行查询，然后处理响应结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //处理病例数量等信息
        getResultMap(rtMap,searchResponse,pec.getParamList(),new TdHealthIndex(),searchTextFlag);

        return rtMap;
    }



    @Override
    public AjaxResult conditionSearch(PatientEnrollmentCondition patientEnrollmentCondition) throws IOException {
        Map rtMap = conditionSearchByES(patientEnrollmentCondition,true);
        Map<String, Object> res = new HashMap<>();
        res.put("total", rtMap.get("totalSize"));
        res.put("data", rtMap.get("rtList"));
//        res.put("total", rtMap.get("totalSize"));
//        res.put("data", rtMap.get("rtList"));
        /*List<PatientEnrollmentSearchVo> diagnosiss = patientEnrollmentConditionMapper.selectDiagnosisDesc(getDiagnosisParam(patientEnrollmentCondition));
        List<PatientEnrollmentSearchVo> patientBase = patientEnrollmentConditionMapper.selectPatientBaseInfo(getPatientParam(patientEnrollmentCondition));
        List<PatientEnrollmentSearchVo> patientDiagnosis = patientBase.stream().filter(vo ->
                npDateFilter(patientEnrollmentCondition, vo) && ageFilter(patientEnrollmentCondition, vo) && clinicFilter(patientEnrollmentCondition, vo)
                        && v2Merge(vo, diagnosiss)).collect(Collectors.toList());


        if (Objects.isNull(patientEnrollmentCondition.getPageNum()) || Objects.isNull(patientEnrollmentCondition.getPageSize()) || patientEnrollmentCondition.getPageNum()<=0 || patientEnrollmentCondition.getPageSize()<=0) {
            patientEnrollmentCondition.setPageSize(10);
            patientEnrollmentCondition.setPageNum(1);
        }
        List<PatientEnrollmentSearchVo> collect = patientDiagnosis.stream().skip((patientEnrollmentCondition.getPageNum() - 1) * patientEnrollmentCondition.getPageSize()).limit(patientEnrollmentCondition.getPageSize()).collect(Collectors.toList());
        */
        return AjaxResult.success(res);
    }

    @Override
    public PatientEnrollmentCondition conditionDetail(Long researchId) {
        PatientEnrollmentCondition patientEnrollmentCondition = patientEnrollmentConditionMapper.selectPatientEnrollmentConditionByResearchId(researchId);
        if (Objects.isNull(patientEnrollmentCondition)) {
            return new PatientEnrollmentCondition();
        }
        if (Objects.nonNull(patientEnrollmentCondition) && Objects.nonNull(patientEnrollmentCondition.getImportTime())) {
            patientEnrollmentCondition.setImportTimes(patientEnrollmentCondition.getImportTime().toString());
        }
        if(StringUtils.isNotBlank(patientEnrollmentCondition.getConditions())){
            JSONArray jsonArray = JSONArray.parse(patientEnrollmentCondition.getConditions());
            List<Map<String,String>> paramList = new ArrayList<>();
            jsonArray.forEach(json->{
                paramList.add(JSONObject.parseObject(json.toString(),Map.class));
            });
            patientEnrollmentCondition.setParamList(paramList);
        }
        return patientEnrollmentCondition;
    }

    private boolean clinicFilter(PatientEnrollmentCondition condition, PatientEnrollmentSearchVo vo) {
        if (StringUtils.isEmpty(condition.getClinicDateType())) {
            return true;
        }
        if (Objects.isNull(vo.getPatientClinicTime())) {
            return false;
        }
        if (StringUtils.equals("区间", condition.getClinicDateType()) && Objects.nonNull(condition.getClinicDateStart()) && Objects.nonNull(condition.getClinicDateEnd())) {
            return DateUtils.isTimeBetweenRange(vo.getPatientClinicTime(), condition.getClinicDateStart(), condition.getClinicDateEnd());
        } else if (StringUtils.equals("等于", condition.getClinicDateType()) && Objects.nonNull(condition.getClinicDateStart())) {
            return DateUtils.isSameDay(vo.getPatientClinicTime(), condition.getClinicDateStart());
        } else if (StringUtils.equals("大于等于", condition.getClinicDateType()) && Objects.nonNull(condition.getClinicDateStart())) {
            return DateUtils.isSameDay(vo.getPatientClinicTime(), condition.getClinicDateStart()) || vo.getPatientClinicTime().after(condition.getClinicDateStart());
        } else if (StringUtils.equals("小于等于", condition.getClinicDateType()) && Objects.nonNull(condition.getClinicDateStart())) {
            return DateUtils.isSameDay(vo.getPatientClinicTime(), condition.getClinicDateStart()) || vo.getPatientClinicTime().before(condition.getClinicDateStart());
        } else if (StringUtils.equals("大于", condition.getClinicDateType()) && Objects.nonNull(condition.getClinicDateStart())) {
            return vo.getPatientClinicTime().after(condition.getClinicDateStart());
        } else if (StringUtils.equals("小于", condition.getClinicDateType()) && Objects.nonNull(condition.getClinicDateStart())) {
            return vo.getPatientClinicTime().before(condition.getClinicDateStart());
        }
        return false;
    }

    private boolean ageFilter(PatientEnrollmentCondition condition, PatientEnrollmentSearchVo vo) {
        if (StringUtils.isEmpty(condition.getAgeType())) {
            return true;
        }
        if (StringUtils.isEmpty(vo.getBirthday())) {
            return false;
        }
        if (StringUtils.equals("区间", condition.getAgeType()) && Objects.nonNull(condition.getAgeStart()) && Objects.nonNull(condition.getAgeEnd())) {
            return DateUtil.ageOfNow(vo.getBirthday()) >= condition.getAgeStart() && DateUtil.ageOfNow(vo.getBirthday()) <= condition.getAgeEnd();
        } else if (StringUtils.equals("等于", condition.getAgeType()) && Objects.nonNull(condition.getAgeStart())) {
            return DateUtil.ageOfNow(vo.getBirthday()) == condition.getAgeStart();
        } else if (StringUtils.equals("大于等于", condition.getAgeType()) && Objects.nonNull(condition.getAgeStart())) {
            return DateUtil.ageOfNow(vo.getBirthday()) >= condition.getAgeStart();
        } else if (StringUtils.equals("小于等于", condition.getAgeType()) && Objects.nonNull(condition.getAgeStart())) {
            return DateUtil.ageOfNow(vo.getBirthday()) <= condition.getAgeStart();
        } else if (StringUtils.equals("大于", condition.getAgeType()) && Objects.nonNull(condition.getAgeStart())) {
            return DateUtil.ageOfNow(vo.getBirthday()) > condition.getAgeStart();
        } else if (StringUtils.equals("小于", condition.getAgeType()) && Objects.nonNull(condition.getAgeStart())) {
            return DateUtil.ageOfNow(vo.getBirthday()) < condition.getAgeStart();
        }
        return false;
    }

    private boolean npDateFilter(PatientEnrollmentCondition condition, PatientEnrollmentSearchVo vo) {
        if (StringUtils.equals(condition.getEnrollmentTimeType(), "出院时间") && Objects.nonNull(condition.getEnrollmentTimeStart()) && Objects.nonNull(condition.getEnrollmentTimeEnd())) {
            return DateUtils.isTimeBetweenRange(vo.getDischargeHospitalDate(), condition.getEnrollmentTimeStart(), condition.getEnrollmentTimeEnd());
        } else if (StringUtils.equals(condition.getEnrollmentTimeType(), "门诊时间") && Objects.nonNull(condition.getEnrollmentTimeStart()) && Objects.nonNull(condition.getEnrollmentTimeEnd())) {
            return DateUtils.isTimeBetweenRange(vo.getPatientClinicTime(), condition.getEnrollmentTimeStart(), condition.getEnrollmentTimeEnd());
        } else if (StringUtils.equals(condition.getEnrollmentTimeType(), "入院时间") && Objects.nonNull(condition.getEnrollmentTimeStart()) && Objects.nonNull(condition.getEnrollmentTimeEnd())) {
            return DateUtils.isTimeBetweenRange(vo.getAdmissionHospitalDate(), condition.getEnrollmentTimeStart(), condition.getEnrollmentTimeEnd());
        } else {
            return DateUtils.isTimeBetweenRange(vo.getAdmissionHospitalDate(), DateUtils.getPreDate(1095), DateUtils.getNowDate());
        }
    }

    private boolean v2Merge(PatientEnrollmentSearchVo vo, List<PatientEnrollmentSearchVo> patientBase) {
        List<PatientEnrollmentSearchVo> diagnosisList = patientBase.stream().filter(v2 -> Objects.nonNull(v2.getPatientId()) && v2.getPatientId().longValue() == vo.getPatientId().longValue()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(diagnosisList)) {
            vo.setDiagnosisDesc(diagnosisList.get(0).getDiagnosisDesc());
            vo.setMedicalHistoryNum(CollectionUtils.size(diagnosisList));
        }
        return CollectionUtils.isNotEmpty(diagnosisList);
    }

    private String getPatientParam(PatientEnrollmentCondition patientEnrollmentCondition) {
        StringBuilder param = new StringBuilder();
        param.append(" where patient_id is not null ");
        if (StringUtils.equals(patientEnrollmentCondition.getDeptType(), "包含")) {
            param.append(" and admission_hospital_department in (").append(patientEnrollmentCondition.getDeptIdArr()).append(")");
        }else if (StringUtils.equals(patientEnrollmentCondition.getDeptType(), "不包含")){
            param.append(" and not admission_hospital_department in (").append(patientEnrollmentCondition.getDeptIdArr()).append(")");
        }
        if (StringUtils.equals(patientEnrollmentCondition.getSexType(), "包含") && StringUtils.equalsAny(patientEnrollmentCondition.getSex(), "男", "女")) {
            param.append(" and sex =").append(StringUtils.equals(patientEnrollmentCondition.getSex(), "男") ? "1" : "2");
        }
        return param.toString();
    }

    private String getDiagnosisParam(PatientEnrollmentCondition patientEnrollmentCondition) {
        StringBuilder param = new StringBuilder();
        if (StringUtils.isNotEmpty(patientEnrollmentCondition.getDiagnosisType())) {
            param.append(" where ");
            if (StringUtils.equals(patientEnrollmentCondition.getDiagnosisType(), "包含")) {
                param.append(" diagnosis_desc like '%").append(patientEnrollmentCondition.getDiagnosis()).append("%'");
            } else {
                param.append(" diagnosis_desc not like '%").append(patientEnrollmentCondition.getDiagnosis()).append("%'");
            }
        }
        return param.toString();
    }

    @Override
    public List<PatientEnrollmentConditionTree> searchTree() {
        PatientEnrollmentConditionTree patientEnrollmentConditionTree = new PatientEnrollmentConditionTree();
        patientEnrollmentConditionTree.setState(1);
        List<PatientEnrollmentConditionTree> ls = patientEnrollmentConditionTreeMapper.queryAll(patientEnrollmentConditionTree);
        return new PatientEnrollmentConditionTree().getTree(ls);
    }

    //清理索引的数据
    @Override
    public Boolean clearIndexData(String indexName) {

        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(indexName)
                .setQuery(queryBuilder);

        BulkByScrollResponse bulkByScrollResponse = null;
        try {
            bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
            log.info("清空索引{}数据成功,总条数:{}", indexName, bulkByScrollResponse.getTotal());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
