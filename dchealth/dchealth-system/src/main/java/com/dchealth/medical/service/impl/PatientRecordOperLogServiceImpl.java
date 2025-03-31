package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.medical.mapper.PatientRecordOperLogMapper;
import com.dchealth.medical.domain.PatientRecordOperLog;
import com.dchealth.medical.service.IPatientRecordOperLogService;

/**
 * 用户统计操作记录Service业务层处理
 * 
 * @author lchbu
 * @date 2023-04-14
 */
@Service
public class PatientRecordOperLogServiceImpl implements IPatientRecordOperLogService{
    @Autowired
    private PatientRecordOperLogMapper patientRecordOperLogMapper;

    /**
     * 查询用户统计操作记录
     * 
     * @param id 用户统计操作记录主键
     * @return 用户统计操作记录
     */
    @Override
    public PatientRecordOperLog selectPatientRecordOperLogById(Long id)
    {
        return patientRecordOperLogMapper.selectPatientRecordOperLogById(id);
    }

    /**
     * 查询用户统计操作记录列表
     * 
     * @param patientRecordOperLog 用户统计操作记录
     * @return 用户统计操作记录
     */
    @Override
    public List<PatientRecordOperLog> selectPatientRecordOperLogList(PatientRecordOperLog patientRecordOperLog)
    {
        return patientRecordOperLogMapper.selectPatientRecordOperLogList(patientRecordOperLog);
    }

    /**
     * 新增用户统计操作记录
     * 
     * @param patientRecordOperLog 用户统计操作记录
     * @return 结果
     */
    @Override
    public int insertPatientRecordOperLog(PatientRecordOperLog patientRecordOperLog)
    {
        patientRecordOperLog.setCreateTime(DateUtils.getNowDate());
        return patientRecordOperLogMapper.insertPatientRecordOperLog(patientRecordOperLog);
    }

    /**
     * 修改用户统计操作记录
     * 
     * @param patientRecordOperLog 用户统计操作记录
     * @return 结果
     */
    @Override
    public int updatePatientRecordOperLog(PatientRecordOperLog patientRecordOperLog)
    {
        return patientRecordOperLogMapper.updatePatientRecordOperLog(patientRecordOperLog);
    }

    /**
     * 批量删除用户统计操作记录
     * 
     * @param ids 需要删除的用户统计操作记录主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordOperLogByIds(Long[] ids)
    {
        return patientRecordOperLogMapper.deletePatientRecordOperLogByIds(ids);
    }

    /**
     * 删除用户统计操作记录信息
     * 
     * @param id 用户统计操作记录主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordOperLogById(Long id)
    {
        return patientRecordOperLogMapper.deletePatientRecordOperLogById(id);
    }
}
