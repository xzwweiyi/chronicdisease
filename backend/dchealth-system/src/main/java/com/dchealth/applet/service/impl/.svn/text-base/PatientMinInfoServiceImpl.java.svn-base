package com.dchealth.applet.service.impl;

import com.dchealth.applet.domain.PatientMinInfo;
import com.dchealth.applet.mapper.PatientMinInfoMapper;
import com.dchealth.applet.service.IPatientMinInfoService;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientMinInfoServiceImpl implements IPatientMinInfoService {
    @Autowired
    private PatientMinInfoMapper patientMininfoMapper;

    /**
     * 查询患者小程序信息
     *
     * @param patientId 患者小程序信息主键
     * @return 患者小程序信息
     */
    @Override
    public PatientMinInfo selectPatientMininfoByPatientId(Long patientId)
    {
        return patientMininfoMapper.selectPatientMininfoByPatientId(patientId);
    }

    /**
     * 查询患者小程序信息列表
     *
     * @param patientMininfo 患者小程序信息
     * @return 患者小程序信息
     */
    @Override
    public List<PatientMinInfo> selectPatientMininfoList(PatientMinInfo patientMininfo)
    {
        return patientMininfoMapper.selectPatientMininfoList(patientMininfo);
    }

    /**
     * 新增患者小程序信息
     *
     * @param patientMininfo 患者小程序信息
     * @return 结果
     */
    @Override
    public int insertPatientMininfo(PatientMinInfo patientMininfo)
    {
        patientMininfo.setCreateTime(DateUtils.getNowDate());
        return patientMininfoMapper.insertPatientMininfo(patientMininfo);
    }

    /**
     * 修改患者小程序信息
     *
     * @param patientMininfo 患者小程序信息
     * @return 结果
     */
    @Override
    public int updatePatientMininfo(PatientMinInfo patientMininfo)
    {
        patientMininfo.setUpdateTime(DateUtils.getNowDate());
        return patientMininfoMapper.updatePatientMininfo(patientMininfo);
    }

    /**
     * 批量删除患者小程序信息
     *
     * @param patientIds 需要删除的患者小程序信息主键
     * @return 结果
     */
    @Override
    public int deletePatientMininfoByPatientIds(Long[] patientIds)
    {
        return patientMininfoMapper.deletePatientMininfoByPatientIds(patientIds);
    }

    /**
     * 删除患者小程序信息信息
     *
     * @param patientId 患者小程序信息主键
     * @return 结果
     */
    @Override
    public int deletePatientMininfoByPatientId(Long patientId)
    {
        return patientMininfoMapper.deletePatientMininfoByPatientId(patientId);
    }
}
