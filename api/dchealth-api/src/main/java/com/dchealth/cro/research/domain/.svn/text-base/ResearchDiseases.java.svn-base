package com.dchealth.cro.research.domain;

import lombok.Data;

import java.util.Date;

/**
 * 病种对象 research_diseases
 * 
 * @author lchbu
 * @date 2022-09-13
 */
@Data
public class ResearchDiseases
{
    private static final long serialVersionUID = 1L;

    /** 病种id */
    private Long diseaseId;

    /** 病种名称 */
    private String diseaseName;

    /** 部门ID */
    private Long deptId;

    /** 是否删除 0未删除 1 删除 */
    private String isDeleted;

    /** 排序 */
    private Long sortNum;


    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /**
     * 待处理任务
     */
    private Integer pendingTasksNum = 0;

    /**
     * 进行中任务
     */
    private Integer ongoingTasksNum = 0;

    /**
     * 已完成任务
     */
    private Integer completedTasksNum = 0;

    /**
     * 已完成评价任务
     */
    private Integer evaluatedNum = 0;

}
