package com.dchealth.web.job;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SplitListUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.ResearchUserRole;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.research.mapper.ResearchUserRoleMapper;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.domain.FollowProperty;
import com.dchealth.template.domain.TextData;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import com.dchealth.template.mapper.FollowPropertyMapper;
import com.dchealth.web.job.form.FollowUpPlanJobHandlerForm;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 随访定时任务
 */
@Component
public class MedicalJob {

    private static Logger logger = LoggerFactory.getLogger(MedicalJob.class);

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private ResearchUserRoleMapper researchUserRoleMapper;
    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private FMedicalPropertyMapper medicalPropertyMapper;

    @Autowired
    private FollowPropertyMapper followPropertyMapper;
    /**
     * 随访任务手动生成
     */
    public AjaxResult followUpTasksAreGeneratedManually(Long id,Long patientId){
        //进行中的队列
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(id);
        if (researchInfo == null){
            return AjaxResult.error("没有此队列");
        }
        if (researchInfo != null && "0".equals(researchInfo.getResearchStatus())){
            return AjaxResult.error("当前队列不是进行中");
        }
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientId);
        if (patientInfo == null){
            return AjaxResult.error("没有此患者");
        }
        //所有进行中队列的规则
        String resultStr = "";
        List<PatientRecord> insertRecord = new ArrayList<>();
        followUpTasksAreGeneratedManually(researchInfo.getId(),patientId, insertRecord);
        resultStr = "操作成功,共插入"+insertRecord.size()+"条";
        return AjaxResult.success(resultStr);
    }


    /**
     * 随访计划
     */
    @XxlJob("followUpPlanJobHandler")
    public void followUpPlanJobHandler() throws Exception {
        XxlJobHelper.log("随访计划定时任务日期{}开始",DateUtils.getTime());
        //进行中的队列
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByStatus("1");
        if (researchInfos == null || researchInfos.isEmpty()){
            XxlJobHelper.log("生成随访计划,没有查询到进行中的队列!");
        }
        //所有进行中队列的规则
        String resultStr = "";
        List<PatientRecord> insertRecord = new ArrayList<>();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 50, 4, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        ResearchInfo ri = new ResearchInfo();
        ri.setResearchStatus("1");
        List<ResearchInfo> researchInfoList = researchInfoMapper.selectResearchInfoList(ri);
        XxlJobHelper.log("随访计划定时任务可执行队列数量:{}",researchInfoList.size());
        List<List<ResearchInfo>> splitList = SplitListUtils.splitList(researchInfoList, 10);
        //记录单个任务的执行次数
        CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
        for (List<ResearchInfo> subList : splitList) {
            threadPoolExecutor.execute(() -> {
                try {
                    for (ResearchInfo r : subList) {
                        insertRecordJob(r.getId(), insertRecord);
                    }
                } finally {
                    countDownLatch.countDown(); // 确保无论如何都会递减计数器
                }
            });
        }
        try {
            // 让当前线程处于阻塞状态，直到锁存器计数为零
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new NullPointerException(e.getMessage());
        }finally {
            threadPoolExecutor.shutdown(); //线程使用完毕后需要关团
        }

        //当线程池完成的线程数等于线程池中的总线程数
//        boolean allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
//        int u=0;
//        if (allThreadsIsDone) {
//            if (!insertRecord.isEmpty() && u==0){
//                patientRecordMapper.batchInsertPatientRecord(insertRecord);
//                resultStr = "操作成功,共插入"+insertRecord.size()+"条";
//                u++;
//            }
//        }
//        while (!allThreadsIsDone) {
//            allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
//            if (allThreadsIsDone) {
//                if (!insertRecord.isEmpty() && u==0){
//                    patientRecordMapper.batchInsertPatientRecord(insertRecord);
//                    resultStr = "操作成功,共插入"+insertRecord.size()+"条";
//                    u++;
//                }
//            }
//        }
        resultStr = "操作成功,共插入"+insertRecord.size()+"条";
        XxlJobHelper.handleSuccess(resultStr);
    }
    //    public String followPlanJobHandler() throws Exception {
