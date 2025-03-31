package com.dchealth.web.job;

import cn.hutool.core.date.DateUtil;
import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.account.service.impl.UmpushService;
import com.dchealth.common.constant.ResearchConstant;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.framework.web.domain.server.Sys;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.mapper.ResearchPatientMapper;
import com.dchealth.msgTip.domain.MsgReminderTask;
import com.dchealth.msgTip.domain.MsgSend;
import com.dchealth.msgTip.domain.SysMessage;
import com.dchealth.msgTip.mapper.MsgReminderTaskMapper;
import com.dchealth.msgTip.mapper.SysMessageMapper;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.mapper.ResearchInfoMapper;
import com.dchealth.system.mapper.SysUserMapper;
import com.dchealth.warn.domain.WarnEarlyRecord;
import com.dchealth.warn.mapper.WarnEarlyRecordMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @Auther: cuitao
 * @Date: 2023/6/5 - 06 - 05 - 13:54
 * @Description: com.dchealth.web.job
 */
@Component
public class FollowTipJob {

    private static Logger logger = LoggerFactory.getLogger(FollowTipJob.class);
    private static final int threadPoolSize = 10;

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private ResearchPatientMapper researchPatientMapper;
    @Autowired
    private MsgReminderTaskMapper msgReminderTaskMapper;
    @Autowired
    private SysMessageMapper sysMessageMapper;
    @Autowired
    private AccountNoticeMapper accountNoticeMapper;

    @Autowired
    private UmpushService umpushService;
    @Autowired
    private WarnEarlyRecordMapper warnEarlyRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;

    //        // 通过线程池，减轻数据插入压力
//        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);
//        CountDownLatch countDownLatch = new CountDownLatch(threadPoolSize);
//        List<PatientRecord> insertRecord = new ArrayList<>();
//        List<List<ResearchInfo>> splitList = SplitListUtils.splitList(researchInfos, threadPoolSize);
//
//

    /**
     * 质疑、预警等固定条件提醒
     * * msgType 消息类型： 1:随访前提醒;2:随访后提醒;3：超窗提醒;4:预警提醒；5：随访意见；6：质疑提醒；7入组提醒；
     *      * 8：质疑未处理；9预警未处理；10随访未提交；11：满意度评价；12：随访处理任务；13：超窗处理任务；13：预警处理任务
     * @throws Exception
     */
//    @XxlJob("followRegularTipJobHandler")
//    @Scheduled(cron = "0 0/2 * * * ?")
//    public void followRegularTipJobHandler() throws Exception {
//        // 质疑未处理 患者有七天未处理的质疑时生成提醒
//        SysMessage questionMsg = new SysMessage();
//        questionMsg.setMsgType(8);
//        questionMsg.setMsgContent("医生向您发起的质疑超过7天未处理，请尽快查看");
//        sysMessageMapper.saveQuestionMsg(6L,questionMsg);
//        // 预警未处理 患者有七天未处理的质疑时生成提醒
//        SysMessage warnMsg = new SysMessage();
//        warnMsg.setMsgType(9);
//        warnMsg.setMsgContent("您的随访预警超过7天未处理，请尽快查看");
//        sysMessageMapper.saveWarnMsg(6L,warnMsg);
//        // 随访未提交 患者有七天未提交的随访时生成提醒
//        SysMessage followMsg = new SysMessage();
//        followMsg.setMsgType(10);
//        followMsg.setMsgContent("您还有7天前的随访未进行，请尽快完成");
//        sysMessageMapper.saveFollowMsg(6L,followMsg);
//
//    }

    @XxlJob("followRegularTipJobHandler")
//    @Scheduled(cron = "0 0/2 * * * ?")
//    @Scheduled(cron = "0 08 17 * * ?")
    public void followRegularTipJobHandler() throws Exception {
        XxlJobHelper.log("患者有七天未处理的质疑时生成提醒任务日期{}开始",DateUtils.getTime());
//        // 质疑未处理 患者有七天未处理的质疑时生成提醒
        PatientRecord patientRecord = new PatientRecord();
//        patientRecord.setResearchId(researchId);
        patientRecord.setQuestionStatus("1");
        Date overDay = DateUtils.addDays(new Date(), -7);
        String overDayStr = DateUtils.dateTime(overDay);
        patientRecord.setQuestionTimeOver(overDayStr);
        List<PatientRecord> patientRecordList = patientRecordMapper.selectListByOverTime(patientRecord);
        if(patientRecordList.size()>0){
            for(PatientRecord p:patientRecordList){
                String msgContent = "医生向您发起的质疑超过7天未处理，请尽快查";
                AccountNotice accountNotice = new AccountNotice(p,msgContent,"12");
                accountNoticeMapper.insertAccountNotice(accountNotice);
//                umpushService.sendCustomizedcastMessage(2,String.valueOf(p.getAccountId()),msgContent,accountNotice.getExtendJson());
            }
        }
        XxlJobHelper.log("患者有七天未处理的质疑时生成提醒任务结束,共提醒{}条",patientRecordList.size());


        // 预警未处理 医生有七天未处理的预警时生成提醒
        warnUntreatedNotice();

        // 随访未提交 患者有七天未提交的随访时生成提醒
        followUntreatedNotice();

    }

