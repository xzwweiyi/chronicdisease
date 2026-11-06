package com.dchealth.pathway.domain;

import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 路径干预方案记录对象 pathway_intervention_record
 *
 * @author xuzw
 * @date 2026-06-30
 */
@Data
public class PathwayInterventionRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 患者ID */
    private Long patientId;

    /** 路径ID */
    private Long researchId;

    /** 随访记录ID */
    private Long recordId;

    /** 干预类型：1-药物，2-饮食，3-运动，4-心理，5-其他 */
    private String interventionType;

    /** 干预内容 */
    private String interventionContent;

    /** 目标值 */
    private String targetValue;

    /** 当前值 */
    private String currentValue;

    /** 调整原因 */
    private String adjustReason;

    /** 系统建议 */
    private String systemSuggestion;

    /** 医生调整 */
    private String doctorAdjustment;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /** 计划结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /** 状态：1-执行中，2-已完成，3-已终止 */
    private String status;
}
