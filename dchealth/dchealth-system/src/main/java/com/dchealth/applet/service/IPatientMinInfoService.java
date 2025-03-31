package com.dchealth.applet.service;

import com.dchealth.applet.domain.PatientMinInfo;

import java.util.List;

public interface IPatientMinInfoService {
    /**
     * 查询患者小程序信息
     *
     * @param patientId 患者小程序信息主键
     * @return 患者小程序信息
     */
    public PatientMinInfo selectPatientMininfoByPatientId(Long patientId);

    /**
     * 查询患者小程序信息列表
     *
     * @param patientMininfo 患者小程序信息
     * @return 患者小程序信息集合
     */
    public List<PatientMinInfo> selectPatientMininfoList(PatientMinInfo patientMininfo);

    /**
     * 新增患者小程序信息
     *
     * @param patientMininfo 患者小程序信息
     * @return 结果
     */
    public int insertPatientMininfo(PatientMinInfo patientMininfo);

    /**
     * 修改患者小程序信息
     *
     * @param patientMininfo 患者小程序信息
     * @return 结果
     */
    public int updatePatientMininfo(PatientMinInfo patientMininfo);

    /**
     * 批量删除患者小程序信息
     *
     * @param patientIds 需要删除的患者小程序信息主键集合
     * @return 结果
     */
    public int deletePatientMininfoByPatientIds(Long[] patientIds);

    /**
     * 删除患者小程序信息信息
     *
     * @param patientId 患者小程序信息主键
     * @return 结果
     */
    public int deletePatientMininfoByPatientId(Long patientId);
}
