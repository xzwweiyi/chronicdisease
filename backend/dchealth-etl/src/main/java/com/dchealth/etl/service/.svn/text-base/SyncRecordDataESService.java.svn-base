package com.dchealth.etl.service;

import java.io.IOException;

/**
 * @author zjl
 * @since 2023/4/18
 */
public interface SyncRecordDataESService<T> {

    /**
     * 同步患者信息
     * @param id
     * @return
     */
    int syncPatientBaseInfos(String id) throws IllegalAccessException, NoSuchFieldException, IOException;
    /**
     * 同步诊断记录
     * @param id
     * @return
     */
    int syncTdDiagnosiss(String id) throws IllegalAccessException, NoSuchFieldException, IOException;
    /**
     * 同步患者信息诊断信息
     * @param id
     * @return
     */
    public int syncTdDiagnosisInfos(String id,String startDate,String endDate) throws Exception;
    /**
     * 同步患者体征记录（一般检查）
     * @param id
     * @return
     */
    public int syncTdGeneralInspectionInfos(String id,String startDate,String endDate) throws Exception;
    /**
     * 同步患者辅助检查信息
     * @param id
     * @return
     */
    public int syncTdExamMaster(String id,String startDate,String endDate) throws Exception;
    /**
     * 同步患者检验信息
     * @param id
     * @return
     */
    public int syncTdTestMaster(String id,String startDate,String endDate) throws Exception;
    /**
     * 同步患者处方信息
     * @param id
     * @return
     */
    public int syncDrugPresc(String id,String startDate,String endDate) throws Exception;
    /**
     * 同步患者医嘱信息
     * @param id
     * @return
     */
    public int syncTdOrders(String id,String startDate,String endDate) throws Exception;
}
