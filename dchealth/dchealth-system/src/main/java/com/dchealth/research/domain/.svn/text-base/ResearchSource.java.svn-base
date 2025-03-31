package com.dchealth.research.domain;

import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class ResearchSource extends BaseEntity {

    private Long sourceId;//菜单id
    private String sourceName;//菜单名称
    private Long parentId;//父菜单id
    private String component;//接口路径
    private String status;//菜单状态（0正常 1停用）
    private String perms;//权限标识


    private List<ResearchSource> child;
    private Long roleId;

}
