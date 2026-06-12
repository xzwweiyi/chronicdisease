package com.dchealth.research.domain.vo;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 队列信息
 */
@Data
public class ResearchInfoVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 主键 */
    private Long noId;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long deptId;

    /** 队列展示图 */
    @Excel(name = "队列展示图")
    private String imageUrl;

    /** 队列名称 */
    @Excel(name = "队列名称")
    private String projectName;

    /** 队列介绍 */
    @Excel(name = "队列介绍")
    private String researchSynopsis;

    /** 当前患者数 */
    @Excel(name = "当前患者数")
    private Long patientNum;

    /** 中心数 */
    @Excel(name = "中心数")
    private Long hospitalNum;

    /** 队列状态0待发布1进行中2已关闭 */
    @Excel(name = "队列状态0待发布1进行中2已关闭")
    private String researchStatus;

    /** 病种id */
    @Excel(name = "病种id")
    private Long diseaseTypeId;

    /** 队列负责人 */
    @Excel(name = "队列负责人")
    private Long doctorId;

    private String doctorName;

    /** 队列类型 */
    @Excel(name = "队列类型")
    private String researchType;

    /** 研究介绍 */
    @Excel(name = "研究介绍")
    private String researchIntro;

    /** 治疗方案 */
    @Excel(name = "治疗方案")
    private String researchScheme;

    /** 入组标准 */
    @Excel(name = "入组标准")
    private String researchStandard;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /** 发布时间 */
    private String publicTime;

    /** 阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态） */
    private String stage;

    /** 是否开启随访率：1-开启，0-关闭 */
    private Integer isFollowRate;

    private List<Long> researchIds;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 病种名称
     */
    private String diseasesName;

    private Long userId;
    /**
     * 查询使用：不在该状态的队列
     */
    private String notResearchStatus;
}
