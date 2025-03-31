package com.dchealth.medical.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 患者问卷填报关系对象 patient_exam_record
 * 
 * @author lchbu
 * @date 2022-11-17
 */
public class PatientExamRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 问卷id */
    @Excel(name = "问卷id")
    private Long articleId;

    /** 问卷名称 */
    private String title;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long medicalFormId;

    /** 患者ID */
    @Excel(name = "患者ID")
    private Long patientId;

    /** 患者名称 */
    private String patientName;

    /** 问卷存值表名 */
    private String examTable;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExamTable() {
        return examTable;
    }

    public void setExamTable(String examTable) {
        this.examTable = examTable;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setArticleId(Long articleId) 
    {
        this.articleId = articleId;
    }

    public Long getArticleId() 
    {
        return articleId;
    }
    public void setMedicalFormId(Long medicalFormId) 
    {
        this.medicalFormId = medicalFormId;
    }

    public Long getMedicalFormId() 
    {
        return medicalFormId;
    }
    public void setPatientId(Long patientId) 
    {
        this.patientId = patientId;
    }

    public Long getPatientId() 
    {
        return patientId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("articleId", getArticleId())
            .append("medicalFormId", getMedicalFormId())
            .append("patientId", getPatientId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
