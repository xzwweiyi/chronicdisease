package com.dchealth.research.mapper;

import java.util.List;
import com.dchealth.research.domain.ResearchModule;

/**
 * 队列模块Mapper接口
 * 
 * @author lchbu
 * @date 2022-08-25
 */
public interface ResearchModuleMapper 
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
     * 删除队列模块
     * 
     * @param id 队列模块主键
     * @return 结果
     */
    public int deleteResearchModuleById(Long id);

    /**
     * 批量删除队列模块
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchModuleByIds(Long[] ids);
}
