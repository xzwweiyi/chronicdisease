package com.dchealth.medical.mapper;

import java.util.List;
import com.dchealth.medical.domain.PatientRecordOperLog;

/**
 * 用户统计操作记录Mapper接口
 * 
 * @author lchbu
 * @date 2023-04-14
 */
public interface PatientRecordOperLogMapper{
    /**
     * 查询用户统计操作记录
     * 
     * @param id 用户统计操作记录主键
     * @return 用户统计操作记录
     */
    public PatientRecordOperLog selectPatientRecordOperLogById(Long id);

    /**
     * 查询用户统计操作记录列表
     * 
     * @param patientRecordOperLog 用户统计操作记录
     * @return 用户统计操作记录集合
     */
    public List<PatientRecordOperLog> selectPatientRecordOperLogList(PatientRecordOperLog patientRecordOperLog);

    /**
     * 新增用户统计操作记录
     * 
     * @param patientRecordOperLog 用户统计操作记录
     * @return 结果
     */
    public int insertPatientRecordOperLog(PatientRecordOperLog patientRecordOperLog);

    /**
     * 修改用户统计操作记录
     * 
     * @param patientRecordOperLog 用户统计操作记录
     * @return 结果
     */
    public int updatePatientRecordOperLog(PatientRecordOperLog patientRecordOperLog);

    /**
     * 删除用户统计操作记录
     * 
     * @param id 用户统计操作记录主键
     * @return 结果
     */
    public int deletePatientRecordOperLogById(Long id);

    /**
     * 批量删除用户统计操作记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordOperLogByIds(Long[] ids);
}
