package com.dchealth.template.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 属性题目设置评价结果对象 f_medical_exam_set_evaluation
 * 
 * @author lchbu
 * @date 2023-04-03
 */
@Data
public class FMedicalExamSetEvaluation extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long medicalFormId;

    /** 区间最小值 */
    @Excel(name = "区间最小值")
    private Float minValue;

    /** 区间最大值 */
    @Excel(name = "区间最大值")
    private Float maxValue;

    /** 评价结果 */
    @Excel(name = "评价结果")
    private String evaluationResult;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("medicalFormId", getMedicalFormId())
            .append("minValue", getMinValue())
            .append("maxValue", getMaxValue())
            .append("evaluationResult", getEvaluationResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
