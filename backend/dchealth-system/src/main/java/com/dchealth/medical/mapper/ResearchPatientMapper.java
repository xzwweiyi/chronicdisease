package com.dchealth.medical.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.ResearchPatient;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-15
 */
public interface ResearchPatientMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public ResearchPatient selectResearchPatientById(Long id);
    public List<ResearchPatient> selectResearchPatientByResearchId(Long researchId);

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

    /**
     * 修改【请填写功能名称】
     *
     * @param researchPatient 【请填写功能名称】
     * @return 结果
     */
    public int updateResearchPatient(ResearchPatient researchPatient);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteResearchPatientById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchPatientByIds(Long[] ids);


    int batchInsertResearchPatient(List<ResearchPatient> researchPatients);

    int batchInsert(List<ResearchPatient> list);

    int delResearchPatient(ResearchPatient researchPatient);

    /**
     * 根据患者id查询队列
     * @param patientId
     * @return
     */
    List<ResearchPatient> selectResearchPatientByPatId(@Param("personId")String personId);
	public List<MpPerson> listByDiseaseId(ResearchPatient researchPatient);
}

