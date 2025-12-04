package com.dchealth.cro.form.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 属性题目设置评价结果对象 f_medical_exam_set_evaluation
 * 
 * @author lchbu
 * @date 2023-04-03
 */
@Data
public class FMedicalExamSetEvaluation {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 模板ID */
    private Long medicalFormId;

    /** 区间最小值 */
    private Float minValue;

    /** 区间最大值 */
    private Float maxValue;

    /** 评价结果 */
    private String evaluationResult;

    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
