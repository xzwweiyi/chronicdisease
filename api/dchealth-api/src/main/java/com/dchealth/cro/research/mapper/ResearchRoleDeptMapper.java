package com.dchealth.cro.research.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.research.domain.ResearchRoleDept;

/**
 * 角色和部门关联表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface ResearchRoleDeptMapper 
{
    /**
     * 查询角色和部门关联表
     * 
     * @param roleId 角色和部门关联表主键
     * @return 角色和部门关联表
     */
    public ResearchRoleDept selectResearchRoleDeptByRoleId(Long roleId);

    /**
     * 查询角色和部门关联表列表
     * 
     * @param researchRoleDept 角色和部门关联表
     * @return 角色和部门关联表集合
     */
    public List<ResearchRoleDept> selectResearchRoleDeptList(ResearchRoleDept researchRoleDept);

    /**
     * 新增角色和部门关联表
     * 
     * @param researchRoleDept 角色和部门关联表
     * @return 结果
     */
    public int insertResearchRoleDept(ResearchRoleDept researchRoleDept);

    /**
     * 修改角色和部门关联表
     * 
     * @param researchRoleDept 角色和部门关联表
     * @return 结果
     */
    public int updateResearchRoleDept(ResearchRoleDept researchRoleDept);

    /**
     * 删除角色和部门关联表
     * 
     * @param roleId 角色和部门关联表主键
     * @return 结果
     */
    public int deleteResearchRoleDeptByRoleId(Long roleId);

    /**
     * 批量删除角色和部门关联表
     * 
     * @param roleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchRoleDeptByRoleIds(String[] roleIds);
}
