package com.dchealth.research.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoleDeptVo {

    private Long deptId;

    private List<String> authType;//权限数组
}
