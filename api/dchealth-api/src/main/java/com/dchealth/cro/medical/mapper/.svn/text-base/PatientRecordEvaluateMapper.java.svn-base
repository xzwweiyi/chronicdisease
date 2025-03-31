package com.dchealth.cro.medical.mapper;


import com.dchealth.cro.medical.domain.PatientRecordEvaluate;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 质效评价Mapper接口
 * 
 * @author lchbu
 * @date 2023-02-15
 */
@Repository
public interface PatientRecordEvaluateMapper 
{
    /**
     * 查询质效评价
     * 
     * @param recordId 质效评价主键
     * @return 质效评价
     */
    public PatientRecordEvaluate selectPatientRecordEvaluateByRecordId(Long recordId);

    /**
     * 查询质效评价列表
     * 
     * @param patientRecordEvaluate 质效评价
     * @return 质效评价集合
     */
    public List<PatientRecordEvaluate> selectPatientRecordEvaluateList(PatientRecordEvaluate patientRecordEvaluate);

    /**
     * 新增质效评价
     * 
     * @param patientRecordEvaluate 质效评价
     * @return 结果
     */
    public int insertPatientRecordEvaluate(PatientRecordEvaluate patientRecordEvaluate);

    /**
     * 修改质效评价
     * 
     * @param patientRecordEvaluate 质效评价
     * @return 结果
     */
    public int updatePatientRecordEvaluate(PatientRecordEvaluate patientRecordEvaluate);

    /**
     * 删除质效评价
     * 
     * @param recordId 质效评价主键
     * @return 结果
     */
    public int deletePatientRecordEvaluateByRecordId(Long recordId);

    /**
     * 批量删除质效评价
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordEvaluateByRecordIds(Long[] recordIds);
}
