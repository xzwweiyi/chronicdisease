package com.dchealth.medical.service;

import java.util.List;
import com.dchealth.medical.domain.PatientRecordTemplate;

/**
 * 周期配置Service接口
 * 
 * @author lchbu
 * @date 2022-08-23
 */
public interface IPatientRecordTemplateService 
{
    /**
     * 查询周期配置
     * 
     * @param followId 周期配置主键
     * @return 周期配置
     */
    public PatientRecordTemplate selectPatientRecordTemplateByFollowId(Long followId);

    /**
     * 查询周期配置列表
     * 
     * @param patientRecordTemplate 周期配置
     * @return 周期配置集合
     */
    public List<PatientRecordTemplate> selectPatientRecordTemplateList(PatientRecordTemplate patientRecordTemplate);

    /**
     * 新增周期配置
     * 
     * @param patientRecordTemplate 周期配置
     * @return 结果
     */
    public int insertPatientRecordTemplate(PatientRecordTemplate patientRecordTemplate);

    /**
     * 修改周期配置
     * 
     * @param patientRecordTemplate 周期配置
     * @return 结果
     */
    public int updatePatientRecordTemplate(PatientRecordTemplate patientRecordTemplate);

    /**
     * 批量删除周期配置
     * 
     * @param followIds 需要删除的周期配置主键集合
     * @return 结果
     */
    public int deletePatientRecordTemplateByFollowIds(Long[] followIds);

    /**
     * 删除周期配置信息
     * 
     * @param followId 周期配置主键
     * @return 结果
     */
    public int deletePatientRecordTemplateByFollowId(Long followId);
}
