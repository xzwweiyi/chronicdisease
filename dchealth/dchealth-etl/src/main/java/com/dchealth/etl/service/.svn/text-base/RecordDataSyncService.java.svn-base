package com.dchealth.etl.service;

/**
 * @author zjl
 * @since 2023/3/27
 * 健康档案数据同步
 */
public interface RecordDataSyncService {
    /**
     * 患者信息
     * @param patientId
     * @return
     */
    public String syncPatMasterIndex(String patientId,String startDate,String endDate,String endPatientId);
    /**
     * 病人体症记录
     * @param patientId
     * @param startDate
     * @return
     */
    public String syncVitalSignsRec(String patientId,String startDate,String endDate) throws Exception;

     /**
     * 检查主记录
     * @param examNo
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncExamMaster(String examNo,String startDate,String endDate);

    /**
     * 检验主记录
     * @param testNo
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncTestMaster(String testNo,String startDate,String endDate);

    /**
     * 辅助检查
     * @param examNo
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncExamReport(String examNo,String startDate,String endDate) throws Exception;

    /**
     * 检验结果
     * @param testNo
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncTestResult(String testNo,String startDate,String endDate) throws Exception;
    /**
     * 诊断记录
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncDiagnosis(String patientId,String startDate,String endDate) throws Exception;
    /**
     * 住院主记录
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncPatVisit(String patientId,String startDate,String endDate) throws Exception;
    /** 医嘱信息
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncOrders(String patientId,String startDate,String endDate) throws Exception;
    /** 药品处方信息
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncDrugPrescMaster(String patientId,String startDate,String endDate) throws Exception;
    /** 药品处方信息
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncDrugPrescDetail(String patientId,String startDate,String endDate) throws Exception;
    /**
     * 同步门诊诊断信息
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public String syncOutpMedrec(String patientId,String startDate,String endDate) throws Exception;
}