    /**
     * 医生对系统满意度调查提醒
     *
     * @throws Exception
     */
    @XxlJob("systemSatisfactionSurveyTipJobHandler")
    // @Scheduled(cron = "0 17 10 6 11 ?")
    public void systemSatisfactionSurveyTipJobHandler() throws Exception {
        XxlJobHelper.log("系统满意度评价提醒任务{}开始", DateUtils.getTime());
//        // 6.1和12.1 提醒系统医生对系统进行评价，医生范畴：启用医生除admin用户
        SysUser sysUser = new SysUser();
        sysUser.setDelFlag("0"); // 未删除
        sysUser.setStatus("0"); // 未禁用
        List<SysUser> sysUsers = sysUserMapper.selectUserList(sysUser);
        if (CollectionUtils.isNotEmpty(sysUsers)) {
            for (SysUser user : sysUsers) {
                if (StringUtils.equals(user.getUserName(), "admin")) {
                    continue;
                }
                String msgContent = "请您对系统的使用体验进行评价，您的宝贵建议是我们前进的动力哦~";
                AccountNotice accountNotice = new AccountNotice(user, msgContent, "15");
                accountNoticeMapper.insertAccountNotice(accountNotice);
            }
        }
        XxlJobHelper.log("系统满意度评价提醒任务结束,共提醒{}条", CollectionUtils.size(sysUsers));
    }

    private void followUntreatedNotice() {
        XxlJobHelper.log("患者有七天未处理的随访生成提醒任务，日期{}开始", DateUtils.getTime());
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setFollowStatus("0");
        Date overDay = DateUtils.addDays(new Date(), -7);
        patientRecord.setCreateTime(overDay);
        List<PatientRecord> patientRecordList = patientRecordMapper.selectListByOverTime(patientRecord);
        if (CollectionUtils.isNotEmpty(patientRecordList)) {
            for (PatientRecord p : patientRecordList) {
                String msgContent = "您还有7天前的随访未进行，请尽快完成";
                AccountNotice accountNotice = new AccountNotice(p, msgContent, "14");
                accountNotice.setIsRead("N");
                accountNoticeMapper.insertAccountNotice(accountNotice);
            }
        }
        XxlJobHelper.log("患者有七天未处理的随访生成提醒任务结束，共提醒{}条", patientRecordList.size());
    }

    private void warnUntreatedNotice() {
        XxlJobHelper.log("患者有七天未处理的预警生成提醒任务，日期{}开始", DateUtils.getTime());
        PatientRecord patientRecordWarn = new PatientRecord();
        patientRecordWarn.setWarnStatus("1");
        List<PatientRecord> patientRecordWarnList = patientRecordMapper.selectListByOverTime(patientRecordWarn);

        if (CollectionUtils.isEmpty(patientRecordWarnList)) {
            return;
        }
        List<Long> untreatedWarnIds = patientRecordWarnList.stream().map(PatientRecord::getRecordId).collect(Collectors.toList());
        Date warnOverDay = DateUtils.addDays(new Date(), -7);
        List<WarnEarlyRecord> warnEarlyRecords = warnEarlyRecordMapper.selectWarnEarlyRecordByIdList(untreatedWarnIds, warnOverDay);

        if (CollectionUtils.isNotEmpty(warnEarlyRecords)) {
            for (WarnEarlyRecord p : warnEarlyRecords) {
                String msgContent = "您的随访预警超过7天未处理，请尽快查看";
                Optional<PatientRecord> any = patientRecordWarnList.stream().filter(pr -> pr.getRecordId().longValue() == p.getRecordId().longValue()).findAny();
                if (any.isPresent()) {
                    AccountNotice accountNotice = new AccountNotice(any.get(), msgContent, "13");
                    accountNotice.setIsRead("N");
                    accountNoticeMapper.insertAccountNotice(accountNotice);
                }
            }
        }
        XxlJobHelper.log("患者有七天未处理的质疑时生成提醒任务结束，共提醒{}条", patientRecordWarnList.size());
    }

