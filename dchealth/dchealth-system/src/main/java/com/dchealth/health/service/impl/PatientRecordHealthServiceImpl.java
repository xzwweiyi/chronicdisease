package com.dchealth.health.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.health.domain.PatientRecordHealth;
import com.dchealth.health.mapper.PatientRecordHealthMapper;
import com.dchealth.health.service.IPatientRecordHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 患者档案健康管理Service业务层处理
 * 
 * @author lchbu
 * @date 2025-06-24
 */
@Service
public class PatientRecordHealthServiceImpl implements IPatientRecordHealthService
{
    @Autowired
    private PatientRecordHealthMapper patientRecordHealthMapper;

    /**
     * 查询患者档案健康管理
     * 
     * @param healthId 患者档案健康管理主键
     * @return 患者档案健康管理
     */
    @Override
    public PatientRecordHealth selectPatientRecordHealthByHealthId(Long healthId)
    {
        return patientRecordHealthMapper.selectPatientRecordHealthByHealthId(healthId);
    }

    /**
     * 查询患者档案健康管理列表
     * 
     * @param patientRecordHealth 患者档案健康管理
     * @return 患者档案健康管理
     */
    @Override
    public List<PatientRecordHealth> selectPatientRecordHealthList(PatientRecordHealth patientRecordHealth)
    {
        return patientRecordHealthMapper.selectPatientRecordHealthList(patientRecordHealth);
    }

    /**
     * 新增患者档案健康管理
     * 
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
    @Override
    public int insertPatientRecordHealth(PatientRecordHealth patientRecordHealth)
    {
        patientRecordHealth.setCreateTime(DateUtils.getNowDate());
        return patientRecordHealthMapper.insertPatientRecordHealth(patientRecordHealth);
    }

    /**
     * 修改患者档案健康管理
     * 
     * @param patientRecordHealth 患者档案健康管理
     * @return 结果
     */
    @Override
    public int updatePatientRecordHealth(PatientRecordHealth patientRecordHealth)
    {
        patientRecordHealth.setUpdateTime(DateUtils.getNowDate());
        return patientRecordHealthMapper.updatePatientRecordHealth(patientRecordHealth);
    }

    /**
     * 批量删除患者档案健康管理
     * 
     * @param healthIds 需要删除的患者档案健康管理主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordHealthByHealthIds(Long[] healthIds)
    {
        return patientRecordHealthMapper.deletePatientRecordHealthByHealthIds(healthIds);
    }

    /**
     * 删除患者档案健康管理信息
     * 
     * @param healthId 患者档案健康管理主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordHealthByHealthId(Long healthId)
    {
        return patientRecordHealthMapper.deletePatientRecordHealthByHealthId(healthId);
    }
}