//
//        XxlJobHelper.log("随访计划定时任务日期{}开始",DateUtils.getTime());
//        //进行中的队列
//        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByStatus("1");
//        if (researchInfos == null || researchInfos.isEmpty()){
//            XxlJobHelper.log("生成随访计划,没有查询到进行中的队列!");
//        }
//        //所有进行中队列的规则
//        String resultStr = "";
//        List<PatientRecord> insertRecord = new ArrayList<>();
//
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 50, 4, TimeUnit.SECONDS,
//                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
//        ResearchInfo ri = new ResearchInfo();
//        ri.setResearchStatus("1");
//        List<ResearchInfo> researchInfoList = researchInfoMapper.selectResearchInfoList(ri);
//        List<List<ResearchInfo>> splitList = SplitListUtils.splitList(researchInfoList, 10);
//        //记录单个任务的执行次数
//        CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
//        for (List<ResearchInfo> splist : splitList) {
//            threadPoolExecutor.execute(new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    for(int i=0;i<splist.size();i++) {
//                        ResearchInfo r = splist.get(i);
//                        insertRecordJob(r.getId(),insertRecord);
//                    }
//                }
//            }));
//            // 任务个数 - 1, 直至为0时唤醒await()
//            countDownLatch.countDown();
//        }
//        try {
//            // 让当前线程处于阻塞状态，直到锁存器计数为零
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            throw new NullPointerException(e.getMessage());
//        }finally {
//            threadPoolExecutor.shutdown(); //线程使用完毕后需要关团
//        }
//
//        //当线程池完成的线程数等于线程池中的总线程数
//        boolean allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
//        int u=0;
//        if (allThreadsIsDone) {
//            if (!insertRecord.isEmpty() && u==0){
//                patientRecordMapper.batchInsertPatientRecord(insertRecord);
//                resultStr = "操作成功,共插入"+insertRecord.size()+"条";
//                u++;
//            }
//        }
//        while (!allThreadsIsDone) {
//            allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
//            if (allThreadsIsDone) {
//                if (!insertRecord.isEmpty() && u==0){
//                    patientRecordMapper.batchInsertPatientRecord(insertRecord);
//                    resultStr = "操作成功,共插入"+insertRecord.size()+"条";
//                    u++;
//                }
//            }
//        }
//        XxlJobHelper.handleSuccess(resultStr);
//        return "success";
//    }
    public void followUpTasksAreGeneratedManually(Long researchId,Long patientId,List<PatientRecord> listTotal){
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        if(researchInfo!=null && "1".equals(researchInfo.getResearchStatus())){
            PatientRecordTemplate pt = new PatientRecordTemplate();
            pt.setResearchId(researchId);
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.followUpTasksAreGeneratedManually(pt);
            if(patientRecordTemplates.size()>0){
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
                List<Long> followIds = patientRecordTemplates.stream().map(PatientRecordTemplate::getFollowId).collect(Collectors.toList());
                List<PatientRecord> insertList = new ArrayList<>();

                //查询的是同一个队列中同一个登录账号下[由于A->B->A可能 有多个patientId]的最大的随访记录，不是所有的，这也是为啥多次插入而不重复插入数据的根因
                PatientRecord patientRecord = patientRecordMapper.followUpTasksAreGeneratedManually(researchInfo.getId(),patientId,followIds);
                logger.info("随访计划定时任务执行队列id:{},手动入组的患者id:{}",researchInfo.getId(),patientRecord.getPatientId());
                if (!maps.containsKey(patientRecord.getFollowId())){
                    return;
                }
                PatientRecordTemplate recordTemplate = maps.get(patientRecord.getFollowId());
                logger.info("---------------------------recordId:{}",patientRecord.getRecordId());
                PatientInfo patientInfo = getPatientInfo(patientRecord);
                //创建随访1的PatientRecord记录
                followUpTasksAreGeneratedManually(listTotal,insertList, researchInfo, patientRecord, recordTemplate,patientInfo);
                if(insertList.size()>0){
                    //肥胖综合征
                    List<Long> list=followPropertyMapper.getDict("research");
                    if (list.contains(researchId)){
                        for (PatientRecord record : insertList) {
                            patientRecordMapper.insertPatientRecord(record);
                            logger.info("表单id：{}，患者id：{}，队列id：{}，recordId:{}",record.getFollowId(),record.getPersonId(),researchId,record.getRecordId());
                            getFollowDate(record.getFollowId(),record.getPersonId(),researchId,record.getRecordId());
                        }
                    }else{
                        patientRecordMapper.batchInsertPatientRecord(insertList);
                    }
                    patientInfo.setGeneratedManually(patientInfo.getGeneratedManually() == null? BigDecimal.ONE.longValue():patientInfo.getGeneratedManually()+BigDecimal.ONE.longValue());
                    patientInfo.setIsEndCreateRecord("0");
                    patientInfoMapper.updatePatientInfo(patientInfo);
                    logger.info("----------------------------------------队列id:{}插入周期数量:{}-----------------",researchId,insertList.size());
                }
            }
        }
    }
    public void insertRecordJob(Long researchId,List<PatientRecord> listTotal){
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        if(researchInfo!=null && "1".equals(researchInfo.getResearchStatus())){
            long TimeNow = System.currentTimeMillis();
            Date now = new Date(TimeNow);
            PatientRecordTemplate pt = new PatientRecordTemplate();
            pt.setResearchId(researchId);
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(pt);
            if(patientRecordTemplates.size()>0){
                List<PatientRecord> insertList = new ArrayList<>();
                patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
                //查询的是同一个队列中同一个登录账号下[由于A->B->A可能 有多个patientId]的最大的随访记录，不是所有的，这也是为啥多次插入而不重复插入数据的根因
                List<PatientRecord> patientRecords = patientRecordMapper.selectOnePatientMaxRecordByResearchId(researchInfo.getId());
                XxlJobHelper.log("随访计划定时任务执行队列id:{},查询可执行周期数:{}",researchInfo.getId(),patientRecords.size());
                patientRecords.forEach(patientRecord->{
                    if(patientRecord.getAccountId()==0){
                        return;
                    }
                    if (!maps.containsKey(patientRecord.getFollowId())){
                        return;
                    }
                    if(patientRecord.getLockVisitTime()==null){
                        return;
                    }
                    PatientRecordTemplate recordTemplate = maps.get(patientRecord.getFollowId());
                    XxlJobHelper.log("---------------------------recordId:{}",patientRecord.getRecordId());
                    PatientInfo patientInfo = getPatientInfo(patientRecord);
                    //说明是基线,会创建随访1的PatientRecord记录
                    if(recordTemplate.getFollowNum()==0){
                        handleBaseLine(listTotal,insertList, researchInfo, now, patientRecordTemplates, patientRecord,
                                recordTemplate,patientInfo);
                    }else{
                        if (!"4".equals(patientInfo.getPatientStatus())) {
                            handleFollowUp(listTotal,insertList,researchInfo, now, patientRecordTemplates, patientRecord,
                                    recordTemplate,patientInfo);
                        }
                    }
                });
                if(insertList.size()>0){
                    //肥胖综合征
                    List<Long> list=followPropertyMapper.getDict("research");
                    if (list.contains(researchId)){
                        for (PatientRecord record : insertList) {
                            patientRecordMapper.insertPatientRecord(record);
                            logger.info("表单id：{}，患者id：{}，队列id：{}，recordId:{}",record.getFollowId(),record.getPersonId(),researchId,record.getRecordId());
                            getFollowDate(record.getFollowId(),record.getPersonId(),researchId,record.getRecordId());
                        }
                    }else{
                        patientRecordMapper.batchInsertPatientRecord(insertList);
                    }
                    XxlJobHelper.log("----------------------------------------队列id:{}插入周期数量:{}-----------------",researchId,insertList.size());
                }
            }
        }
    }

    private PatientInfo getPatientInfo(PatientRecord patientRecord) {
        PatientInfo patientInfo = null;
        if (Objects.isNull(patientRecord.getPatientId())) {
            logger.error("随访记录了recordId 缺失患者信息:{} patientId:{}", patientRecord.getRecordId(),
                    patientRecord.getPatientId());
        } else {
            patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
            if (Objects.isNull(patientInfo)) {
                logger.error("通过患者ID查询患者信息结果为空  patientId:{}", patientRecord.getPatientId());
            }
        }
        return patientInfo;
    }

    private void handleFollowUp(List<PatientRecord> listTotal,List<PatientRecord> insertList,ResearchInfo researchInfo, Date now,
                                List<PatientRecordTemplate> patientRecordTemplates, PatientRecord x,
                                PatientRecordTemplate recordTemplate,PatientInfo patientInfo) {
        if ("1".equals(recordTemplate.getFollowFrequency())){//限制频次
            handleLimitFrequency(listTotal,insertList, researchInfo, now, patientRecordTemplates, x, recordTemplate,patientInfo);
        }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
            handleNoLimitFrequency(listTotal,insertList, now, patientRecordTemplates, x, recordTemplate,patientInfo);
        }
    }

    private void handleNoLimitFrequency(List<PatientRecord> listTotal,List<PatientRecord> insertList, Date now,
                                        List<PatientRecordTemplate> patientRecordTemplates, PatientRecord patientRecord,
                                        PatientRecordTemplate recordTemplate,PatientInfo patient) {
        patientRecord.setStageAddNum(patientRecord.getStageAddNum()+1);
        patientRecord.setPersonId(patient.getPersonId());
        // 没设置次数且间隔为0天
        if(recordTemplate.getFollowTotal()==null && recordTemplate.getFollowInterval()==0){
            int index = patientRecordTemplates.indexOf(recordTemplate);
            if (index+1 < patientRecordTemplates.size()){
                if(isCreateRecord(now, patientRecord, patientRecordTemplates.get(index + 1))){
                    if(Objects.isNull(patient)){
                        logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                        return;
                    }
                    if (!Objects.isNull(patient.getPlanResearchId()) && !Objects.equals(patient.getPlanResearchId(),
                            patient.getResearchId())) {
                        logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}", patient.getPatientId());
                        return;
                    }
                    listTotal.add(new PatientRecord(patientRecord, patientRecordTemplates.get(index + 1)));
                    insertList.add(new PatientRecord(patientRecord, patientRecordTemplates.get(index + 1)));
                }
            }
        }else{
            if(isCreateRecord(now, patientRecord, recordTemplate)){
                if(Objects.isNull(patient)){
                    logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                    return;
                }
                if (!Objects.isNull(patient.getPlanResearchId()) && !Objects.equals(patient.getPlanResearchId(),
                        patient.getResearchId())) {
                    logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}", patient.getPatientId());
                    return;
                }
                listTotal.add(new PatientRecord(patientRecord, recordTemplate));
                insertList.add(new PatientRecord(patientRecord, recordTemplate));
            }
        }
    }

    private void handleLimitFrequency(List<PatientRecord> listTotal,List<PatientRecord> insertList,ResearchInfo researchInfo, Date now,
                                      List<PatientRecordTemplate> patientRecordTemplates, PatientRecord patientRecord,
                                      PatientRecordTemplate recordTemplate,PatientInfo patient) {
        if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
            logger.info("生成随访计划,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
            return;
        }
        patientRecord.setPersonId(patient.getPersonId());
        patientRecord.setStageAddNum(patientRecord.getStageAddNum()+1);
        if (patientRecord.getStageNum().intValue() == recordTemplate.getFollowTotal().intValue()){//完成当前周期的随访节点进入下一个随访节点
            int index = patientRecordTemplates.indexOf(recordTemplate);
            if (index+1 < patientRecordTemplates.size()){
                if(isCreateRecord(now, patientRecord, patientRecordTemplates.get(index + 1))){
                    if(Objects.isNull(patient)){
                        logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                        return;
                    }
                    if (!Objects.isNull(patient.getPlanResearchId()) && !Objects.equals(patient.getPlanResearchId(),
                            patient.getResearchId())) {
                        logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}", patient.getPatientId());
                        return;
                    }
                    listTotal.add(new PatientRecord(patientRecord, patientRecordTemplates.get(index + 1)));
                    insertList.add(new PatientRecord(patientRecord, patientRecordTemplates.get(index + 1)));
                }
            }else{
                //后续没有配置的随访计划就先更新当前队列不可再生成对方节点
                PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
                patientInfo.setIsEndCreateRecord("1");
                patientInfoMapper.updatePatientInfo(patientInfo);
            }
        }else if (patientRecord.getStageNum().intValue() < recordTemplate.getFollowTotal().intValue()){//未完成当前随访节点继续生成当前随访节点计划
            if(isCreateRecord(now, patientRecord, recordTemplate)){
                if(Objects.isNull(patient)){
                    logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                    return;
                }
                if (!Objects.isNull(patient.getPlanResearchId()) && !Objects.equals(patient.getPlanResearchId(),
                        patient.getResearchId())) {
                    logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}", patient.getPatientId());
                    return;
                }
                listTotal.add(new PatientRecord(patientRecord, recordTemplate));
                insertList.add(new PatientRecord(patientRecord, recordTemplate));
            }
        }
    }
    private void followUpTasksAreGeneratedManually(List<PatientRecord> listTotal, List<PatientRecord> insertList,ResearchInfo researchInfo,
                               PatientRecord patientRecord,PatientRecordTemplate recordTemplate,PatientInfo patientInfo) {
        patientRecord.setStageAddNum(1);

        patientRecord.setPersonId(patientInfo.getPersonId());
        if ("1".equals(recordTemplate.getFollowFrequency())){
            //限制频次
            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
                logger.info("生成随访计划,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                return ;
            }
            if(Objects.isNull(patientInfo)){
                logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                return ;
            }
            if(!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),patientInfo.getResearchId())){
                logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}",patientInfo.getPatientId());
                return ;
            }
            PatientRecord newPatientRecord = new PatientRecord(patientRecord, recordTemplate);
            newPatientRecord.setStageNum(1);
            listTotal.add(newPatientRecord);
            newPatientRecord =  new PatientRecord(patientRecord, recordTemplate);
            newPatientRecord.setStageNum(1);
            insertList.add(newPatientRecord);
        }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
            if(Objects.isNull(patientInfo)){
                logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                return;
            }
            if(!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),patientInfo.getResearchId())){
                logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}",patientInfo.getPatientId());
                return;
            }
            PatientRecord newPatientRecord = new PatientRecord(patientRecord, recordTemplate);
            newPatientRecord.setStageNum(1);
            listTotal.add(newPatientRecord);
            newPatientRecord =  new PatientRecord(patientRecord, recordTemplate);
            newPatientRecord.setStageNum(1);
            insertList.add(newPatientRecord);
        }
    }
    private void handleBaseLine(List<PatientRecord> listTotal, List<PatientRecord> insertList,ResearchInfo researchInfo, Date now,
                                List<PatientRecordTemplate> patientRecordTemplates, PatientRecord patientRecord,
                                PatientRecordTemplate recordTemplate,PatientInfo patientInfo) {

        if(patientRecordTemplates.size() >1){
            //随访1节点模板
            recordTemplate = patientRecordTemplates.get(1);
        }
        patientRecord.setStageAddNum(1);
        patientRecord.setPersonId(patientInfo.getPersonId());
        if ("1".equals(recordTemplate.getFollowFrequency())){
            //限制频次
            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
                logger.info("生成随访计划,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                return;
            }
            if(isCreateRecord(now, patientRecord, recordTemplate)){
                if(Objects.isNull(patientInfo)){
                    logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                    return;
                }
                if(!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),patientInfo.getResearchId())){
                    logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}",patientInfo.getPatientId());
                    return;
                }
                listTotal.add(new PatientRecord(patientRecord, recordTemplate));
                insertList.add(new PatientRecord(patientRecord, recordTemplate));
            }
        }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
            if(isCreateRecord(now, patientRecord, recordTemplate)){
                if(Objects.isNull(patientInfo)){
                    logger.error("患者信息为空 patientId:{}",patientRecord.getPatientId());
                    return;
                }
                if(!Objects.isNull(patientInfo.getPlanResearchId()) && !Objects.equals(patientInfo.getPlanResearchId(),patientInfo.getResearchId())){
                    logger.info("已经切换了队列,旧队列不再生成新的随访节点,patientId:{}",patientInfo.getPatientId());
                    return;
                }
                listTotal.add(new PatientRecord(patientRecord, recordTemplate));
                insertList.add(new PatientRecord(patientRecord, recordTemplate));
            }
        }
    }


    /**
     * 判断生成的下一个随访时间是否在当前时间之前或当天，如果是则可以生成随访记录
     *
     * @param now 当前时间（基准时间）
     * @param patientRecord 患者随访记录（包含上次锁定的随访时间）
     * @param prt 随访计划模板（包含随访间隔单位和数值）
     * @return true-可以生成随访记录（下次随访时间 <= 当前时间）；false-不可生成
     */
    public boolean isCreateRecord(Date now, PatientRecord patientRecord, PatientRecordTemplate prt) {
        // 参数校验：患者记录、模板、锁定随访时间任一为空则不能生成
        if (patientRecord == null || prt == null || patientRecord.getLockVisitTime() == null) {
            return false;
        }

        Date nextDate = null;
        // 获取随访间隔单位：1-天，2-周，3-月
        String unit = prt.getFollowIntervalUnit();
        // 获取随访间隔数值（整数）
        int interval = prt.getFollowInterval() != null ? prt.getFollowInterval().intValue() : 0;

        // 根据单位类型计算下次应随访的日期（基于上次锁定的随访时间）
        if ("1".equals(unit)) {
            // 按天计算下次随访日期：上次锁定时间 + 间隔天数
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), interval);
        } else if ("2".equals(unit)) {
            // 按周计算下次随访日期：上次锁定时间 + 间隔周数
            nextDate = DateUtils.addWeeks(patientRecord.getLockVisitTime(), interval);
        } else if ("3".equals(unit)) {
            // 按月计算下次随访日期：上次锁定时间 + 间隔月数
            nextDate = DateUtils.addMonths(patientRecord.getLockVisitTime(), interval);
        } else {
            // 未知单位类型（非1/2/3），无法计算，返回false
            return false;
        }

        // 计算失败（异常情况），返回false
        if (nextDate == null) {
            return false;
        }

        // 判断下次随访时间是否 <= 当前时间
        // 条件：同一天 或者 当前时间晚于下次随访时间 → 说明已到或超过随访时间，可以生成
        // 举例：上次锁定时间=2026-04-01，间隔30天，则下次随访日期=2026-05-01
        //情况1- 当前时间=2026-05-01（当天）→ 返回 true
        //情况2- 当前时间=2026-05-03（已逾期）→ 返回 true
        //情况3- 当前时间=2026-04-28（未到期）→ 返回 false
        if (DateUtils.isSameDay(now, nextDate) || now.after(nextDate)) {
            return true;
        }
        return false;
    }



    /**
     * 随访计划单次执行
     * @param {"researchIds":[1,2],"handleDate":"2022-09-20"}
     */
    @XxlJob("followUpPlanJobHandlerSingle")
    public void followUpPlanJobHandlerSingle(){
        String loggerName = "日志："+XxlJobHelper.getJobLogFileName()+",";
        String param = XxlJobHelper.getJobParam();
        if (!StringUtils.isNotBlankAndNotEmpty(param)){//参数为空不执行
            XxlJobHelper.handleFail(loggerName +"参数有误:"+param+"示例：{\"researchIds\":[1,2],\"handleDate\":\"yyyy-MM-dd\"}");
            return;
        }
        FollowUpPlanJobHandlerForm followUpPlanJobHandlerForm = null;
        try{
            param = param.trim();
            followUpPlanJobHandlerForm = JSON.parseObject(param, FollowUpPlanJobHandlerForm.class);
            DateUtils.parseDate(followUpPlanJobHandlerForm.getHandleDate());
        }catch (Exception e){
            XxlJobHelper.handleFail(loggerName +"参数有误:"+param+"示例：{\"researchIds\":[1,2],\"handleDate\":\"yyyy-MM-dd\"}");
            return;
        }

        if (followUpPlanJobHandlerForm == null || followUpPlanJobHandlerForm.getResearchIds() == null
                || followUpPlanJobHandlerForm.getResearchIds().isEmpty() || !StringUtils.isNotBlankAndNotEmpty(followUpPlanJobHandlerForm.getHandleDate())){
            XxlJobHelper.handleFail(loggerName +"参数有误:"+param+"示例：{\"researchIds\":[1,2],\"handleDate\":\"yyyy-MM-dd\"}");
            return;
        }
        if (followUpPlanJobHandlerForm.getResearchIds().size() > 10){
            XxlJobHelper.handleFail(loggerName +"单次执行researchIds数量不能超过10条:"+param);
            return;
        }
        Long[] researchIds = followUpPlanJobHandlerForm.getResearchIds().toArray(new Long[0]);
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByIds(researchIds);
        if (researchInfos == null || researchInfos.isEmpty()){
            XxlJobHelper.log(loggerName + "单次执行生成随访计划,没有查询到队列!");
        }
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchIds(researchIds);
        if (patientRecordTemplates == null || patientRecordTemplates.isEmpty()){
            XxlJobHelper.log(loggerName + "单次执行生成随访计划,没有查询到队列的随访规则!");
            return;
        }
        Map<Long, List<PatientRecordTemplate>> listMap = patientRecordTemplates.stream().collect(Collectors.groupingBy(PatientRecordTemplate :: getResearchId));
        List<Long> errors = new ArrayList<>();
        final String handleDate = followUpPlanJobHandlerForm.getHandleDate();
        researchInfos.forEach(x->handle(x,listMap.get(x.getId()), handleDate,r->errors.add(r)));
        String msg = loggerName +"随访计划定时任务日期"+DateUtils.getTime()+"结束,成功：" + (researchInfos.size()-errors.size())+"次,失败：" + errors.size()+"次,失败researchId[]："+ JSONArray.toJSONString(errors) + "。";
        XxlJobHelper.log(msg);
        if (errors.size() > 0){
            XxlJobHelper.handleFail(msg);
        }else{
            XxlJobHelper.handleSuccess(msg);
        }
    }

    /**
     * 生成档案
     * @param researchInfo
     * @param patientRecordTemplates
     */
    @Transactional
    public void handle(ResearchInfo researchInfo, List<PatientRecordTemplate> patientRecordTemplates, String handleDate, Consumer<Long> fun){
        try{
            XxlJobHelper.log("------------------队列{}开始生成档案------------------",researchInfo.getId());
            if (patientRecordTemplates == null || patientRecordTemplates.isEmpty()){
                XxlJobHelper.log("生成随访计划,队列ID{}没有对应随访规则",researchInfo.getId());
                return;
            }
            patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
            Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
            List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordByResearchReleaseIdAndDate(researchInfo.getId(), handleDate);
            if (patientRecords == null || patientRecords.isEmpty()){
                XxlJobHelper.log("生成随访计划,队列ID{}没有对应患者",researchInfo.getId());
            }
            List<PatientRecord> insertRecord = new ArrayList<>();
            patientRecords.forEach(x->{
                if (!maps.containsKey(x.getFollowId())){
                    return;
                }
                PatientRecordTemplate recordTemplate = maps.get(x.getFollowId());
                if ("1".equals(recordTemplate.getFollowFrequency())){//限制频次
                    if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
                        XxlJobHelper.log("生成随访计划,队列ID{}随访规则ID{}followTotal没有值",researchInfo.getId(),recordTemplate.getFollowId());
                        return;
                    }
                    if (x.getStageNum().intValue() == recordTemplate.getFollowTotal().intValue()){//完成当前随访节点进入下一个随访节点
                        int index = patientRecordTemplates.indexOf(recordTemplate);
                        if (index+1 <= patientRecordTemplates.size()){
                            insertRecord.add(new PatientRecord(x, patientRecordTemplates.get(index + 1)));
                        }
                    }else if (x.getStageNum().intValue() < recordTemplate.getFollowTotal().intValue()){//未完成当前随访节点继续生成当前随访节点计划
                        insertRecord.add(new PatientRecord(x, recordTemplate));
                    }
                }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
                    insertRecord.add(new PatientRecord(x, recordTemplate));
                }
            });
            if (!insertRecord.isEmpty()){
                patientRecordMapper.batchInsertPatientRecord(insertRecord);
            }
        }catch(Exception e){
            fun.accept(researchInfo.getId());
            XxlJobHelper.log("队列{}生成档案失败", researchInfo.getId());
            XxlJobHelper.log(e);
        }finally {
            XxlJobHelper.log("------------------队列{}生成档案结束------------------", researchInfo.getId());
        }
    }

    /**
     * 超窗定时
     * 参数未超窗天数  数字类型
     */
    @XxlJob("overTimeJobHandLer")
    public void overTimeJobHandLer(){
        String loggerName = "日志："+XxlJobHelper.getJobLogFileName()+",";
        try{
            String param = XxlJobHelper.getJobParam();
            int overDayNum = 0;//默认超窗天数
            if (StringUtils.isNotBlankAndNotEmpty(param)){
                if (!StringUtils.isNumeric(param.trim())){
                    XxlJobHelper.handleFail(loggerName + "参数有误："+ param);
                    return;
                }
                overDayNum = Integer.parseInt(param.trim());
            }
            Date overDay = DateUtils.addDays(new Date(), -overDayNum);
            String overDayStr = DateUtils.dateTime(overDay);
            patientRecordMapper.updatePatientRecordOverTimeStatus(overDayStr);

            // 超窗通知
            overTimeTipJob(overDayNum);
        }catch (Exception e){
            XxlJobHelper.log(e.getMessage());
            XxlJobHelper.handleFail(loggerName + "定时超窗执行失败!");
        }
        XxlJobHelper.handleSuccess(loggerName + "定时超窗执行成功!");
    }

    private void overTimeTipJob(int overDayNum) {

        XxlJobHelper.log("超窗任务提醒日期{}开始",DateUtils.getTime());
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setOverTimeStatus("1");

//        int overDayNum = 3;//默认超窗天数
        Date overDay = DateUtils.addDays(new Date(), -overDayNum);
        patientRecord.setOverTimeCreateTime(overDay);
        List<PatientRecord> patientRecords = patientRecordMapper.selectListByOverTime(patientRecord);

        XxlJobHelper.log("超窗任务提醒,提醒档案{}条",patientRecords.size());
        if (CollectionUtils.isEmpty(patientRecords)) {
            return;
        }

        Map<Long, List<PatientRecord>> researchMap = patientRecords.stream().collect(Collectors.groupingBy(PatientRecord::getResearchId));
        researchMap.keySet().forEach(reId -> {
            List<ResearchUserRole> users = researchUserRoleMapper.findUserRoleByResearchId(reId);
            users.forEach(s -> {
                List<PatientRecord> researchPatientRecords = researchMap.get(reId);
                researchPatientRecords.stream().forEach(rpr -> {
                    AccountNotice accountNotice = new AccountNotice(s, rpr, "患者有新的随访超窗请尽快处理", "17");
                    accountNoticeMapper.insertAccountNotice(accountNotice);
                });
            });
        });
        XxlJobHelper.handleSuccess( "超窗任务提醒执行成功！");
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        List<Long> list = new ArrayList<>();
        list.add(1l);
        list.add(2l);
        map.put("researchIds",list);
        map.put("handleDate",DateUtils.getTime());
        System.out.println("args = " + JSON.toJSONString(map));
        int overDayNum = 3;
        System.out.println("args = " + (-overDayNum+5));

    }

    @XxlJob("followTip")
    public void followTip(){
        String loggerName = "日志："+XxlJobHelper.getJobLogFileName()+",";
        String param = XxlJobHelper.getJobParam();
        System.out.println("param = " + param);
        System.out.println("loggerName = " + loggerName);
        System.out.println("XxlJobHelper.getJobLogFileName() = " + XxlJobHelper.getJobLogFileName());
        System.out.println("XxlJobHelper.getShardIndex() = " + XxlJobHelper.getShardIndex());
        System.out.println("XxlJobHelper.getJobId() = " + XxlJobHelper.getJobId());
        System.out.println("XxlJobHelper.handleResult() = " + XxlJobHelper.handleResult(1,""));
    }


    /**
     * 同步随访数据
     */
    public void getFollowDate(Long followId,String personId,Long researchId,Long recordId) {//
//        Long followId=92L;
//        Long personId=10003930L;
//        Long researchId=44L;
//        Long recordId=1266L;
        List<TextData> dataList = new ArrayList<>();
        //根绝followId获取随访表单内容
        List<FMedicalProperty> medicalPropertyList=medicalPropertyMapper.selectFMedicalPropertyByFollowId(followId);
        //根据属性查询表
        List<String> propertyCodeList=medicalPropertyList.stream().map(FMedicalProperty::getPropertyCode).collect(Collectors.toList());
        //获取需要拉取的属性
        List<FollowProperty> tableList=followPropertyMapper.getFollowProperty(followId,propertyCodeList);
        if (CollectionUtils.isNotEmpty(tableList)){
            Map<String,FMedicalProperty> medicalPropertMap=medicalPropertyList.stream().collect(Collectors.toMap(FMedicalProperty::getPropertyCode,fMedicalProperty->fMedicalProperty));

            Map<String,List<FollowProperty>> tableMap = tableList.stream().collect(Collectors.groupingBy(FollowProperty::getPropertyTable));
            for (Map.Entry<String, List<FollowProperty>> entry : tableMap.entrySet()) {
                String tableName = entry.getKey();
                List<FollowProperty> propertyList = entry.getValue();
                //String columns=propertyList.stream().map(FollowProperty::getPropertyCode).collect(Collectors.joining(","));
                StringBuilder condition= new StringBuilder();
                for (FollowProperty property : propertyList) {
                    if (StringUtils.isNotEmpty(property.getConditions())) {
                        condition.append(property.getConditions()).append(" or ");
                    }
                }
                condition.delete(condition.length()-3, condition.length());
                List<Map<String,Object>>  result = followPropertyMapper.getResult(tableName, condition.toString(),personId);
                if (result!=null){
                    for (Map<String,Object> map : result) {
                        for (FollowProperty property : propertyList) {
                            System.out.println(map.get("propertyName"));
                            System.out.println(property.getPropertyName());
                            String propertyName = String.valueOf(map.get("propertyName")).trim();
                            if (property.getPropertyName().equals(propertyName)) {
                                TextData textData = new TextData();
                                textData.setPropertyValue(String.valueOf(map.get(property.getColumnName())));
                                textData.setPropertyId(medicalPropertMap.get(property.getPropertyCode()).getId());
                                textData.setPropertyCode(property.getPropertyCode());
                                textData.setFormId(medicalPropertMap.get(property.getPropertyCode()).getMedicalFormId());
                                dataList.add(textData);
                            }
                        }

                    }

                }
            }
        }

        if (CollectionUtils.isNotEmpty(dataList)){
            followPropertyMapper.insertTextData(researchId,recordId,dataList);
        }


    }


    /**
     * 肥胖症基线数据拉取
     */
    @XxlJob("getBaseData")
    public void getBaseData(){
        XxlJobHelper.log("肥胖症基线数据拉取定时任务日期{}开始",DateUtils.getTime());
        //进行中的队列
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByStatus2();
        if (researchInfos == null || researchInfos.isEmpty()){
            XxlJobHelper.log("没查到相关队列信息");
        }
        for(ResearchInfo researchInfo:researchInfos){
            //查询需要拉取的患者
            List<PatientRecord> patientRecordList = patientRecordMapper.selectPatientRecordByResearchId(researchInfo.getId());
            for (PatientRecord record : patientRecordList) {
                patientRecordMapper.insertPatientRecord(record);
                logger.info("表单id：{}，患者id：{}，队列id：{}，recordId:{}",record.getFollowId(),record.getPersonId(),researchInfo.getId(),record.getRecordId());
                getFollowDate(record.getFollowId(),record.getPersonId(),researchInfo.getId(),record.getRecordId());
            }
        }
    }


    public void testXxjobGeneratedManually(Long id,Long patientId,Date specifyTheTime) {
        XxlJobHelper.log("随访计划定时任务日期{}开始",DateUtils.getTime());
        //进行中的队列
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(id);
        //所有进行中队列的规则
        String resultStr = "";
        List<PatientRecord> insertRecord = new ArrayList<>();
        testXxjobInsertRecordJob(researchInfo.getId(),patientId, insertRecord,specifyTheTime);
        resultStr = "操作成功,共插入"+insertRecord.size()+"条";
        XxlJobHelper.handleSuccess(resultStr);
    }
    public void testXxjobInsertRecordJob(Long researchId,Long patientId,List<PatientRecord> listTotal,Date now){
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        if(researchInfo!=null && "1".equals(researchInfo.getResearchStatus())){
            PatientRecordTemplate pt = new PatientRecordTemplate();
            pt.setResearchId(researchId);
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(pt);
            if(patientRecordTemplates.size()>0){
                List<PatientRecord> insertList = new ArrayList<>();
                patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
                //查询的是同一个队列中同一个登录账号下[由于A->B->A可能 有多个patientId]的最大的随访记录，不是所有的，这也是为啥多次插入而不重复插入数据的根因
                List<PatientRecord> patientRecords = patientRecordMapper.testXxjobGeneratedManually(researchInfo.getId(),patientId);
                XxlJobHelper.log("随访计划定时任务执行队列id:{},查询可执行周期数:{}",researchInfo.getId(),patientRecords.size());
                patientRecords.forEach(patientRecord->{
                    if(patientRecord.getAccountId()==0){
                        return;
                    }
                    if (!maps.containsKey(patientRecord.getFollowId())){
                        return;
                    }
                    if(patientRecord.getLockVisitTime()==null){
                        return;
                    }
                    PatientRecordTemplate recordTemplate = maps.get(patientRecord.getFollowId());
                    XxlJobHelper.log("---------------------------recordId:{}",patientRecord.getRecordId());
                    PatientInfo patientInfo = getPatientInfo(patientRecord);
                    //说明是基线,会创建随访1的PatientRecord记录
                    if(recordTemplate.getFollowNum()==0){
                        handleBaseLine(listTotal,insertList, researchInfo, now, patientRecordTemplates, patientRecord,
                                recordTemplate,patientInfo);
                    }else{
                        if (!"4".equals(patientInfo.getPatientStatus())) {
                            handleFollowUp(listTotal,insertList,researchInfo, now, patientRecordTemplates, patientRecord,
                                    recordTemplate,patientInfo);
                        }
                    }
                });
                if(insertList.size()>0){
                    //肥胖综合征
                    List<Long> list=followPropertyMapper.getDict("research");
                    if (list.contains(researchId)){
                        for (PatientRecord record : insertList) {
                            patientRecordMapper.insertPatientRecord(record);
                            logger.info("表单id：{}，患者id：{}，队列id：{}，recordId:{}",record.getFollowId(),record.getPersonId(),researchId,record.getRecordId());
                            getFollowDate(record.getFollowId(),record.getPersonId(),researchId,record.getRecordId());
                        }
                    }else{
                        patientRecordMapper.batchInsertPatientRecord(insertList);
                    }
                    XxlJobHelper.log("----------------------------------------队列id:{}插入周期数量:{}-----------------",researchId,insertList.size());
                }
            }
        }
    }
}
