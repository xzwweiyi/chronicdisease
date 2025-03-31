package com.dchealth.cro.research.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.research.domain.ResearchPatient;

/**
 * 队列患者Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface ResearchPatientMapper 
{
    /**
     * 查询队列患者
     * 
     * @param id 队列患者主键
     * @return 队列患者
     */
    public ResearchPatient selectResearchPatientById(Long id);

    /**
     * 查询队列患者列表
     * 
     * @param researchPatient 队列患者
     * @return 队列患者集合
     */
    public List<ResearchPatient> selectResearchPatientList(ResearchPatient researchPatient);

    /**
     * 新增队列患者
     * 
     * @param researchPatient 队列患者
     * @return 结果
     */
    public int insertResearchPatient(ResearchPatient researchPatient);

    /**
     * 修改队列患者
     * 
     * @param researchPatient 队列患者
     * @return 结果
     */
    public int updateResearchPatient(ResearchPatient researchPatient);

    /**
     * 删除队列患者
     * 
     * @param id 队列患者主键
     * @return 结果
     */
    public int deleteResearchPatientById(Long id);

    /**
     * 批量删除队列患者
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchPatientByIds(String[] ids);
}
