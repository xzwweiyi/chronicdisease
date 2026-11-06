package com.dchealth.medical.service;

import java.util.List;

import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.ResearchPatient;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2022-06-15
 */
public interface IResearchPatientService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ResearchPatient selectResearchPatientById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param researchPatient 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<ResearchPatient> selectResearchPatientList(ResearchPatient researchPatient);

    /**
     * 新增【请填写功能名称】
     *
     * @param researchPatient 【请填写功能名称】
     * @return 结果
     */
    public int insertResearchPatient(ResearchPatient researchPatient);

    public int batchInsertResearchPatient(List<ResearchPatient> researchPatients);

    public int batchDelResearchPatient(List<ResearchPatient> researchPatients);

    /**
     * 修改【请填写功能名称】
     *
     * @param researchPatient 【请填写功能名称】
     * @return 结果
     */
    public int updateResearchPatient(ResearchPatient researchPatient);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteResearchPatientByIds(Long[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteResearchPatientById(Long id);

    List<MpPerson> listByDiseaseId(ResearchPatient researchPatient);
}
