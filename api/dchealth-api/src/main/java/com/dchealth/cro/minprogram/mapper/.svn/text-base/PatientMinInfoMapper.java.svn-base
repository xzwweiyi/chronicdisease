package com.dchealth.cro.minprogram.mapper;

import com.dchealth.cro.minprogram.domain.PatientMinInfo;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 患者小程序信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-23
 */
@Repository
public interface PatientMinInfoMapper
{
    /**
     * 查询患者小程序信息
     *
     * @param patientId 患者小程序信息主键
     * @return 患者小程序信息
     */
    public PatientMinInfo selectPatientMinInfoByPatientId(Long patientId);

    /**
     * 根据openId获取患者
     * @param openId
     * @return
     */
    public PatientMinInfo selectPatientMinInfoByOpenId(Long openId);

    /**
     * 查询患者小程序信息列表
     *
     * @param patientMininfo 患者小程序信息
     * @return 患者小程序信息集合
     */
    public List<PatientMinInfo> selectPatientMinInfoList(PatientMinInfo patientMininfo);

    /**
     * 新增患者小程序信息
     *
     * @param patientMininfo 患者小程序信息
     * @return 结果
     */
    public int insertPatientMinInfo(PatientMinInfo patientMininfo);

    /**
     * 修改患者小程序信息
     *
     * @param patientMininfo 患者小程序信息
     * @return 结果
     */
    public int updatePatientMinInfo(PatientMinInfo patientMininfo);

    /**
     * 删除患者小程序信息
     *
     * @param patientId 患者小程序信息主键
     * @return 结果
     */
    public int deletePatientMinInfoByPatientId(Long patientId);

    /**
     * 批量删除患者小程序信息
     *
     * @param patientIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientMinInfoByPatientIds(Long[] patientIds);
}

