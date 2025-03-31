package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchRoleDept;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ResearchRoleDeptMapper {

    void deleteRoledept(Long roleId);

    void batchRoleDept(List<ResearchRoleDept> researchRoleDepts);

    void deleteRoleDeptByRoleIds(Long[] roleIds);

    List<ResearchRoleDept> findRoleDeptByRoleId(Long roleId);

    List<ResearchRoleDept> findRoleDeptByRoleIds(List<Long> roleIds);

    void deleteRoleDeptByRoleIdAndDeptIds(@Param("roleId") Long roleId, @Param("deptIds") List<Long> deptIds);

    void deleteRoleDeptByRoleIdAndDeptId(@Param("roleId") Long roleId, @Param("deptId") Long deptId);
}
