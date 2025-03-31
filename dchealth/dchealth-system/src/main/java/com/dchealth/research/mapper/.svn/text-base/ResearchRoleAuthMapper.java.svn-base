package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchRoleAuth;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchRoleAuthMapper {

    void deleteRoleDeptAuTh(Long roleId);

    void batchRoleAuth(@Param("auths") List<ResearchRoleAuth> auths);

    List<ResearchRoleAuth> findRoleAuthByRoleIdAndDeptId(@Param("roleId") Long roleId, @Param("deptId") Long deptId);

    /**
     * 根据角色id查询
     * @param roleId
     * @return
     */
    public List<ResearchRoleAuth> findRoleAuthByRoleId(Long roleId);

    void deleteRoleDeptAuthByRoleIdAndDeptIds(@Param("roleId") Long roleId, @Param("deptIds") List<Long> deptIds);

    void deleteRoleDeptAuthByRoleIdAndDeptId(@Param("roleId") Long roleId, @Param("deptId") Long deptId);

    void deleteRoleDeptAuThByRoleIds(Long[] roleIds);
}
