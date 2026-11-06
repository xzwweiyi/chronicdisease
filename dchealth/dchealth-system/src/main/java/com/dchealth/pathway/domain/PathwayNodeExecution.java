package com.dchealth.pathway.domain;

import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 路径节点执行记录对象 pathway_node_execution
 *
 * @author xuzw
 * @date 2026-06-30
 */
@Data
public class PathwayNodeExecution extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 患者ID */
    private Long patientId;

    /** 路径ID */
    private Long researchId;

    /** 节点模板ID（关联patient_record_template.follow_id） */
    private Long templateId;

    /** 随访记录ID（关联patient_record.record_id） */
    private Long recordId;

    /** 节点类型：follow_up-随访，assessment-评估，intervention-干预，education-宣教，reminder-提醒，orders-医嘱 */
    private String nodeType;

    /** 执行状态：0-待执行，1-执行中，2-已完成，3-已跳过 */
    private String executionStatus;

    /** 计划执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduledTime;

    /** 实际执行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date actualTime;

    /** 执行结果（JSON） */
    private String executionResult;

    /** 执行人ID */
    private Long executedBy;

    /** 备注 */
    private String remark;
}
