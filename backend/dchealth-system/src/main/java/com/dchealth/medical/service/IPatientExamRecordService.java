package com.dchealth.medical.service;

import java.util.List;
import com.dchealth.medical.domain.PatientExamRecord;

/**
 * 患者问卷填报关系Service接口
 * 
 * @author lchbu
 * @date 2022-11-17
 */
public interface IPatientExamRecordService 
{
    /**
     * 查询患者问卷填报关系
     * 
     * @param id 患者问卷填报关系主键
     * @return 患者问卷填报关系
     */
    public PatientExamRecord selectPatientExamRecordById(Long id);

    /**
     * 查询患者问卷填报关系列表
     * 
     * @param patientExamRecord 患者问卷填报关系
     * @return 患者问卷填报关系集合
     */
    public List<PatientExamRecord> selectPatientExamRecordList(PatientExamRecord patientExamRecord);

    /**
     * 新增患者问卷填报关系
     * 
     * @param patientExamRecord 患者问卷填报关系
     * @return 结果
     */
    public int insertPatientExamRecord(PatientExamRecord patientExamRecord);

    /**
     * 修改患者问卷填报关系
     * 
     * @param patientExamRecord 患者问卷填报关系
     * @return 结果
     */
    public int updatePatientExamRecord(PatientExamRecord patientExamRecord);

    /**
     * 批量删除患者问卷填报关系
     * 
     * @param ids 需要删除的患者问卷填报关系主键集合
     * @return 结果
     */
    public int deletePatientExamRecordByIds(Long[] ids);

    /**
     * 删除患者问卷填报关系信息
     * 
     * @param id 患者问卷填报关系主键
     * @return 结果
     */
    public int deletePatientExamRecordById(Long id);
}
