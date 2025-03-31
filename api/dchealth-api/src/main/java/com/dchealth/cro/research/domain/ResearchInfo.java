package com.dchealth.cro.research.domain;

import lombok.Data;

import java.util.Date;

/**
 * 队列基础信息对象 research_info
 * 
 * @author lchbu
 * @date 2022-09-14
 */
@Data
public class ResearchInfo
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 科室ID */
    private Long deptId;

    /** 队列展示图 */
    private String imageUrl;

    /** 队列名称 */
    private String projectName;

    /** 队列介绍 */
    private String researchSynopsis;

    /** 当前患者数 */
    private Long patientNum;

    /** 中心数 */
    private Long hospitalNum;

    /** 队列状态0待发布1进行中3已关闭 */
    private String researchStatus;

    /** 病种id */
    private Long diseaseTypeId;

    /** 队列负责人 */
    private Long doctorId;

    /** 类型 1本中心队列  2院外队列 */
    private String researchType;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /** 阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态） */
    private String stage;

    /** 创建者 */
    private String createBy;
    /**
     * 创建人id
     */
    private Long userId;

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
