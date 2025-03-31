package com.dchealth.cro.medical.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 质效评价对象 patient_record_evaluate
 * 
 * @author lchbu
 * @date 2023-02-15
 */
@Data
public class PatientRecordEvaluate
{

    /** 档案id */
    private Long recordId;

    /** 评价人 */
    private Long evaluateUserId;

    /** 回复时效 */
    private Long replyPeriod;

    /** 回复质量 */
    private Long replyQuality;

    /** $column.columnComment */
    private String replyEvaluate;

    /** 质效评价时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date evaluateTime;

    /** 医生id */
    private Long doctorId;
}