    /**
     * 队列设置-智能外呼配置的提醒
     * @throws Exception
     */
    @XxlJob("followTipJob")
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void followTipJobHandler() throws Exception {

        Map<Long, MsgSend> msgTipRecordIdMsgSendMap = new ConcurrentHashMap<>();

        // 查询所有的队列-》过滤出存活的队列
        //每个队列找出对应的周期设置模板
        // 按照随访节点遍历出对应的同周期节点（队列没有结束生成的节点）
        // 对每个节点进行随访到期判断
        //step1到期的进行三种消息发送
        //step2到期的进行红点处理
        //step3跳转详情页逻辑处理
        long startTime = System.currentTimeMillis();
        //查询进行中以及未删除的队列信息
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoByStatus(ResearchConstant.IN_PROCESS);
        if (CollectionUtils.isEmpty(researchInfos)) {
            XxlJobHelper.log("【生成定时任务执行计划】没有查询到进行中的队列!");
            return;
        }
        Map<Long, ResearchInfo> researchInfoMap = researchInfos.stream().collect(Collectors.toMap(ResearchInfo::getId
                , recordInfo -> recordInfo));
        Long[] researchIds = researchInfoMap.keySet().toArray(new Long[0]);

        List<PatientRecordTemplate> patientRecordTemplates =
                patientRecordTemplateMapper.selectPatientRecordTemplateByResearchIds(researchIds);
        if (CollectionUtils.isEmpty(patientRecordTemplates)) {
            XxlJobHelper.log("【生成定时任务执行计划】没有查询到任何一个队列的周期设置信息!");
            return;
        }

        Map<Long, List<PatientRecordTemplate>> patientRecordTemplateMap =
                patientRecordTemplates.stream().collect(Collectors.groupingBy(PatientRecordTemplate::getResearchId));
        patientRecordTemplateMap.keySet().forEach(researchId -> {
            List<PatientRecordTemplate> patientRecordTemplateList = patientRecordTemplateMap.get(researchId);
            if (CollectionUtils.isEmpty(patientRecordTemplateList)) {
                logger.error("通过队列id :{} 查询周期配置失败", researchId);
                return;
            }
            List<SysMessage> msgList = new ArrayList<>();
            //处理提醒逻辑
            List<MsgReminderTask> reminderTasks = msgReminderTaskMapper.queryListByResearchId(researchId);
            for(MsgReminderTask mrt : reminderTasks){
                if (Objects.isNull(mrt)) {
                    logger.error("队列:{}提醒配置信息查询异常", researchId);
                    return;
                }
                if(mrt.getFollowType()==1 && (mrt.getEarlierTime()!=null || mrt.getAfterTime()!=null)){
                    //随访提前n天提醒
                    this.dealTipInfo(researchId,msgList,patientRecordTemplateList,mrt,mrt.getFollowType());
                }else if(mrt.getFollowType()==2 && mrt.getUltraWindowTime()!=null){
                    //超窗n天后提醒
                    this.dealTipInfo(researchId,msgList,patientRecordTemplateList,mrt,mrt.getFollowType());
                }
            }
            //保存数据
            if(msgList.size()>0){
                sysMessageMapper.insertBatch(msgList);
            }
        });
        long endTime = System.currentTimeMillis();
        XxlJobHelper.log("随访定时提醒任务执行耗时:{}ms", endTime - startTime);
        XxlJobHelper.handleSuccess();
    }

