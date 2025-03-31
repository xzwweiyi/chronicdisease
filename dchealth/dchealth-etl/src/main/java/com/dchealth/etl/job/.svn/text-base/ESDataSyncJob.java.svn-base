package com.dchealth.etl.job;

import cn.hutool.core.date.DateUtil;
import com.dchealth.etl.service.SyncRecordDataESService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zjl
 * @since 2023/4/12
 */
@Component
@Slf4j
public class ESDataSyncJob {

    @Autowired
    SyncRecordDataESService SyncRecordDataESService;


    /**
     * 同步开关
     * 前期先关闭，后期历史数据同步完成后放开定时任务每天同步
     */
    @Value("${sync.es.syncflag:false}")
    public static boolean ESDataSyncFlag = false;

    public static String formatStrShort = "yyyy-MM-dd";

    /**
     * 同步患者信息
     * 0 30 0 * * ?
     */
//    @Scheduled(cron = "0 0/10 * * * ?")
    public void syncPatientBaseInfos() throws Exception{
        log.info("定时任务：syncPatientBaseInfos 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            SyncRecordDataESService.syncPatientBaseInfos("");
        }
    }
    /**
     * 同步诊断记录
     * 0 40 2 * * ?
     */
//    @Scheduled(cron = "0 0/10 * * * ?")
    public void syncTdDiagnosiss() throws Exception{
        log.info("定时任务：syncTdDiagnosiss 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            SyncRecordDataESService.syncTdDiagnosiss("");
        }
    }

    /**
     * 同步患者信息诊断信息
     */
    @Scheduled(cron = "0 30 3 * * ?")
    public void syncTdDiagnosisInfos() throws Exception{
        log.info("定时任务：syncTdDiagnosisInfos 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            SyncRecordDataESService.syncTdDiagnosisInfos("",startDate,endDate);
        }
    }
    /**
     * 同步患者体征记录（一般检查）
     * @return
     */
    @Scheduled(cron = "0 45 3 * * ?")
    public void syncTdGeneralInspectionInfos() throws Exception{
        log.info("定时任务：syncTdGeneralInspectionInfos 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            SyncRecordDataESService.syncTdGeneralInspectionInfos("",startDate,endDate);
        }
    }
    /**
     * 同步患者辅助检查信息
     * @return
     */
//    @Scheduled(cron = "0 0 4 * * ?")
    public void syncTdExamMaster() throws Exception{
        log.info("定时任务：syncTdExamMaster 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            SyncRecordDataESService.syncTdExamMaster("",startDate,endDate);
        }
    }
    /**
     * 同步患者检验信息
     * @return
     */
    @Scheduled(cron = "0 15 4 * * ?")
    public void syncTdTestMaster() throws Exception{
        log.info("定时任务：syncTdTestMaster 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            SyncRecordDataESService.syncTdTestMaster("",startDate,endDate);
        }
    }
    /**
     * 同步患者处方信息
     * @return
     */
    @Scheduled(cron = "0 30 4 * * ?")
    public void syncDrugPresc() throws Exception{
        log.info("定时任务：syncDrugPresc 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            SyncRecordDataESService.syncDrugPresc("",startDate,endDate);
        }
    }
    /**
     * 同步患者医嘱信息
     * @return
     */
    @Scheduled(cron = "0 45 4 * * ?")
    public void syncTdOrders() throws Exception{
        log.info("定时任务：syncTdOrders 开始同步,ESDataSyncFlag={}",ESDataSyncFlag);
        if(ESDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            SyncRecordDataESService.syncTdOrders("",startDate,endDate);
        }
    }



}
