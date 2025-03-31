package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchRoleModule;
import com.dchealth.research.domain.ResearchRoleModuleAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchRoleModuleAuthMapper {

    void deleteRoleModuleAuthByRoleId(Long roleId);

    void batchRoleModuleAuth(@Param("moduleAuths") List<ResearchRoleModuleAuth> moduleAuths);

    List<ResearchRoleModuleAuth> findRoleModuleAuthByRoleIdAndModuleId(@Param("roleId") Long roleId, @Param("moduleId") String moduleId);

    void deleteRoleModuleAuthByRoleIds(Long[] roleIds);

    void deleteRoleModuleAuthByModuleIdAndRoleIds(@Param("moduleId")String moduleId, @Param("roleIds") List<Long> roleIds);

    List<ResearchRoleModuleAuth> findRoleModuleAuthByResearchRoleModules(@Param("researchRoleModules") List<ResearchRoleModule> researchRoleModules);
}
