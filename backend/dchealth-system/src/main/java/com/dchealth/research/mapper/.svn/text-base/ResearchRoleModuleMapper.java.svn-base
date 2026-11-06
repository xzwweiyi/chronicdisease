package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchRoleModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchRoleModuleMapper {

    void deleteRoleModuleByRoleId(Long roleId);

    void batchRoleModule(List<ResearchRoleModule> researchRoleModules);

    List<ResearchRoleModule> findRoleModuleByRoleId(Long roleId);

    void deleteRoleModuleByRoleIds(Long[] roleIds);

    List<ResearchRoleModule> findRoleModuleByRoleIds(@Param("roleIds") List<Long> roleIds);

}
