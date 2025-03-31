package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchXunfeiTemplate;

import java.util.List;

/**
 * 讯飞智能外呼模板Mapper接口
 * 
 * @date 2023-10-30
 */
public interface ResearchXunfeiTemplateMapper{
    /**
     * 查询讯飞智能外呼模板
     * 
     * @param id 讯飞智能外呼模板主键
     * @return 讯飞智能外呼模板
     */
    public ResearchXunfeiTemplate selectResearchXunfeiTemplateById(Long id);

    /**
     * 查询讯飞智能外呼模板列表
     * 
     * @param researchXunfeiTemplate 讯飞智能外呼模板
     * @return 讯飞智能外呼模板集合
     */
    public List<ResearchXunfeiTemplate> selectResearchXunfeiTemplateList(ResearchXunfeiTemplate researchXunfeiTemplate);

    /**
     * 新增讯飞智能外呼模板
     * 
     * @param researchXunfeiTemplate 讯飞智能外呼模板
     * @return 结果
     */
    public int insertResearchXunfeiTemplate(ResearchXunfeiTemplate researchXunfeiTemplate);

    /**
     * 修改讯飞智能外呼模板
     * 
     * @param researchXunfeiTemplate 讯飞智能外呼模板
     * @return 结果
     */
    public int updateResearchXunfeiTemplate(ResearchXunfeiTemplate researchXunfeiTemplate);

    /**
     * 删除讯飞智能外呼模板
     * 
     * @param id 讯飞智能外呼模板主键
     * @return 结果
     */
    public int deleteResearchXunfeiTemplateById(Long id);

    /**
     * 批量删除讯飞智能外呼模板
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchXunfeiTemplateByIds(Long[] ids);


    public ResearchXunfeiTemplate selectBySpeedId(String speechId);


}
