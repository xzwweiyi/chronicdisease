package com.dchealth.etl.service;

/**
 * @author zjl
 * @since 2023/3/27
 * 健康档案数据同步  已废弃，改为从视图中获取
 */
public interface CheckDataSyncService {

    /**
     * 检查信息表
     * @param studyId
     * @return
     */
    String syncJcxx(String studyId,String startDate,String endDate,String endStudyId) throws Exception;

    /**
     * 检查表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    String syncJcb(String studyId, String startDate, String endDate) throws Exception;

    /**
     * 化验表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncHyb(String studyId, String startDate, String endDate) throws Exception;

    /**
     * 结论表
     * @param studyId
     * @param startDate
     * @param endDate
     * @return
     */
    public String syncJlb(String studyId, String startDate, String endDate) throws Exception;

    public String syncJlbByThred(String studyId, String startDate, String endDate) throws Exception;
}
