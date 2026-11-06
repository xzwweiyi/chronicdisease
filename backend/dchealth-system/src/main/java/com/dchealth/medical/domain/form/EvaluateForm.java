package com.dchealth.medical.domain.form;

import lombok.Data;

/**
 * 评价管理
 */
@Data
public class EvaluateForm {

    /**
     * 所属类型1随访处理评价2预警处理评价
     */
    private String belongType;

    /**
     * 1待评价2已完成
     */
    private String evaluateStatus;

    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 患者姓名
     */
    private String patientName;
}
