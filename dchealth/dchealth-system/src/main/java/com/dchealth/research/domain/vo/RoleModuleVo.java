package com.dchealth.research.domain.vo;

import lombok.Data;

import java.util.List;
@Data
public class RoleModuleVo {

    private String moduleId;//模块id

    private List<String> authType;//权限数组
}
