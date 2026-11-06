package com.dchealth.etl.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.elasticsearch.utils.ESQueryUtils;
import com.dchealth.etl.dao.target.TdESDataQueryMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;

import com.dchealth.elasticsearch.service.ElasticsearchTemplate;
import com.dchealth.etl.domain.TdHealthIndex;
import com.dchealth.etl.domain.target.PatientBaseInfo;
import com.dchealth.etl.domain.target.TdDiagnosis;
import com.dchealth.etl.service.target.PatientBaseInfoService;
import com.dchealth.etl.service.target.TdDiagnosisService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author zjl
 * @since 2023/4/18
 */
@Service("SyncRecordDataESServiceImpl")
@Slf4j
public class SyncRecordDataESServiceImpl<T> implements SyncRecordDataESService<T>{

    @Autowired
    private PatientBaseInfoService patientBaseInfoService;
    @Autowired
    private ElasticsearchTemplate<PatientBaseInfo> patientBaseInfoTemplate;
    @Autowired
    private TdDiagnosisService tdDiagnosisService;
    @Autowired
    private ElasticsearchTemplate<TdDiagnosis> tdDiagnosisTemplate;
    @Autowired
    private ElasticsearchTemplate<TdHealthIndex> tdHealthIndexTemplate;
    @Autowired
    ElasticsearchClient elasticsearchClient;
    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    TdESDataQueryMapper tdESDataQueryMapper;

