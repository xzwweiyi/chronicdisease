package com.dchealth.research.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.research.domain.ResearchXunfeiTemplate;
import com.dchealth.research.mapper.ResearchXunfeiTemplateMapper;
import com.dchealth.research.service.IResearchXunfeiTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 讯飞智能外呼模板Service业务层处理
 * 
 * @date 2023-10-30
 */
@Service
public class ResearchXunfeiTemplateServiceImpl implements IResearchXunfeiTemplateService {
    @Autowired
    private ResearchXunfeiTemplateMapper researchXunfeiTemplateMapper;

    /**
     * 查询讯飞智能外呼模板
     * 
     * @param id 讯飞智能外呼模板主键
     * @return 讯飞智能外呼模板
     */
    @Override
    public ResearchXunfeiTemplate selectResearchXunfeiTemplateById(Long id){
        return researchXunfeiTemplateMapper.selectResearchXunfeiTemplateById(id);
    }

    /**
     * 查询讯飞智能外呼模板列表
     * 
     * @param researchXunfeiTemplate 讯飞智能外呼模板
     * @return 讯飞智能外呼模板
     */
    @Override
    public List<ResearchXunfeiTemplate> selectResearchXunfeiTemplateList(ResearchXunfeiTemplate researchXunfeiTemplate){
        return researchXunfeiTemplateMapper.selectResearchXunfeiTemplateList(researchXunfeiTemplate);
    }

    /**
     * 新增讯飞智能外呼模板
     * 
     * @param researchXunfeiTemplate 讯飞智能外呼模板
     * @return 结果
     */
    @Override
    public int insertResearchXunfeiTemplate(ResearchXunfeiTemplate researchXunfeiTemplate){
        researchXunfeiTemplate.setCreateTime(DateUtils.getNowDate());
        return researchXunfeiTemplateMapper.insertResearchXunfeiTemplate(researchXunfeiTemplate);
    }

    /**
     * 修改讯飞智能外呼模板
     * 
     * @param researchXunfeiTemplate 讯飞智能外呼模板
     * @return 结果
     */
    @Override
    public int updateResearchXunfeiTemplate(ResearchXunfeiTemplate researchXunfeiTemplate){
        researchXunfeiTemplate.setUpdateTime(DateUtils.getNowDate());
        return researchXunfeiTemplateMapper.updateResearchXunfeiTemplate(researchXunfeiTemplate);
    }

    /**
     * 批量删除讯飞智能外呼模板
     * 
     * @param ids 需要删除的讯飞智能外呼模板主键
     * @return 结果
     */
    @Override
    public int deleteResearchXunfeiTemplateByIds(Long[] ids){
        return researchXunfeiTemplateMapper.deleteResearchXunfeiTemplateByIds(ids);
    }

    /**
     * 删除讯飞智能外呼模板信息
     * 
     * @param id 讯飞智能外呼模板主键
     * @return 结果
     */
    @Override
    public int deleteResearchXunfeiTemplateById(Long id){
        return researchXunfeiTemplateMapper.deleteResearchXunfeiTemplateById(id);
    }
}
