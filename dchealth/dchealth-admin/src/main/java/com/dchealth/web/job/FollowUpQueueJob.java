package com.dchealth.web.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.common.constant.FollowTipStatusConstants;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SplitListUtils;
import com.dchealth.es.service.SyncRecordDataESService;
import com.dchealth.medical.domain.*;
import com.dchealth.medical.mapper.*;
import com.dchealth.medical.service.IPatientEnrollmentConditionService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;


/**
 * 随访队列患者检索导入
 * @author caihe
 * @since 2023/5/10
 */
@Component
@Slf4j
public class FollowUpQueueJob {


    @Autowired
    private PatientEnrollmentConditionMapper patientEnrollmentConditionMapper;
    @Resource
    private PatientInfoMapper patientInfoMapper;
    @Resource
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Resource
    private PatientRecordMapper patientRecordMapper;
    @Resource
    private ResearchPatientMapper researchPatientMapper;
    @Autowired
    private IPatientEnrollmentConditionService patientEnrollmentConditionService;
    @Autowired
    private SyncRecordDataESService syncRecordDataESService;

    public static String formatStrShort = "yyyy-MM-dd";
    /**
     * 随访队列患者检索导入 es-->队列待入组
     */
    @XxlJob("followUpPatientImportHandler")
    @Deprecated
    public void followUpPatientImportHandler() throws Exception {
        //查询队列检索条件(预纳排入组条件)
        List<PatientEnrollmentCondition> conditionList = patientEnrollmentConditionMapper.queryPatientEnrollmentConditionList("1");
        if (conditionList == null || conditionList.isEmpty()){
            XxlJobHelper.log("随访队列患者检索导入，没有查询到队列检索条件！");
            log.info("followUpPatientImportHandler 随访队列患者检索导入，没有查询到队列检索条件！");
        }
        log.info("followUpPatientImportHandler 查询队列检索条件 conditionList.size={}",conditionList.size());

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 30, 4, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(20), new ThreadPoolExecutor.CallerRunsPolicy());
        //10个一批次
        List<List<PatientEnrollmentCondition>> splitList = SplitListUtils.splitList(conditionList, 10);
        //记录单个任务的执行次数
        CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
        log.info("followUpPatientImportHandler 开始循环splitList，splitList.size={}",splitList.size());
        for (List<PatientEnrollmentCondition> splist : splitList) {
            //一个线程执行10个队列, 10个任务以内只有一个线程处理
            threadPoolExecutor.execute(() -> {
                for(int i=0;i<splist.size();i++) {
                    try {
                        log.info("followUpPatientImportHandler 开始执行线程任务，splist，splist.size={},condition={}",splist.size(), JSON.toJSONString(splist.get(i)));
                        this.importPatient(splist.get(i));
                    } catch (Exception e) {
                        e.printStackTrace();
                        XxlJobHelper.log("随访队列患者检索导入，导入患者线程内异常！");
                        log.error("followUpPatientImportHandler 导入患者线程内异常，PatientEnrollmentCondition={}",JSON.toJSONString(splist.get(i)));
                        log.error("followUpPatientImportHandler 导入患者线程内异常，Exception={}",JSON.toJSONString(e));
                    }
                }
            });
            // 任务个数 - 1, 直至为0时唤醒await()
            countDownLatch.countDown();
            log.info("********countDownLatch 任务个数 -1 ******************");
        }
        try {
            // 让当前线程处于阻塞状态，直到锁存器计数为零
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new Exception(e);
        }finally {
            threadPoolExecutor.shutdown(); //线程池使用完毕后需要关毕
        }

