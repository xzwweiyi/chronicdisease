package com.dchealth.etl.controller;

import com.dchealth.etl.service.RecordDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjl
 * @since 2023/3/27
 * 健康档案同步
 */
@RestController
@RequestMapping("/record")
public class RecordDataSyncController {

    @Autowired
    RecordDataSyncService recordDataSyncService;

    /**
     * 患者信息
     * @param patientId
     * @return
     */
    @GetMapping("/syncPatMasterIndex")
    public String syncPatMasterIndex(String patientId,String startDate,String endDate,String endPatientId){
        return recordDataSyncService.syncPatMasterIndex(patientId,startDate,endDate,endPatientId);
    }

    /**
     * 病人体症记录
     * @param patientId
     * @param startDate
     * @return
     */
    @GetMapping("/syncVitalSignsRec")
    public String syncVitalSignsRec(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncVitalSignsRec(patientId,startDate,endDate);
    }
    /**
     * 检查主记录
     * @param examNo
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncExamMaster")
    public String syncExamMaster(String examNo,String startDate,String endDate){
        return recordDataSyncService.syncExamMaster(examNo,startDate,endDate);
    }
    /**
     * 检验主记录
     * @param testNo
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncTestMaster")
    public String syncTestMaster(String testNo,String startDate,String endDate){
        return recordDataSyncService.syncTestMaster(testNo,startDate,endDate);
    }
    /**
     * 检查报告
     * @param examNo
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncExamReport")
    public String syncExamReport(String examNo,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncExamReport(examNo,startDate,endDate);
    }

    /**
     * 检验结果
     * @param testNo
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncTestResult")
    public String syncTestResult(String testNo,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncTestResult(testNo,startDate,endDate);
    }
    /**
     * 诊断记录
     * @param patientId
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncDiagnosis")
    public String syncDiagnosis(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncDiagnosis(patientId,startDate,endDate);
    }

    /**
     * 更新患者信息的入院出院时间
     * @param patientId
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncPatVisit")
    public String syncPatVisit(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncPatVisit(patientId,startDate,endDate);
    }
    /**
     * 同步医嘱信息
     * @param patientId
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncOrders")
    public String syncOrders(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncOrders(patientId,startDate,endDate);
    }
    /**
     * 同步药品处方主记录
     * @param patientId
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncDrugPrescMaster")
    public String syncDrugPrescMaster(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncDrugPrescMaster(patientId,startDate,endDate);
    }
    /**
     * 同步药品处方明细
     * @param patientId
     * @param startDate yyyy-MM-dd
     * @param endDate   yyyy-MM-dd
     * @return
     */
    @GetMapping("/syncDrugPrescDetail")
    public String syncDrugPrescDetail(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncDrugPrescDetail(patientId,startDate,endDate);
    }
    /**
     * 同步门诊诊断信息
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @GetMapping("/syncOutpMedrec")
    public String syncOutpMedrec(String patientId,String startDate,String endDate) throws Exception {
        return recordDataSyncService.syncOutpMedrec(patientId,startDate,endDate);
    }
}
