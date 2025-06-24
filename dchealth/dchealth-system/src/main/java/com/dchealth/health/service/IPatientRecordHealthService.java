package com.dchealth.health.service;

import com.dchealth.health.domain.PatientRecordHealth;

import java.util.List;

/**
 * 患者档案健康管理Service接口
 * 
 * @author lchbu
 * @date 2025-06-24
 */
public interface IPatientRecordHealthService 
{
    /**
     * 查询患者档案健康管理
     * 
     * @param healthId 患者档案健康管理主键
     * @return 患者档案健康管理
     */
    public PatientRecordHealth selectPatientRecordHealthByHealthId(Long healthId);

    /**
     * 查询患者档案健康管理列表
     * 
     * @param patientRecordHealth 患者档案健康管理
     * @return 患者档案健康管理集合
     */
    public List<PatientRecordHealth> selectPatientRecordHealthList(PatientRecordHealth patientRecordHealth);

    /**
     * 新增患者档案健康管理
     * 
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
    public int insertPatientRecordHealth(PatientRecordHealth patientRecordHealth);

    /**
     * 修改患者档案健康管理
     * 
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
    public int updatePatientRecordHealth(PatientRecordHealth patientRecordHealth);

    /**
     * 批量删除患者档案健康管理
     * 
     * @param healthIds 需要删除的患者档案健康管理主键集合
     * @return 结果
     */
    public int deletePatientRecordHealthByHealthIds(Long[] healthIds);

    /**
     * 删除患者档案健康管理信息
     * 
     * @param healthId 患者档案健康管理主键
     * @return 结果
     */
    public int deletePatientRecordHealthByHealthId(Long healthId);
}
