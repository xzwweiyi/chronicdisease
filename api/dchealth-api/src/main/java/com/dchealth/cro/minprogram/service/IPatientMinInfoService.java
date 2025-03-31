package com.dchealth.cro.minprogram.service;


import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.minprogram.domain.PatientMinInfo;
import com.dchealth.cro.minprogram.mapper.PatientMinInfoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IPatientMinInfoService {
    @Resource
    private PatientMinInfoMapper patientMinInfoMapper;

    /**
     * 查询患者小程序信息
     *
     * @param patientId 患者小程序信息主键
     * @return 患者小程序信息
     */
    public PatientMinInfo selectPatientMinInfoByPatientId(Long patientId)
    {
        return patientMinInfoMapper.selectPatientMinInfoByPatientId(patientId);
    }

    /**
     * 查询患者小程序信息列表
     *
     * @param patientMininfo 患者小程序信息
     * @return 患者小程序信息
     */
    public List<PatientMinInfo> selectPatientMinInfoList(PatientMinInfo patientMinInfo)
    {
        return patientMinInfoMapper.selectPatientMinInfoList(patientMinInfo);
    }

    /**
     * 新增患者小程序信息
     *
     * @param patientMinInfo 患者小程序信息
     * @return 结果
     */
    public int insertPatientMinInfo(PatientMinInfo patientMinInfo)
    {
        patientMinInfo.setCreateTime(DateUtils.getNowDate());
        return patientMinInfoMapper.insertPatientMinInfo(patientMinInfo);
    }

    /**
     * 修改患者小程序信息
     *
     * @param patientMinInfo 患者小程序信息
     * @return 结果
     */
    public int updatePatientMinInfo(PatientMinInfo patientMinInfo)
    {
        patientMinInfo.setUpdateTime(DateUtils.getNowDate());
        return patientMinInfoMapper.updatePatientMinInfo(patientMinInfo);
    }

    /**
     * 批量删除患者小程序信息
     *
     * @param patientIds 需要删除的患者小程序信息主键
     * @return 结果
     */
    public int deletePatientMinInfoByPatientIds(Long[] patientIds)
    {
        return patientMinInfoMapper.deletePatientMinInfoByPatientIds(patientIds);
    }

    /**
     * 删除患者小程序信息信息
     *
     * @param patientId 患者小程序信息主键
     * @return 结果
     */
    public int deletePatientMinInfoByPatientId(Long patientId)
    {
        return patientMinInfoMapper.deletePatientMinInfoByPatientId(patientId);
    }
}
