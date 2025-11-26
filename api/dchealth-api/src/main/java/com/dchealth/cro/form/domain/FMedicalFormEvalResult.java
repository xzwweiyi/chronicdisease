package com.dchealth.cro.form.domain;

import com.dchealth.cro.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 【请填写功能名称】对象 f_medical_form_eval_result
 * 
 * @author wangpf
 * @date 2025-11-26
 */
public class FMedicalFormEvalResult extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** $column.columnComment */
    private Long medicalFormId;

    /** $column.columnComment */
    private Long recordId;

    /** $column.columnComment */
    private String evaluationResult;

    /** $column.columnComment */
    private String evaluationScores;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMedicalFormId(Long medicalFormId) 
    {
        this.medicalFormId = medicalFormId;
    }

    public Long getMedicalFormId() 
    {
        return medicalFormId;
    }
    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setEvaluationResult(String evaluationResult) 
    {
        this.evaluationResult = evaluationResult;
    }

    public String getEvaluationResult() 
    {
        return evaluationResult;
    }
    public void setEvaluationScores(String evaluationScores) 
    {
        this.evaluationScores = evaluationScores;
    }

    public String getEvaluationScores() 
    {
        return evaluationScores;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("medicalFormId", getMedicalFormId())
            .append("recordId", getRecordId())
            .append("evaluationResult", getEvaluationResult())
            .append("evaluationScores", getEvaluationScores())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
