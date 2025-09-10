package com.dchealth.web.job;

import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.account.service.impl.UmpushService;
import com.dchealth.common.enums.DataSourceType;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SplitListUtils;
import com.dchealth.framework.datasource.DynamicDataSourceContextHolder;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.notice.noticevo.HisNoticeVo;
import com.dchealth.notice.service.HisNoticeService;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.ResearchOutbound;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.research.mapper.ResearchOutboundMapper;
import com.dchealth.system.domain.PatientAccount;
import com.dchealth.system.mapper.PatientAccountMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Auther: zdd
 * @Date: 2023/9/6
 */
@Component
public class FollowRemindJob {

    private static Logger log = LoggerFactory.getLogger(FollowRemindJob.class);

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private ResearchOutboundMapper researchOutboundMapper;
    @Autowired
    private AccountNoticeMapper accountNoticeMapper;
    @Autowired
    private UmpushService umpushService;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Resource
    private PatientAccountMapper ppatientAccountMapper;

    @Resource
    private HisNoticeService hisNoticeService;


    /**
     * App站内信随访提醒和超窗提醒
     *
     */
    @XxlJob("remindMailJobHandler")
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void remindMailJobHandler() throws Exception {
        XxlJobHelper.log("App站内信随访提醒和超窗提醒任务日期{}开始",DateUtils.getTime());
        //进行中的队列
        ResearchInfo ri = new ResearchInfo();
        ri.setResearchStatus("1");
        List<ResearchInfo> researchInfoList = researchInfoMapper.selectResearchInfoList(ri);
        if (researchInfoList == null || researchInfoList.isEmpty()){
            XxlJobHelper.log("App站内信随访提醒和超窗提醒,没有查询到进行中的队列!");
        }
        //所有进行中队列的规则
        String resultStr = "";
        List<AccountNotice> insertRecord = new ArrayList<>();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20, 50, 4, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        List<List<ResearchInfo>> splitList = SplitListUtils.splitList(researchInfoList, 10);
        //记录单个任务的执行次数
        CountDownLatch countDownLatch = new CountDownLatch(splitList.size());
        for (List<ResearchInfo> splist : splitList) {
            threadPoolExecutor.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i=0;i<splist.size();i++) {
                        ResearchInfo r = splist.get(i);
                        insertRecordAccountNotice(r.getId(),insertRecord);
                    }
                }
            }));
            // 任务个数 - 1, 直至为0时唤醒await()
            countDownLatch.countDown();
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
        boolean allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
        int u=0;
        if (allThreadsIsDone) {
            if (!insertRecord.isEmpty() && u==0){

                accountNoticeMapper.batchInsertList(insertRecord);

                List<HisNoticeVo> hisNoticeVos = new ArrayList<>();
                for(AccountNotice a:insertRecord){
                    PatientAccount patientAccount = ppatientAccountMapper.selectPatientAccountByAccountId(a.getAccountId());
                    // 同步his微信站内消息
                    HisNoticeVo hisNoticeVo = new HisNoticeVo();
                    hisNoticeVo.setInfoNo("tdmbsf"+ System.currentTimeMillis());
                    hisNoticeVo.setInsertTime(new Date());
                    hisNoticeVo.setPatientId(String.valueOf(patientAccount.getRegistrySigns()));
                    hisNoticeVo.setContactPhone(patientAccount.getMobile());
                    hisNoticeVo.setInfoContent(a.getContent());
                    hisNoticeVo.setInfoSources("tdmbsfxt");
                    hisNoticeVo.setInfoStatus("1");
                    hisNoticeVo.setInfoTitle(a.getTitle());
                    hisNoticeVo.setPatientName(patientAccount.getName());
                    hisNoticeVos.add(hisNoticeVo);
                    sendPush(2,a);
                }

                DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.HISMYSQL.name());
                try {
                    // 执行数据库操作
                    int i = hisNoticeService.batchInsert(hisNoticeVos);
                    log.info("His消息通知批量插入结果:"+i);
                } finally {
                    // 恢复默认数据源
                    DynamicDataSourceContextHolder.clearDataSourceType();
                }

                resultStr = "操作成功,共插入"+insertRecord.size()+"条";
                XxlJobHelper.log("App站内信随访提醒和超窗提醒{}",resultStr);
                u++;
            }
        }
        while (!allThreadsIsDone) {
            allThreadsIsDone = threadPoolExecutor.getTaskCount() == threadPoolExecutor.getCompletedTaskCount();
            if (allThreadsIsDone) {
                if (!insertRecord.isEmpty() && u==0){
                    accountNoticeMapper.batchInsertList(insertRecord);
                    for(AccountNotice a:insertRecord){
                        sendPush(2,a);
                    }
                    resultStr = "操作成功,共插入"+insertRecord.size()+"条";
                    XxlJobHelper.log("App站内信随访提醒和超窗提醒{}",resultStr);
                    u++;
                }
            }
        }
        XxlJobHelper.handleSuccess(resultStr);
    }



    //app站内信，随访提醒和超窗提醒
    public void insertRecordAccountNotice(Long researchId,List<AccountNotice> insertList){
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        ResearchOutbound researchOutbound = researchOutboundMapper.selectByResearchIdAndType(researchId,"3"/*站内信*/);
        long TimeNow = System.currentTimeMillis();
        Date now = new Date(TimeNow);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(researchOutbound!=null && researchInfo!=null && "1".equals(researchInfo.getResearchStatus()) && getTimeAccess(researchOutbound.getFollowAdvanceTime(),hour)){
            PatientRecordTemplate pt = new PatientRecordTemplate();
            pt.setResearchId(researchId);
            List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateList(pt);
            if(patientRecordTemplates.size()>0){
                patientRecordTemplates.sort(Comparator.comparing(PatientRecordTemplate :: getFollowNum));
                Map<Long, PatientRecordTemplate> maps = patientRecordTemplates.stream().collect(Collectors.toMap(PatientRecordTemplate :: getFollowId, Function.identity()));
                List<PatientRecord> patientRecords = patientRecordMapper.selectMaxRecordByResearchId(researchInfo.getId());
                patientRecords.forEach(x->{
                    if (!maps.containsKey(x.getFollowId())){
                        return;
                    }
                    if(x.getLockVisitTime()==null){
                        return;
                    }
                    PatientRecordTemplate recordTemplate = maps.get(x.getFollowId());
                    if(recordTemplate.getFollowNum()==0){
                        //说明当前是基线,下次为第一次生成随访信息
                        int index = patientRecordTemplates.indexOf(recordTemplate);
                        if (index+1 < patientRecordTemplates.size()){
                            if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                insertList.add(new AccountNotice(x,researchOutbound,"6"));
                            }
                        }
                    }else{
                        //限制频次
                        if("1".equals(recordTemplate.getFollowFrequency())){
                            if (recordTemplate.getFollowTotal() == null || recordTemplate.getFollowTotal() < 1){
                                log.info("app站内信提醒,队列ID{}随访规则ID{}followTotal没有值", researchInfo.getId(), recordTemplate.getFollowId());
                                return;
                            }
                            if (x.getStageNum().intValue() == recordTemplate.getFollowTotal().intValue()) {//完成当前随访节点进入下一个随访节点
                                int index = patientRecordTemplates.indexOf(recordTemplate);
                                if (index+1 < patientRecordTemplates.size()){
                                    if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                        insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                    }
                                }
                            }else if (x.getStageNum().intValue() < recordTemplate.getFollowTotal().intValue()){//未完成当前随访节点继续生成当前随访节点计划
                                if(isCreateFollowRecord(now, x, recordTemplate,researchOutbound.getFollowAdvanceDays())){
                                    insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                }
                            }
                        }else if ("2".equals(recordTemplate.getFollowFrequency())){//不限频次
                            //没设置次数且间隔为0天
                            if(recordTemplate.getFollowTotal()==null && recordTemplate.getFollowInterval()==0){
                                int index = patientRecordTemplates.indexOf(recordTemplate);
                                if (index+1 < patientRecordTemplates.size()){
                                    if(isCreateFollowRecord(now, x, patientRecordTemplates.get(index + 1),researchOutbound.getFollowAdvanceDays())){
                                        insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                    }
                                }
                            }else{
                                if(isCreateFollowRecord(now, x, recordTemplate,researchOutbound.getFollowAdvanceDays())){
                                    insertList.add(new AccountNotice(x,researchOutbound,"6"));
                                }
                            }
                        }
                    }
                });
            }
        }

        if(researchOutbound!=null && researchInfo!=null && "1".equals(researchInfo.getResearchStatus()) && getTimeAccess(researchOutbound.getOverAdvanceTime(),hour)){
            //超窗提醒,超过随访时间1天要提醒
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setResearchId(researchId);
            patientRecord.setFillStatus("0");
            Date overDay = DateUtils.addDays(new Date(), -researchOutbound.getOverAdvanceDays());
            String overDayStr = DateUtils.dateTime(overDay);
            patientRecord.setOverDay(overDayStr);
            List<PatientRecord> patientRecordList = patientRecordMapper.selectListByOverTime(patientRecord);
            if(patientRecordList.size()>0){
                for(PatientRecord p:patientRecordList){
                    PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(p.getPatientId());
                    String patientStatus = patientInfo.getPatientStatus();
                    if (!"4".equals(patientStatus)) {
                        insertList.add(new AccountNotice(p,researchOutbound,"7"));
                    }
                }
            }
        }
    }

    //判断生成的下一个随访时间在提前提醒时间，是否在当前日期之前，如果是为true
    public boolean isCreateFollowRecord(Date now, PatientRecord patientRecord, PatientRecordTemplate prt,Integer i) {
        Date nextDate = null;
        if (StringUtils.equals("1", prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() - i);
        } else if ("2".equals(prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 7 - i);
        } else if ("3".equals(prt.getFollowIntervalUnit())) {
            nextDate = DateUtils.addDays(patientRecord.getLockVisitTime(), prt.getFollowInterval().intValue() * 30 - i);
        }
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoByPatientId(patientRecord.getPatientId());
        String patientStatus = patientInfo.getPatientStatus();
        if ("4".equals(patientStatus)) {
            return false;
        }
        if (DateUtils.dateTime(now).equals(DateUtils.dateTime(nextDate))) {
            return true;
        }
//        if (nextDate != null && now.after(nextDate)) {
//            return true;
//        }
        return false;
    }

