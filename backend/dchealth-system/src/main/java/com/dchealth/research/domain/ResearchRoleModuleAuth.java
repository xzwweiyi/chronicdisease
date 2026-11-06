package com.dchealth.research.domain;

import lombok.Data;

@Data
public class ResearchRoleModuleAuth {
    private Long id;
    private Long roleId;
    private String moduleId;
    private String authType;



}
