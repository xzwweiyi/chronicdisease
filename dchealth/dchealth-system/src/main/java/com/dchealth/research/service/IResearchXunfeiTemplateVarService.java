package com.dchealth.research.service;

import com.dchealth.research.domain.ResearchXunfeiTemplateVar;

import java.util.List;


/**
 * 讯飞智能外呼模板话术变量Service接口
 * 
 * @date 2023-10-30
 */
public interface IResearchXunfeiTemplateVarService{
    /**
     * 查询讯飞智能外呼模板话术变量
     * 
     * @param id 讯飞智能外呼模板话术变量主键
     * @return 讯飞智能外呼模板话术变量
     */
    public ResearchXunfeiTemplateVar selectResearchXunfeiTemplateVarById(Long id);


    /**
     * 查询讯飞智能外呼模板话术变量列表
     * 
     * @param researchXunfeiTemplateVar 讯飞智能外呼模板话术变量
     * @return 讯飞智能外呼模板话术变量集合
     */
    public List<ResearchXunfeiTemplateVar> selectResearchXunfeiTemplateVarList(ResearchXunfeiTemplateVar researchXunfeiTemplateVar);

    /**
     * 新增讯飞智能外呼模板话术变量
     * 
     * @param researchXunfeiTemplateVar 讯飞智能外呼模板话术变量
     * @return 结果
     */
    public int insertResearchXunfeiTemplateVar(ResearchXunfeiTemplateVar researchXunfeiTemplateVar);

    /**
     * 修改讯飞智能外呼模板话术变量
     * 
     * @param researchXunfeiTemplateVar 讯飞智能外呼模板话术变量
     * @return 结果
     */
    public int updateResearchXunfeiTemplateVar(ResearchXunfeiTemplateVar researchXunfeiTemplateVar);

    /**
     * 批量删除讯飞智能外呼模板话术变量
     * 
     * @param ids 需要删除的讯飞智能外呼模板话术变量主键集合
     * @return 结果
     */
    public int deleteResearchXunfeiTemplateVarByIds(Long[] ids);

    /**
     * 删除讯飞智能外呼模板话术变量信息
     * 
     * @param id 讯飞智能外呼模板话术变量主键
     * @return 结果
     */
    public int deleteResearchXunfeiTemplateVarById(Long id);
}
