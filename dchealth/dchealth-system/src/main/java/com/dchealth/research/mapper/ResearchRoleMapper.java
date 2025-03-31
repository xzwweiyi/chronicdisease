package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchRoleMapper {

    int insertResearchRole(ResearchRole researchRole);

    int updateResearchRole(ResearchRole researchRole);

    List<ResearchRole> selectResearchRoleList(ResearchRole researchRole);

    ResearchRole findResearchRoleByRoleId(Long roleId);

    int deleteRoleByIds(Long[] roleIds);

    List<ResearchRole> findRoleByRoleIds(List<Long> roleIds);

    List<ResearchRole> findResearchRoleByResearchId(Long researchId);

    /**
     * 校验角色名称
     * @param researchId
     * @param roleName
     * @return
     */
    public ResearchRole checkRoleNameUnique(@Param("researchId")Long researchId, @Param("roleName")String roleName);

    /**
     * 校验角色标识
     * @param researchId
     * @param roleKey
     * @return
     */
    public ResearchRole checkRoleKeyUnique(@Param("researchId")Long researchId,@Param("roleKey")String roleKey);
}
