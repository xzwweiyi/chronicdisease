package com.dchealth.cro.form.domain;

import lombok.Data;

/**
 * 动态实体
 */
@Data
public class DynamicCommonProperty {

    /**
     * id
     */
    private Long id;

    /**
     * 档案ID
     */
    private Long recordId;

    /**
     * 表单ID
     */
    private Long formId;

    /**
     * 来源(1-患者，2-EDC)
     */
    private String type;

    private Integer number;

    /**
     * 列表号
     */
    private Integer columnNumber;

    /**
     * 属性code
     */
    private String propertyCode;

    /**
     * 属性值
     */
    private String propertyValue;

    /**
     * 其它属性值
     */
    private String other;

    /**
     * 属性code
     */
    private Long propertyId;

    /**
     * 问卷ID
     */
    private Long articleId;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 队列ID
     */
    private Long researchId;

    /**
     * 随访节点ID
     */
    private Long followId;

    /**
     * 问卷填报记录id
     */
    private Long examRecordId;
}
