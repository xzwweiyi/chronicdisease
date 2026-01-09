package com.dchealth.medical.domain.form;

import lombok.Data;

import java.util.List;

/**
 * 动态form
 */
@Data
public class DynamicForm {

    /**
     * 执行sql
     */
    private String sql;

    /**
     * 档案id
     */
    private Long recordId;

    /**
     * formId
     */
    private Long formId;

    /**
     * 来源(1-患者，2-EDC)
     */
    private String type;

    /**
     * 资讯ID
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
     * 随访ID
     */
    private Long followId;


    /**
     * 问卷填报记录id
     */
    private Long examRecordId;

    private String examTable;

    private List<Long> recordIds;

}
