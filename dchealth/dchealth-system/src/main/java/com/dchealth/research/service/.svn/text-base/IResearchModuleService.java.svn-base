package com.dchealth.research.service;

import java.util.List;
import com.dchealth.research.domain.ResearchModule;

/**
 * 队列模块Service接口
 * 
 * @author lchbu
 * @date 2022-08-25
 */
public interface IResearchModuleService 
{
    /**
     * 查询队列模块
     * 
     * @param id 队列模块主键
     * @return 队列模块
     */
    public ResearchModule selectResearchModuleById(Long id);

    /**
     * 查询队列模块列表
     * 
     * @param researchModule 队列模块
     * @return 队列模块集合
     */
    public List<ResearchModule> selectResearchModuleList(ResearchModule researchModule);

    /**
     * 新增队列模块
     * 
     * @param researchModule 队列模块
     * @return 结果
     */
    public int insertResearchModule(ResearchModule researchModule);

    /**
     * 修改队列模块
     * 
     * @param researchModule 队列模块
     * @return 结果
     */
    public int updateResearchModule(ResearchModule researchModule);

    /**
     * 批量删除队列模块
     * 
     * @param ids 需要删除的队列模块主键集合
     * @return 结果
     */
    public int deleteResearchModuleByIds(Long[] ids);

    /**
     * 删除队列模块信息
     * 
     * @param id 队列模块主键
     * @return 结果
     */
    public int deleteResearchModuleById(Long id);
}
