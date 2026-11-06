package com.dchealth.medical.service;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.compatibility.domain.MpDrugExposure;
import com.dchealth.medical.domain.PatientDrugInfo;

import java.util.List;

/**
 * 用药信息Service接口
 * 
 * @author wangpf
 * @date 2025-08-06
 */
public interface IPatientDrugInfoService 
{
    /**
     * 查询用药信息
     * 
     * @param id 用药信息主键
     * @return 用药信息
     */
    public PatientDrugInfo selectPatientDrugInfoById(Long id);

    /**
     * 查询用药信息列表
     * 
     * @param patientDrugInfo 用药信息
     * @return 用药信息集合
     */
    public AjaxResult selectPatientDrugInfoList(PatientDrugInfo patientDrugInfo);

    /**
     * 新增用药信息
     * 
     * @param patientDrugInfo 用药信息
     * @return 结果
     */
    public int insertPatientDrugInfo(PatientDrugInfo patientDrugInfo);

    /**
     * 修改用药信息
     * 
     * @param patientDrugInfo 用药信息
     * @return 结果
     */
    public int updatePatientDrugInfo(PatientDrugInfo patientDrugInfo);

    /**
     * 批量删除用药信息
     * 
     * @param ids 需要删除的用药信息主键集合
     * @return 结果
     */
    public int deletePatientDrugInfoByIds(Long[] ids);

    /**
     * 删除用药信息信息
     * 
     * @param id 用药信息主键
     * @return 结果
     */
    public int deletePatientDrugInfoById(Long id);

    List<PatientDrugInfo> getPatientDrugInfoList(PatientDrugInfo patientDrugInfo);

    AjaxResult mpDrugExposureList(MpDrugExposure mpDrugExposure);
}
