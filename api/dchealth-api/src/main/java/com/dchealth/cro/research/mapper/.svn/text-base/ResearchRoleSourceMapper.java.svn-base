package com.dchealth.cro.research.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.research.domain.ResearchRoleSource;

/**
 * 角色和菜单关联表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface ResearchRoleSourceMapper 
{
    /**
     * 查询角色和菜单关联表
     * 
     * @param roleId 角色和菜单关联表主键
     * @return 角色和菜单关联表
     */
    public ResearchRoleSource selectResearchRoleSourceByRoleId(Long roleId);

    /**
     * 查询角色和菜单关联表列表
     * 
     * @param researchRoleSource 角色和菜单关联表
     * @return 角色和菜单关联表集合
     */
    public List<ResearchRoleSource> selectResearchRoleSourceList(ResearchRoleSource researchRoleSource);

    /**
     * 新增角色和菜单关联表
     * 
     * @param researchRoleSource 角色和菜单关联表
     * @return 结果
     */
    public int insertResearchRoleSource(ResearchRoleSource researchRoleSource);

    /**
     * 修改角色和菜单关联表
     * 
     * @param researchRoleSource 角色和菜单关联表
     * @return 结果
     */
    public int updateResearchRoleSource(ResearchRoleSource researchRoleSource);

    /**
     * 删除角色和菜单关联表
     * 
     * @param roleId 角色和菜单关联表主键
     * @return 结果
     */
    public int deleteResearchRoleSourceByRoleId(Long roleId);

    /**
     * 批量删除角色和菜单关联表
     * 
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchRoleSourceByRoleIds(String[] roleIds);
}
