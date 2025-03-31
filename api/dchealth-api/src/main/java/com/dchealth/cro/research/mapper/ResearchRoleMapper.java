package com.dchealth.cro.research.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.research.domain.ResearchRole;

/**
 * 角色信息表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface ResearchRoleMapper 
{
    /**
     * 查询角色信息表
     * 
     * @param roleId 角色信息表主键
     * @return 角色信息表
     */
    public ResearchRole selectResearchRoleByRoleId(Long roleId);

    /**
     * 查询角色信息表列表
     * 
     * @param researchRole 角色信息表
     * @return 角色信息表集合
     */
    public List<ResearchRole> selectResearchRoleList(ResearchRole researchRole);

    /**
     * 新增角色信息表
     * 
     * @param researchRole 角色信息表
     * @return 结果
     */
    public int insertResearchRole(ResearchRole researchRole);

    /**
     * 修改角色信息表
     * 
     * @param researchRole 角色信息表
     * @return 结果
     */
    public int updateResearchRole(ResearchRole researchRole);

    /**
     * 删除角色信息表
     * 
     * @param roleId 角色信息表主键
     * @return 结果
     */
    public int deleteResearchRoleByRoleId(Long roleId);

    /**
     * 批量删除角色信息表
     * 
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchRoleByRoleIds(String[] roleIds);
}
