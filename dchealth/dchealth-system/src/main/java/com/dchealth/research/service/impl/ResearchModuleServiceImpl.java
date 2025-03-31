package com.dchealth.research.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.research.mapper.ResearchModuleMapper;
import com.dchealth.research.domain.ResearchModule;
import com.dchealth.research.service.IResearchModuleService;

/**
 * 队列模块Service业务层处理
 * 
 * @author lchbu
 * @date 2022-08-25
 */
@Service
public class ResearchModuleServiceImpl implements IResearchModuleService 
{
    @Autowired
    private ResearchModuleMapper researchModuleMapper;

    /**
     * 查询队列模块
     * 
     * @param id 队列模块主键
     * @return 队列模块
     */
    @Override
    public ResearchModule selectResearchModuleById(Long id)
    {
        return researchModuleMapper.selectResearchModuleById(id);
    }

    /**
     * 查询队列模块列表
     * 
     * @param researchModule 队列模块
     * @return 队列模块
     */
    @Override
    public List<ResearchModule> selectResearchModuleList(ResearchModule researchModule)
    {
        return researchModuleMapper.selectResearchModuleList(researchModule);
    }

    /**
     * 新增队列模块
     * 
     * @param researchModule 队列模块
     * @return 结果
     */
    @Override
    public int insertResearchModule(ResearchModule researchModule)
    {
        researchModule.setCreateTime(DateUtils.getNowDate());
        return researchModuleMapper.insertResearchModule(researchModule);
    }

    /**
     * 修改队列模块
     * 
     * @param researchModule 队列模块
     * @return 结果
     */
    @Override
    public int updateResearchModule(ResearchModule researchModule)
    {
        researchModule.setUpdateTime(DateUtils.getNowDate());
        return researchModuleMapper.updateResearchModule(researchModule);
    }

    /**
     * 批量删除队列模块
     * 
     * @param ids 需要删除的队列模块主键
     * @return 结果
     */
    @Override
    public int deleteResearchModuleByIds(Long[] ids)
    {
        return researchModuleMapper.deleteResearchModuleByIds(ids);
    }

    /**
     * 删除队列模块信息
     * 
     * @param id 队列模块主键
     * @return 结果
     */
    @Override
    public int deleteResearchModuleById(Long id)
    {
        return researchModuleMapper.deleteResearchModuleById(id);
    }
}
