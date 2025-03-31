package com.dchealth.cro.form.domain;

import com.dchealth.cro.common.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 患者问卷填报关系对象 patient_exam_record
 *
 * @author lchbu
 * @date 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class PatientExamRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 问卷id */
    private Long articleId;

    /** 模板ID */
    private Long medicalFormId;

    /** 患者ID */
    private Long patientId;

    /** 问卷存值表名 */
    private String examTable;

    /*
     * 0待填写1进行中2已完成
     */
    private String fillStatus;

    private Long researchId;
}