    /**
     * 处理提醒
     * msgType 消息类型： 1:随访前提醒;2:随访后提醒;3：超窗提醒;4:预警提醒；5：随访意见；6：质疑提醒；7入组提醒；
     * 8：质疑未处理；9预警未处理；10随访未提交；11：满意度评价；12：随访处理任务；13：超窗处理任务；13：预警处理任务
     */
    private void dealTipInfo(Long researchId,List<SysMessage> msgList,
                             List<PatientRecordTemplate> patientRecordTemplateList,MsgReminderTask mrt,int followType){

        List<Long> followIdList =
                patientRecordTemplateList.stream().map(PatientRecordTemplate::getFollowId).sorted().collect(Collectors.toList());
        followIdList.forEach(followId -> {
            if(followType == 1){
                List<PatientRecord> patientRecordList = patientRecordMapper.selectPatientRecordByFollowId(followId);
                // 当前时间-随访提前提醒时间 = 随访提醒时间
                if (CollectionUtils.isEmpty(patientRecordList)) {
                    return;
                }
                patientRecordList.forEach(patientRecord -> {
                    //随访
                    Date tipTime = null;
                    if(mrt.getEarlierTime()!=null){
                        Date visitTime = patientRecord.getVisitTime();
                        //随访提前n天提醒时间
                        tipTime = DateUtils.getPreDate(mrt.getEarlierTime());
                        if (tipTime!=null && DateUtil.isSameDay(visitTime,tipTime)) {
                            SysMessage msg = getMessageInfo(2,1,patientRecord.getRecordId(),patientRecord,mrt);
                            if(msg!=null){
                                msgList.add(msg);
                            }
                        }
                    }
//                        if(mrt.getAfterTime()!=null){
//                            Date visitTime = patientRecord.getVisitTime();
//                            //随访之后n天提醒时间
//                            tipTime = DateUtils.getPreDate(-mrt.getAfterTime());
//                            if (tipTime!=null && DateUtil.isSameDay(visitTime,tipTime)) {
//                                SysMessage msg = getMessageInfo(2,2,patientRecord.getRecordId(),patientRecord,mrt);
//                                if(msg!=null){
//                                    msgList.add(msg);
//                                }
//                            }
//                        }
                });
            }else if(followType == 2) {
                List<PatientRecord> patientRecordList = patientRecordMapper.selectOverTimePatientRecordByFollowId(followId);
                // 当前时间-随访提前提醒时间 = 随访提醒时间
                if (CollectionUtils.isEmpty(patientRecordList)) {
                    return;
                }
                patientRecordList.forEach(patientRecord -> {
                    //超窗
                    Date visitTime = patientRecord.getVisitTime();
                    if (Objects.isNull(visitTime)) {
                        logger.error("队列:{} 节点:{} 的随访时间查询为空", researchId, followId);
                        return;
                    }
                    //判断超窗提醒
                    Date tipTime = DateUtils.getPreDate(-mrt.getUltraWindowTime());
                    if ( DateUtil.isSameDay(visitTime,tipTime)) {
                        SysMessage msg = getMessageInfo(2,3,patientRecord.getRecordId(),patientRecord,mrt);
                        if(msg!=null){
                            msgList.add(msg);
                        }
                    }
                });
            }
        });

    }

