package com.dchealth.research.domain.vo;

import lombok.Data;

/**
 * 科室、病种、队列VO
 */
@Data
public class ResearchDiseasesVO {
    /**
     * 子部门
     */
    private Long deptId;
    /**
     * 父部门
     */
    private Long parentId;

    private String deptName;

    private Long diseaseId;

    private String diseaseName;

    private Long researchId;

    private String projectName;

    /** 阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态） */
    private String stage;

    /** 队列状态0待发布1进行中3已关闭 */
    private String researchStatus;
    private String createBy;

    private int num;

    private Integer orderNum;

    private Long diseasesGroupsId;
}