//    public AccountNotice getAccoutnNotice(PatientRecord patientRecord,ResearchOutbound researchOutbound,String type){
//        AccountNotice accountNotice = new AccountNotice();
//        accountNotice.setAccountId(patientRecord.getAccountId());
//        accountNotice.setRecordId(patientRecord.getRecordId());
//        accountNotice.setType(type);
//        accountNotice.setCreateTime(DateUtils.getNowDate());
//        accountNotice.setIsRead(Constants.YES);
//        if("6".equals(type)){
//            accountNotice.setTitle(MedicalContants.MESSAGE_TITLE.get("6"));
//            accountNotice.setContent(researchOutbound.getFollowMailContent());
//            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
//            accountNoticeVo.setRecordId(accountNotice.getRecordId());
//            accountNoticeVo.setType("6");
//            accountNoticeVo.setResearchId(patientRecord.getResearchId());
//            accountNoticeVo.setPatientId(patientRecord.getPatientId());
//            accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo));
//        }else if("7".equals(type)){
//            accountNotice.setTitle(MedicalContants.MESSAGE_TITLE.get("7"));
//            accountNotice.setContent(researchOutbound.getOverMailContent());
//            AccountNoticeVo accountNoticeVo = new AccountNoticeVo();
//            accountNoticeVo.setRecordId(accountNotice.getRecordId());
//            accountNoticeVo.setType("7");
//            accountNoticeVo.setStageName(patientRecord.getName());
//            accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(accountNoticeVo));
//        }
//        return accountNotice;
//    }

    //根据App站内信提醒时间和当前小时判断是否在符合时间内
    public boolean getTimeAccess(String type,int hour){
        if(hour<7 && hour>20){
            return false;
        }
        if("1".equals(type)){
            if(hour>=7 && hour <8){
                return true;
            }
        }else if("2".equals(type)){
            if(hour>=8 && hour <9){
                return true;
            }
        }else if("3".equals(type)){
            if(hour>=9 && hour <10){
                return true;
            }
        }else if("4".equals(type)){
            if(hour>=10 && hour <11){
                return true;
            }
        }else if("5".equals(type)){
            if(hour>=11 && hour <12){
                return true;
            }
        }else if("6".equals(type)){
            if(hour>=12 && hour <13){
                return true;
            }
        }else if("7".equals(type)){
            if(hour>=13 && hour <14){
                return true;
            }
        }else if("8".equals(type)){
            if(hour>=14 && hour <15){
                return true;
            }
        }else if("9".equals(type)){
            if(hour>=15 && hour <16){
                return true;
            }
        }else if("10".equals(type)){
            if(hour>=16 && hour <17){
                return true;
            }
        }else if("11".equals(type)){
            if(hour>=17 && hour <18){
                return true;
            }
        }else if("12".equals(type)){
            if(hour>=18 && hour <19){
                return true;
            }
        }else if("13".equals(type)){
            if(hour>=19 && hour <=20){
                return true;
            }
        }
        return false;
    }

    public void sendPush(Integer appType,AccountNotice accountNotice){
        if (accountNotice.getAccountId() != null){
//            umpushService.sendCustomizedcastMessage(appType,String.valueOf(accountNotice.getAccountId()),accountNotice.getContent(),
//                    accountNotice.getExtendJson());
        }
    }


}