        //当线程池完成的线程数等于线程池中的总线程数,线程任务全部执行完毕
        boolean allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
        log.info("followUpPatientImportHandler allThreadsIsDone={}",allThreadsIsDone);

    }

    /**
     * 定时导入 db-->es, 入参是时间端, 若无则默认导入昨天的数据
     */
    @XxlJob("followUpPersonImportHandler")
    public void followUpPersonImportHandler() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();
        if(StringUtils.isBlank(jobParam) || jobParam.split(",").length!=2){
            XxlJobHelper.log("followUpPersonImportHandler 定时导入es，jobParam为空或没有开始结束日期, 默认导入昨天的数据, 若填写请严格按照日期格式填写,数据示例:2020-01-01,2023-11-11");
            String startDate = DateUtil.format(DateUtil.yesterday(), formatStrShort);
            String endDate = DateUtil.today();
            syncRecordDataESService.syncMpDiagnosisInfos("", startDate, endDate);
        }else {
            //jobParam示例：2021-05-10,2021-05-11
            String[] jobParamArr = jobParam.split(",");
            String startDate = jobParamArr[0];
            String endDate = jobParamArr[1];
            syncRecordDataESService.syncMpDiagnosisInfos("", startDate, endDate);
        }
    }

    /**
     * 定时导入 db-->es, 指定运行天, 0代表今天, 默认-1, 0代表今天, -1代表昨天, -2代表前天
     */
    @XxlJob("followUpPersonImportHandlerAppointDay")
    public void followUpPersonImportHandlerAppointDay() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();
        if(StringUtils.isBlank(jobParam)){
            XxlJobHelper.log("失败, 参数错误");
        }else{
            XxlJobHelper.log("followUpPersonImportHandlerAppointDay 定时导入es，jobParam为-1或者0,-1,-2等, 表示处理昨天的诊断数据或今天,昨天,前天...");

            //jobParam示例：2021-05-10,2021-05-11
            String[] jobParamArr = jobParam.split(",");
            for (int i = 0; i < jobParamArr.length; i++) {
                String dayOffset = jobParamArr[i];
                DateTime date = DateUtil.date();
                String startDate = DateUtil.format(DateUtil.offsetDay(date, Integer.parseInt(dayOffset)), formatStrShort);
                String endDate = DateUtil.format(DateUtil.offsetDay(date, Integer.parseInt(dayOffset)+1), formatStrShort);
                XxlJobHelper.log("开始日期:{}", startDate);
                XxlJobHelper.log("结束日期:{}", endDate);
                syncRecordDataESService.syncMpDiagnosisInfos("", startDate, endDate);
            }
        }
    }

    /**
     * 清理es索引数据
     */
    @XxlJob("clearEsIndexData")
    public void clearEsIndexData() throws Exception {
        String jobParam = XxlJobHelper.getJobParam();
        String index = "td_health_index";
        if(patientEnrollmentConditionService.clearIndexData(index)){
            XxlJobHelper.log("followUpPersonImportHandler 清理es索引数据成功！");
        }else{
            XxlJobHelper.log("followUpPersonImportHandler 清理es索引数据失败!!！");
//            XxlJobHelper.handleResult(500,"followUpPersonImportHandler 清理es索引数据，清理es索引数据失败！");
        }

    }

    /**
     * 导入检索患者
     * @param condition
     * @return
     */
    @Deprecated
    private String importPatient(PatientEnrollmentCondition condition) throws Exception {
        long logNum = 0;
        if(condition!=null && StringUtils.isNotBlank(condition.getConditions()) && condition.getResearchId()!=null){
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
            List<String> baseIds = patientInfoMapper.queryPatientInfoBaseIds(pi);

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
                Map rtMap = patientEnrollmentConditionService.conditionSearchByES(condition,false);
                totalSize = (long)rtMap.getOrDefault("totalSize",0L);
                List<Map> list = (List)rtMap.getOrDefault("list",new ArrayList());
                log.info("当前es查询totalSize={},list.size={}",totalSize,list.size());
                //要插入的患者信息
                List<PatientInfo> insertPatientInfos = new ArrayList<>();
                //要插入的基线信息
                List<PatientRecord> insertPatientRecords = new ArrayList<>();
                List<ResearchPatient> insertResearchPatients = new ArrayList<>();

                //增量写入patientInfo表, 已经纳排过的数据过滤掉
                for(Map map : list){
                    if(!baseIds.contains(map.get("patientId"))){
                        Map infoMap = this.getPatientInfo(condition,map,patientRecordTemplates);
                        insertPatientInfos.add((PatientInfo)infoMap.get("patientInfo"));
                        logNum++;
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
    @Deprecated
    private Map getPatientInfo(PatientEnrollmentCondition condition,Map esMap,List<PatientRecordTemplate> patientRecordTemplates){
        Map rtMap =new HashMap();
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPersonId(String.valueOf(esMap.get("personId")));
        patientInfo.setAccountId(Long.parseLong(String.valueOf(esMap.getOrDefault("accountId",0))));
        patientInfo.setResearchId(condition.getResearchId());
        patientInfo.setPlanResearchId(condition.getResearchId());
        patientInfo.setPatientName(String.valueOf(esMap.get("name")));
        patientInfo.setDeptId(StringUtils.isBlank(condition.getDeptIdArr())?0:Long.valueOf(condition.getDeptIdArr()));
        patientInfo.setPatientMobile(String.valueOf(esMap.get("mobile")));
        patientInfo.setPatientStatus("0");
        patientInfo.setCreateTime(DateUtils.getNowDate());
//        patientInfo.setEnrollmentTime(DateUtils.getNowDate());
        //入组状态0待分配1已入组2已分配3已完成4失访
        patientInfo.setEnrollmentStatus("0");
        patientInfo.setPersonId(String.valueOf(esMap.get("patientId")));
        //渠道来源1后台2扫码
        patientInfo.setChannelType("1");
        patientInfo.setAdjustType("1");
        patientInfo.setCreateBy("adminJob");
        //入组结果0待分配1已入组2已分配3已完成4失访
        patientInfo.setEnrollmentResult("0");
        //入组处理状态0待处理1已处理2已取消3已完成4失访
        patientInfo.setEnrollmentHandlerStatus("1");
        //分配处理状态0待处理1已处理2已取消3已完成4失访
        patientInfo.setAllocatedHandlerStatus("0");
        patientInfo.setEnrollmentResultContent("确认入组");
        patientInfo.setAllocatedStatus("0");
        patientInfo.setLossCause("0");
        patientInfo.setIsEndCreateRecord("0");
        rtMap.put("patientInfo",patientInfo);
        return rtMap;
    }

}
