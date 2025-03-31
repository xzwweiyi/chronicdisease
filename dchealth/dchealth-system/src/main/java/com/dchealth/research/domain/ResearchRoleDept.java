package com.dchealth.research.domain;

import lombok.Data;

@Data
public class ResearchRoleDept {

    private Long roleId;//角色id
    private Long deptId;//部门id

    public ResearchRoleDept(){}

    public ResearchRoleDept(Long roleId, Long deptId){
        this.roleId = roleId;
        this.deptId = deptId;
    }

}
