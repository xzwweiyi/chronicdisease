package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchRoleSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchRoleSourceMapper {

    public void deleteRoleSource(Long roleId);

    public int batchRoleSource(@Param("researchRoleSources") List<ResearchRoleSource> researchRoleSources);

    public void deleteRoleSourceByRoleIds(Long[] roleIds);

    public int isDistributionResearchSource(Long sourceId);

    public List<ResearchRoleSource> findByRoles(List<Long> roles);

    public List<ResearchRoleSource> findByRoleId(Long roleId);
    
}
