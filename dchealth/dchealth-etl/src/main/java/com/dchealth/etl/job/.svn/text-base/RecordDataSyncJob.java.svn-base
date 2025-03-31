package com.dchealth.etl.job;



import cn.hutool.core.date.DateUtil;
import com.dchealth.etl.service.RecordDataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zjl
 * @since 2023/4/6
 */
@Component
@Slf4j
public class RecordDataSyncJob {
    @Autowired
    RecordDataSyncService recordDataSyncService;

    public static String formatStrShort = "yyyy-MM-dd";

    /**
     * 同步开关
     * 前期先关闭，后期历史数据同步完成后放开定时任务每天同步
     */
    @Value("${sync.reportdata.syncflag:false}")
    private boolean reportDataSyncFlag ;

    /**
     * 同步病人主索引
     * 每晚0点30执行执行 0 30 0 * * ?
     */
    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncPatMasterIndex() throws Exception{
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncPatMasterIndex("",startDate,endDate,"");
        }
    }

    /**
     * 病人体症记录
     * 每晚0点30执行执行 0 50 0 * * ?
     */
    @Scheduled(cron = "0 50 0 * * ?")
    public void syncVitalSignsRec() throws Exception{
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncVitalSignsRec("",startDate,endDate);
        }
    }

    /**
     * 检查主记录
     */
    @Scheduled(cron = "0 20 1 * * ?")
    public void syncExamMaster() throws Exception{
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDatareportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncExamMaster("",startDate,endDate);
        }
    }

    /**
     * 检验主记录
     */
    @Scheduled(cron = "0 40 1 * * ?")
    public void syncTestMaster() throws Exception{
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncTestMaster("",startDate,endDate);
        }
    }
    /**
     * 检查报告
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void syncExamReport() throws Exception {
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncExamReport("",startDate,endDate);
        }
    }

    /**
     * 检验结果
     */
    @Scheduled(cron = "0 20 2 * * ?")
    public void syncTestResult() throws Exception {
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncTestResult("",startDate,endDate);
        }
    }
    /**
     * 诊断记录
     */
    @Scheduled(cron = "0 40 2 * * ?")
    public void syncDiagnosis() throws Exception {
        log.info("定时任务：syncPatMasterIndex 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncDiagnosis("",startDate,endDate);
        }
    }
    /**
     * 医嘱信息
     */
    @Scheduled(cron = "0 45 2 * * ?")
    public void syncOrders() throws Exception {
        log.info("定时任务：syncOrders 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncOrders("",startDate,endDate);
        }
    }
    /**
     * 药品处方主记录
     */
    @Scheduled(cron = "0 50 2 * * ?")
    public void syncDrugPrescMaster() throws Exception {
        log.info("定时任务：syncDrugPrescMaster 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncDrugPrescMaster("",startDate,endDate);
        }
    }
    /**
     * 药品处方明细
     */
    @Scheduled(cron = "0 55 2 * * ?")
    public void syncDrugPrescDetail() throws Exception {
        log.info("定时任务：syncDrugPrescDetail 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncDrugPrescDetail("",startDate,endDate);
        }
    }

    /**
     * 门诊诊断
     */
    @Scheduled(cron = "0 05 3 * * ?")
    public void syncOutpMedrec() throws Exception {
        log.info("定时任务：syncOutpMedrec 开始同步,reportDataSyncFlag={}",reportDataSyncFlag);
        if(reportDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            recordDataSyncService.syncOutpMedrec("",startDate,endDate);
        }
    }
}
