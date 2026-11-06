package com.dchealth.research.domain;

import lombok.Data;

import java.util.List;

@Data
public class ResearchRoleAuth {

    private Long id;
    private Long roleId;//角色id
    private Long deptId;//部门id
    private String authType;//权限类型 1查看 2 编辑 3 删除
    /**
     * 权限集合
     */
    private List<String> authTypes;

    public ResearchRoleAuth(){}

    public ResearchRoleAuth(Long roleId, Long deptId, String authType){
        this.roleId = roleId;
        this.deptId = deptId;
        this.authType = authType;
    }

    public ResearchRoleAuth(Long roleId, Long deptId, List<String> authTypes){
        this.roleId = roleId;
        this.deptId = deptId;
        this.authTypes = authTypes;
    }

}
