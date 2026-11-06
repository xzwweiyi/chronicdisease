package com.dchealth.etl.job;

import cn.hutool.core.date.DateUtil;
import com.dchealth.etl.service.CheckDataSyncService;
import com.dchealth.etl.service.CheckDataSyncViewService;
import com.dchealth.etl.service.RecordDataSyncService;
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
public class CheckDataSyncJob {

    @Autowired
    CheckDataSyncViewService checkDataSyncService;

    public static String formatStrShort = "yyyy-MM-dd";

    /**
     * 同步开关
     * 前期先关闭，后期历史数据同步完成后放开定时任务每天同步
     */
    @Value("${sync.checkdata.syncflag:false}")
    public static boolean checkDataSyncFlag = false;

    /**
     * 同步检查信息表
     * 每晚0点30执行执行 0 55 0 * * ?
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncJcxx() throws Exception{
        log.info("定时任务：syncJcxx 开始同步,reportDataSyncFlag={}",checkDataSyncFlag);
        if(checkDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            checkDataSyncService.syncJcxx("",startDate,endDate,"");
        }
    }
    /**
     * 同步检查表
     * 每晚0点30执行执行 0 10 1 * * ?
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncJcb() throws Exception{
        log.info("定时任务：syncJcb 开始同步,reportDataSyncFlag={}",checkDataSyncFlag);
        if(checkDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            checkDataSyncService.syncJcb("",startDate,endDate);
        }
    }
    /**
     * 同步化验表
     * 每晚0点30执行执行 0 30 1 * * ?
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncHyb() throws Exception{
        log.info("定时任务：syncHyb 开始同步,reportDataSyncFlag={}",checkDataSyncFlag);
        if(checkDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            checkDataSyncService.syncHyb("",startDate,endDate);
        }
    }
    /**
     * 同步结论表
     * 每晚0点30执行执行 0 55 1 * * ?
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncJlb() throws Exception{
        log.info("定时任务：syncJlb 开始同步,reportDataSyncFlag={}",checkDataSyncFlag);
        if(checkDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            checkDataSyncService.syncJlb("",startDate,endDate);
        }
    }

    /**
     * 同步结论表
     * 每晚0点30执行执行 0 55 1 * * ?
     */
//    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncPacs() throws Exception{
        log.info("定时任务：syncPacs 开始同步,reportDataSyncFlag={}",checkDataSyncFlag);
        if(checkDataSyncFlag){
            String startDate = DateUtil.format(DateUtil.yesterday(),formatStrShort);
            String endDate = DateUtil.today();
            checkDataSyncService.syncPacs("",startDate,endDate);
        }
    }

}
