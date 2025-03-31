package com.dchealth.cro.medical.mapper;

import com.dchealth.cro.medical.domain.PatientRecordTemplate;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 周期配置Mapper接口
 * 
 * @author lchbu
 * @date 2022-08-23
 */
@Repository
public interface PatientRecordTemplateMapper 
{
    /**
     * 查询周期配置
     * 
     * @param followId 周期配置主键
     * @return 周期配置
     */
    public PatientRecordTemplate selectPatientRecordTemplateByFollowId(Long followId);

    /**
     * 根据队列id查询随访节点信息
     * @param researchId
     * @return
     */
    public List<PatientRecordTemplate> selectPatientRecordTemplateByResearchId(Long researchId);

    /**
     * 查询已发布队列的规则
     * @return
     */
    public List<PatientRecordTemplate> selectPatientRecordTemplateByResearchRelease();

    /**
     * researchIds查询
     * @param ids
     * @return
     */
    public List<PatientRecordTemplate> selectPatientRecordTemplateByResearchIds(Long[] ids);

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
     * 删除周期配置
     * 
     * @param followId 周期配置主键
     * @return 结果
     */
    public int deletePatientRecordTemplateByFollowId(Long followId);

    /**
     * 批量删除周期配置
     * 
     * @param followIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordTemplateByFollowIds(Long[] followIds);

    /**
     * 查询周期排序最大值
     * @param researchId
     * @return
     */
    Integer selectMaxFollowNumByResearchId(Long researchId);
}