    /**
     * 获取消息内容
     * @param userType 1:医生；2：患者
     * @param msgType
     * @param recordId
     * @param patientRecord
     * @param msgReminderTask
     * @return
     */
    private SysMessage getMessageInfo(int userType,int msgType, Long recordId, PatientRecord patientRecord, MsgReminderTask msgReminderTask){
        //判断是否已经记录过提醒
        SysMessage msg = new SysMessage();
        msg.setRecordId(patientRecord.getRecordId());
        msg.setMsgType(msgType);
        msg.setTipType(msgReminderTask.getTipType());
        long count = sysMessageMapper.count(msg);
        if(count>0){
            return null;
        }
        msg.setUserId(patientRecord.getPatientId());
        msg.setUserType(userType);
        msg.setMsgContent(msgReminderTask.getStationMessage());
        msg.setStatus(0);
        msg.setTemplate(msgReminderTask.getTemplate());
        msg.setRedialNum(msgReminderTask.getRedialNum());
        msg.setRedialInterval(msgReminderTask.getRedialInterval());
        msg.setRedialIntervalUnit(msgReminderTask.getRedialIntervalUnit());
        msg.setReminderStartTime(msgReminderTask.getReminderStartTime());
        msg.setReminderEndTime(msgReminderTask.getReminderEndTime());
        return msg;
    }
//    private Map checkParams(MsgReminderTask mrt){
//        Map rtMap= new HashMap();
//        rtMap.put("flag","false");
//        if(mrt.getFollowType()==1 && mrt.getEarlierTime()!=null){
//            rtMap.put("flag","true");
//            rtMap.put("followFlag","true");
//        }
//        if(mrt.getFollowType()==2 && mrt.getUltraWindowTime()!=null){
//            rtMap.put("flag","true");
//            rtMap.put("UltraWindowFlag","true");
//        }
//        return rtMap;
//    }

//    private void handleTipRedDot(Long researchId, Map<Long, List<MsgSend>> msgTipFollowIdMap) {
//        handlePatientSideTipRedDot(researchId, msgTipFollowIdMap);
//    }

//    private void handleDoctorSideTipRedDot(Long researchId) {
//        List<ResearchUserRole> researchUserRoleList = researchUserRoleMapper.findUserRoleByResearchId(researchId);
//        if (CollectionUtils.isEmpty(researchUserRoleList)) {
//            logger.error("通过队列ID查询医生信息失败 researchId:{}", researchId);
//            return;
//        }
//        researchUserRoleList.forEach(rur -> {
//            MsgReminderStats reminderStats = msgReminderStatsMapper.queryByUserId(rur.getUserId());
//            if (Objects.isNull(reminderStats)) {
//                MsgReminderStats msgReminderStats = new MsgReminderStats();
//                msgReminderStats.setUserId(rur.getUserId());
//                msgReminderStats.setUserType(1);
//                msgReminderStats.setFollowReminderCount(1);
//                msgReminderStats.setTotalCount(1);
//                int result = msgReminderStatsMapper.insert(msgReminderStats);
//                if (result < 1) {
//                    logger.error("队列:{}插入随访提醒数量统计数据异常 userType:{}", researchId, 1);
//                }
//            } else {
//                MsgReminderStats msgReminderStats = new MsgReminderStats();
//                msgReminderStats.setFollowReminderCount(reminderStats.getFollowReminderCount() + 1);
//                msgReminderStats.setTotalCount(reminderStats.getTotalCount() + 1);
//                msgReminderStats.setId(reminderStats.getId());
//                int result = msgReminderStatsMapper.update(msgReminderStats);
//                if (result < 1) {
//                    logger.error("队列:{}更新随访提醒数量统计数据异常,userType:{}", researchId, 1);
//                }
//            }
//        });
//    }

//    private void handlePatientSideTipRedDot(Long researchId, Map<Long, List<MsgSend>> msgTipFollowIdMap) {
//        List<ResearchPatient> researchPatientList = researchPatientMapper.selectResearchPatientByResearchId(researchId);
//        if (CollectionUtils.isEmpty(researchPatientList)) {
//            logger.error("通过队列ID查询患者信息失败 researchId:{} userType:{}", researchId, 2);
//            return;
//        }
//        researchPatientList.forEach(rp -> {
//            MsgReminderStats reminderStats = msgReminderStatsMapper.queryByUserId(rp.getBasePatientId());
//            if (Objects.isNull(reminderStats)) {
//                MsgReminderStats msgReminderStats = new MsgReminderStats();
//                msgReminderStats.setUserId(rp.getBasePatientId());
//                msgReminderStats.setUserType(2);
//                msgReminderStats.setFollowReminderCount(1);
//                msgReminderStats.setTotalCount(1);
//                int result = msgReminderStatsMapper.insert(msgReminderStats);
//                if (result < 1) {
//                    logger.error("队列:{}插入随访提醒数量统计数据异常 userType:{}", researchId, 2);
//                }
//            } else {
//                MsgReminderStats msgReminderStats = new MsgReminderStats();
//                msgReminderStats.setFollowReminderCount(reminderStats.getFollowReminderCount() + 1);
//                msgReminderStats.setTotalCount(reminderStats.getTotalCount() + 1);
//                msgReminderStats.setId(reminderStats.getId());
//                int result = msgReminderStatsMapper.update(msgReminderStats);
//                if (result < 1) {
//                    logger.error("队列:{}更新随访提醒数量统计数据异常 userType:{}", researchId, 2);
//                }
//            }
//
//        });
//    }

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("等到各位大佬都到位之后，分发护照和签证等内容！");
        });

        new Thread(() -> {
            System.out.println("Tom到位！！！");
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("悲剧，人没到齐！");
                return;
            }
            System.out.println("Tom出发！！！");
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            System.out.println("Jack到位！！！");
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("悲剧，人没到齐！");
                return;
            }
            System.out.println("Jack出发！！！");
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            System.out.println("Rose到位！！！");
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("悲剧，人没到齐！");
                return;
            }
            System.out.println("Rose出发！！！");
        }).start();
        System.out.println("主线程执行1 = " + "主线程执行1");
        new Thread(() -> {
            System.out.println("1到位！！！");
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("悲剧，人没到齐！");
                return;
            }
            System.out.println("1出发！！！");
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            System.out.println("2到位！！！");
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("悲剧，人没到齐！");
                return;
            }
            System.out.println("2出发！！！");
        }).start();
        Thread.sleep(100);
        new Thread(() -> {
            System.out.println("3到位！！！");
            try {
                barrier.await();
            } catch (Exception e) {
                System.out.println("悲剧，人没到齐！");
                return;
            }
            System.out.println("3出发！！！");
        }).start();

        System.out.println("主线程执行2 = " + "主线程执行2");
    }
}
