package com.dchealth.research.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.research.domain.ResearchXunfeiTemplateVar;
import com.dchealth.research.mapper.ResearchXunfeiTemplateVarMapper;
import com.dchealth.research.service.IResearchXunfeiTemplateVarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 讯飞智能外呼模板话术变量Service业务层处理
 * 
 * @author lchbu
 * @date 2023-10-30
 */
@Service
public class ResearchXunfeiTemplateVarServiceImpl implements IResearchXunfeiTemplateVarService {
    @Autowired
    private ResearchXunfeiTemplateVarMapper researchXunfeiTemplateVarMapper;

    /**
     * 查询讯飞智能外呼模板话术变量
     * 
     * @param id 讯飞智能外呼模板话术变量主键
     * @return 讯飞智能外呼模板话术变量
     */
    @Override
    public ResearchXunfeiTemplateVar selectResearchXunfeiTemplateVarById(Long id){
        return researchXunfeiTemplateVarMapper.selectResearchXunfeiTemplateVarById(id);
    }

    /**
     * 查询讯飞智能外呼模板话术变量列表
     * 
     * @param researchXunfeiTemplateVar 讯飞智能外呼模板话术变量
     * @return 讯飞智能外呼模板话术变量
     */
    @Override
    public List<ResearchXunfeiTemplateVar> selectResearchXunfeiTemplateVarList(ResearchXunfeiTemplateVar researchXunfeiTemplateVar){
        return researchXunfeiTemplateVarMapper.selectResearchXunfeiTemplateVarList(researchXunfeiTemplateVar);
    }

    /**
     * 新增讯飞智能外呼模板话术变量
     * 
     * @param researchXunfeiTemplateVar 讯飞智能外呼模板话术变量
     * @return 结果
     */
    @Override
    public int insertResearchXunfeiTemplateVar(ResearchXunfeiTemplateVar researchXunfeiTemplateVar){
        researchXunfeiTemplateVar.setCreateTime(DateUtils.getNowDate());
        return researchXunfeiTemplateVarMapper.insertResearchXunfeiTemplateVar(researchXunfeiTemplateVar);
    }

    /**
     * 修改讯飞智能外呼模板话术变量
     * 
     * @param researchXunfeiTemplateVar 讯飞智能外呼模板话术变量
     * @return 结果
     */
    @Override
    public int updateResearchXunfeiTemplateVar(ResearchXunfeiTemplateVar researchXunfeiTemplateVar){
        researchXunfeiTemplateVar.setUpdateTime(DateUtils.getNowDate());
        return researchXunfeiTemplateVarMapper.updateResearchXunfeiTemplateVar(researchXunfeiTemplateVar);
    }

    /**
     * 批量删除讯飞智能外呼模板话术变量
     * 
     * @param ids 需要删除的讯飞智能外呼模板话术变量主键
     * @return 结果
     */
    @Override
    public int deleteResearchXunfeiTemplateVarByIds(Long[] ids){
        return researchXunfeiTemplateVarMapper.deleteResearchXunfeiTemplateVarByIds(ids);
    }

    /**
     * 删除讯飞智能外呼模板话术变量信息
     * 
     * @param id 讯飞智能外呼模板话术变量主键
     * @return 结果
     */
    @Override
    public int deleteResearchXunfeiTemplateVarById(Long id){
        return researchXunfeiTemplateVarMapper.deleteResearchXunfeiTemplateVarById(id);
    }
}
