package com.dchealth.medical.service.impl;

import com.dchealth.medical.domain.PatientDrugInfo;
import com.dchealth.medical.mapper.PatientDrugInfoMapper;
import com.dchealth.medical.service.IPatientDrugInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用药信息Service业务层处理
 * 
 * @author wangpf
 * @date 2025-08-06
 */
@Service
public class PatientDrugInfoServiceImpl implements IPatientDrugInfoService
{
    @Autowired
    private PatientDrugInfoMapper patientDrugInfoMapper;

    /**
     * 查询用药信息
     * 
     * @param id 用药信息主键
     * @return 用药信息
     */
    @Override
    public PatientDrugInfo selectPatientDrugInfoById(Long id)
    {
        return patientDrugInfoMapper.selectPatientDrugInfoById(id);
    }

    /**
     * 查询用药信息列表
     * 
     * @param patientDrugInfo 用药信息
     * @return 用药信息
     */
    @Override
    public List<PatientDrugInfo> selectPatientDrugInfoList(PatientDrugInfo patientDrugInfo)
    {
        return patientDrugInfoMapper.selectPatientDrugInfoList(patientDrugInfo);
    }

    /**
     * 新增用药信息
     * 
     * @param patientDrugInfo 用药信息
     * @return 结果
     */
    @Override
    public int insertPatientDrugInfo(PatientDrugInfo patientDrugInfo)
    {
        return patientDrugInfoMapper.insertPatientDrugInfo(patientDrugInfo);
    }

    /**
     * 修改用药信息
     * 
     * @param patientDrugInfo 用药信息
     * @return 结果
     */
    @Override
    public int updatePatientDrugInfo(PatientDrugInfo patientDrugInfo)
    {
        return patientDrugInfoMapper.updatePatientDrugInfo(patientDrugInfo);
    }

    /**
     * 批量删除用药信息
     * 
     * @param ids 需要删除的用药信息主键
     * @return 结果
     */
    @Override
    public int deletePatientDrugInfoByIds(Long[] ids)
    {
        return patientDrugInfoMapper.deletePatientDrugInfoByIds(ids);
    }

    /**
     * 删除用药信息信息
     * 
     * @param id 用药信息主键
     * @return 结果
     */
    @Override
    public int deletePatientDrugInfoById(Long id)
    {
        return patientDrugInfoMapper.deletePatientDrugInfoById(id);
    }
}
