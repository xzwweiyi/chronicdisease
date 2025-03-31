package com.dchealth.research.domain.form;

import lombok.Data;

import java.util.List;

/**
 * 队列数据权限form
 */
@Data
public class ResearchDataPermissionsForm {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 随访节点勾选数据
     */
    private List<Long> followIds;

    /**
     * 数据范围
     */
    private String dataScope;

    /**
     * 科室及操作权限
     */
    private List<ResearchDeptAndOperForm> deptAndOperForms;

}
