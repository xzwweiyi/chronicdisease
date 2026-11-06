package com.dchealth.research.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 队列角色数据权限
 */
@Data
public class ResearchDataPermissionsVO {

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 随访节点
     */
    private List<ResearchFollowVO> researchFollowVOS;

    /**
     * 科室数据选择
     */
    private List<ResearchDeptVO> deptVOS;

}
