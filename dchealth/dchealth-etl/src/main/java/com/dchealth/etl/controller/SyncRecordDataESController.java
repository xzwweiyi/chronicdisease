package com.dchealth.etl.controller;

import com.dchealth.etl.service.SyncRecordDataESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author caihe
 * @since 2023/4/18
 */
@RestController
@RequestMapping("/es")
public class SyncRecordDataESController {


    @Autowired
    SyncRecordDataESService SyncRecordDataESService;

    /**
     * 同步患者信息
     * @param id
     * @return
     */
    @GetMapping("/syncPatientBaseInfos")
    public int syncPatientBaseInfos(String id) throws IllegalAccessException, NoSuchFieldException, IOException {
        return  SyncRecordDataESService.syncPatientBaseInfos(id);
    }

    /**
     * 同步患者信息
     * @param id
     * @return
     */
    @GetMapping("/syncTdDiagnosiss")
    public int syncTdDiagnosiss(String id) throws IllegalAccessException, NoSuchFieldException, IOException {
        return  SyncRecordDataESService.syncTdDiagnosiss(id);
    }

    /**
     * 同步患者信息诊断信息
     * @param id
     * @return
     */
    @GetMapping("/syncTdDiagnosisInfos")
    public int syncTdDiagnosisInfos(String id,String startDate,String endDate) throws Exception {
        return  SyncRecordDataESService.syncTdDiagnosisInfos(id,startDate,endDate);
    }
    /**
     * 同步患者体征记录（一般检查）
     * @param id
     * @return
     */
    @GetMapping("/syncTdGeneralInspectionInfos")
    public int syncTdGeneralInspectionInfos(String id,String startDate,String endDate) throws Exception {
        return  SyncRecordDataESService.syncTdGeneralInspectionInfos(id,startDate,endDate);
    }

    /**
     * 同步患者辅助检查信息
     * @param id
     * @return
     */
    @GetMapping("/syncTdExamMaster")
    public int syncTdExamMaster(String id,String startDate,String endDate) throws Exception {
        return  SyncRecordDataESService.syncTdExamMaster(id,startDate,endDate);
    }
    /**
     * 同步患者检验信息
     * @param id
     * @return
     */
    @GetMapping("/syncTdTestMaster")
    public int syncTdTestMaster(String id,String startDate,String endDate) throws Exception {
        return  SyncRecordDataESService.syncTdTestMaster(id,startDate,endDate);
    }
    /**
     * 同步患者处方信息
     * @param id
     * @return
     */
    @GetMapping("/syncDrugPresc")
    public int syncDrugPresc(String id,String startDate,String endDate) throws Exception {
        return  SyncRecordDataESService.syncDrugPresc(id,startDate,endDate);
    }
    /**
     * 同步患者医嘱信息
     * @param id
     * @return
     */
    @GetMapping("/syncTdOrders")
    public int syncTdOrders(String id,String startDate,String endDate) throws Exception {
        return  SyncRecordDataESService.syncTdOrders(id,startDate,endDate);
    }


}