    public static String formatStrShort = "yyyy-MM-dd";
    private static double pageSize = 300.00;
    /**
     * 同步患者信息
     * @param id
     * @return
     */
    @Override
    public int syncPatientBaseInfos(String id) throws IllegalAccessException, NoSuchFieldException, IOException {
        boolean isNext = true;
        try{
            //排序
           /* SortOptions.Builder sb = new SortOptions.Builder();
            sb.field((f) -> {
                        f.order(SortOrder.Desc);
                        f.field("id");
                        return f;
                    }
            );
            SortOptions build = sb.build();
            List<SortOptions> builders = Arrays.asList(build);

            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("index_patientbaseinfo")
                    .from(0)
                    .size(10)
                    .sort(builders)
                    .build();
            //patientBaseInfoTemplate 查询会报类型异常，故查询采用elasticsearchClient查询
            SearchResponse<PatientBaseInfo> response = elasticsearchClient.search(searchRequest,PatientBaseInfo.class);
            List<Hit<PatientBaseInfo>> hits = response.hits().hits();
            log.info("ES查询PatientBaseInfo={}",hits.size());
            //聚合
            long queryId = 0;
            if(hits.size()>0){
                queryId = hits.get(0).source().getId();
            }
            if(StringUtils.isNotBlank(id)){
                queryId = Long.valueOf(id);
            }

            Map params = new HashMap();
            params.put("offset",0);
            params.put("pageSize",300);

            PatientBaseInfo pb = new PatientBaseInfo();
            pb.setParams(params);
            while(isNext){
                log.info("数据库查询PatientBaseInfo，queryId={}",queryId);
                pb.setId(queryId);
                List<PatientBaseInfo> list = patientBaseInfoService.queryByPage(pb);
                log.info("数据库查询PatientBaseInfo size={}",list.size());
                if(list!=null && list.size()>0){
                    patientBaseInfoTemplate.bulkSaveOrUpdate(list);
                    queryId = list.get(list.size()-1).getId();
                    log.info("保存ES数据PatientBaseInfo成功 size={}",list.size());
                }else{
                    isNext = false;
                }
            }*/
        }catch (Exception e){
            log.info("同步TdDiagnosis异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return 0;
    }
    /**
     * 同步诊断记录
     * @param id
     * @return
     */
    @Override
    public int syncTdDiagnosiss(String id) throws IllegalAccessException, NoSuchFieldException, IOException {
        boolean isNext = true;
        try{
           /* //排序
            SortOptions.Builder sb = new SortOptions.Builder();
            sb.field((f) -> {
                        f.order(SortOrder.Desc);
                        f.field("id");
                        return f;
                    }
            );
            SortOptions build = sb.build();
            List<SortOptions> builders = Arrays.asList(build);

            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("index_tddiagnosis")
                    .from(0)
                    .size(3)
                    .sort(builders)
                    .build();
            //tdDiagnosisTemplate 查询会报类型异常，故查询采用elasticsearchClient查询
            SearchResponse<TdDiagnosis> response = elasticsearchClient.search(searchRequest,TdDiagnosis.class);
            List<Hit<TdDiagnosis>> hits = response.hits().hits();
            log.info("ES查询TdDiagnosis={}",hits.size());
            //聚合
            Integer queryId = 0;
            if(hits.size()>0){
                queryId = hits.get(0).source().getId();
            }
            if(StringUtils.isNotBlank(id)){
                queryId = Integer.valueOf(id);
            }

            Map params = new HashMap();
            params.put("offset",0);
            params.put("pageSize",300);
            TdDiagnosis diagnosis = new TdDiagnosis();
            diagnosis.setParams(params);
            while(isNext){
                log.info("数据库查询TdDiagnosis，queryId={}",queryId);
                diagnosis.setId(queryId);
                List<TdDiagnosis> list = tdDiagnosisService.queryByPage(diagnosis);
                log.info("数据库查询TdDiagnosis size={}",list.size());
                if(list!=null && list.size()>0){
                    int num = tdDiagnosisTemplate.bulkSaveOrUpdate(list);
                    log.info("保存ES数据TdDiagnosis成功 num={}",num);
                    queryId = list.get(list.size()-1).getId();
                }else{
                    isNext = false;
                }
            }*/
        }catch (Exception e){
            log.info("同步TdDiagnosis异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
    }
    /**
     * 获取日期偏移后日期字符串
     *
     * @param recordDateStr yyyy-MM-dd
     * @param offset
     * @return
     */
    private String getDateStr(String recordDateStr, int offset) {
        Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
        return DateUtil.format(DateUtil.offsetDay(rDate, offset), formatStrShort);
    }
    public List<TdHealthIndex> getSources(SearchResponse searchResponse) {
        log.info("consume times {} mill second", searchResponse.getTook());
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<TdHealthIndex> ls = new ArrayList<>();
        if (searchHits != null && searchHits.length > 0) {
            // 将hits中的hits中的值放在对象中
            for (SearchHit hit : searchHits) {
                String str = hit.getSourceAsString();
                TdHealthIndex tTdHealthIndex = JSONObject.parseObject(str, TdHealthIndex.class);
                ls.add(tTdHealthIndex);
            }
        }
        return ls;
    }
    /**
     * 同步患者诊断信息，es中字段平铺
     * 诊断记录依次从数据库查询后插入es即可，不存在更新数据的情况，也和其他表不做关联
     * @param id
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @Override
    public int syncTdDiagnosisInfos(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.filter(QueryBuilders.termQuery("esDataType","1"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("diagnosisDate", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getDiagnosisDate(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = tdESDataQueryMapper.queryDiagnosisInfosCount(params);
                if (count == 0) {
                    log.info("syncTdDiagnosisInfos当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = tdESDataQueryMapper.queryDiagnosisInfos(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者诊断信息成功：最后一条数据,i={},TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                log.info("{}当天保存诊断信息数据{}条",startDate,saveNum);
                //循环进行下一天的数据
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncTdDiagnosisInfos异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
    }

    /**
     * 保存数据到es
     * @param ls
     * @return
     */
    private int esBulk(List<TdHealthIndex> ls) throws IOException {
        int rsNum = 0;
        if(ls!=null && ls.size()>0){
            BulkRequest bulkRequest = new BulkRequest();
            for(TdHealthIndex hl : ls){
                if(StringUtils.isNotBlank(hl.getPatientId())){
                    String hlStr = JSON.toJSONString(hl);
                    bulkRequest.add(new IndexRequest("td_health_index").source(hlStr, XContentType.JSON));
                }
            }
            rsNum = restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT).getItems().length;
        }
        return rsNum;
    }

    @Override
    public int syncTdGeneralInspectionInfos(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.termQuery("esDataType","2"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("generalInspectionDate", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getGeneralInspectionDate(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = tdESDataQueryMapper.queryGeneralInspectionsCount(params);
                if (count == 0) {
                    log.info("syncTdGeneralInspectionInfos当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = tdESDataQueryMapper.queryGeneralInspectionInfos(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者一般检查成功：当前分页pageNo={},最后一条数据TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                log.info("{}当天保存一般检查数据{}条",startDate,saveNum);
                //循环进行下一天的数据
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncTdGeneralInspectionInfos异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return 0;
    }

    @Override
    public int syncTdExamMaster(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.termQuery("esDataType","3"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("examResultTime", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getExamResultTime(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = tdESDataQueryMapper.queryExamInfoCount(params);
                if (count == 0) {
                    log.info("syncTdExamMaster当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = tdESDataQueryMapper.queryExamInfoInfos(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者辅助检查信息成功：当前分页pageNo={},最后一条数据TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                //循环进行下一天的数据
                log.info("{}当天保存辅助检查数据{}条",startDate,saveNum);
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncTdExamMaster异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
    }

    @Override
    public int syncTdTestMaster(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.termQuery("esDataType","4"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("testResultTime", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getTestResultTime(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = tdESDataQueryMapper.queryTestInfoCount(params);
                if (count == 0) {
                    log.info("syncTdTestMaster当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = tdESDataQueryMapper.queryTestInfoInfos(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者检验信息成功：当前分页pageNo={},最后一条数据TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                //循环进行下一天的数据
                log.info("{}当天保存检验信息数据{}条",startDate,saveNum);
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncTdTestMaster异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
    }

    /**
     * 同步患者处方信息
     * @param id
     * @return
     */
    @Override
    public int syncDrugPresc(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.termQuery("esDataType","6"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("id", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getPrescDate(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = tdESDataQueryMapper.queryDrugPrescInfoCount(params);
                if (count == 0) {
                    log.info("syncDrugPresc当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = tdESDataQueryMapper.queryDrugPrescInfos(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者药品处方信息成功：当前分页pageNo={},最后一条数据TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                log.info("{}当天保存药品处方信息数据{}条",startDate,saveNum);
                //循环进行下一天的数据
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncDrugPresc异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
     }
    /**
     * 同步患者医嘱信息
     * @param id
     * @return
     */
    @Override
    public int syncTdOrders(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.must(QueryBuilders.termQuery("esDataType","5"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("enterDateTime", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getEnterDateTime(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = tdESDataQueryMapper.queryOrdersInfoCount(params);
                if (count == 0) {
                    log.info("syncTdOrders当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = tdESDataQueryMapper.queryOrdersInfos(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者医嘱信息成功：当前分页pageNo={},最后一条数据TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                log.info("{}当天保存医嘱信息数据{}条",startDate,saveNum);
                //循环进行下一天的数据
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncTdOrders异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
    }
}
