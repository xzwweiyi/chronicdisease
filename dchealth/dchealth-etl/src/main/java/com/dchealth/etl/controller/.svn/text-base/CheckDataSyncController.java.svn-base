package com.dchealth.etl.controller;

import com.dchealth.etl.service.CheckDataSyncService;
import com.dchealth.etl.service.CheckDataSyncViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjl
 * @since 2023/3/27
 * 体检档案同步
 */
@RestController
@RequestMapping("/check")
public class CheckDataSyncController {

    @Autowired
    CheckDataSyncViewService checkDataSyncService;

    /**
     * 同步检查信息
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/syncJcxx")
    public String syncJcxx(String studyId, String startDate, String endDate,String endStudyId) throws Exception {
        return checkDataSyncService.syncJcxx(studyId,startDate,endDate,endStudyId);
    }


    /**
     * 同步检查表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/syncJcb")
    public String syncJcb(String studyId, String startDate, String endDate) throws Exception {
        return checkDataSyncService.syncJcb(studyId,startDate,endDate);
    }

    /**
     * 同步化验表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/syncHyb")
    public String syncHyb(String studyId, String startDate, String endDate) throws Exception {
        return checkDataSyncService.syncHyb(studyId,startDate,endDate);
    }

    /**
     * 同步结论表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/syncJlb")
    public String syncJlb(String studyId, String startDate, String endDate) throws Exception {
        return checkDataSyncService.syncJlb(studyId,startDate,endDate);
    }

    /**
     * 同步结论表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/syncPacs")
    public String syncPacs(String studyId, String startDate, String endDate) throws Exception {
        return checkDataSyncService.syncPacs(studyId,startDate,endDate);
    }

    /**
     * 同步结论表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    @GetMapping("/syncJlbByThred")
    public String syncJlbByThred(String studyId, String startDate, String endDate) throws Exception {
        return checkDataSyncService.syncJlbByThred(studyId,startDate,endDate);
    }


}
