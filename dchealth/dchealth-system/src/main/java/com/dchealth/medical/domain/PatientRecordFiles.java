package com.dchealth.medical.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 患者档案图片对象 patient_record_files
 * 
 * @author lchbu
 * @date 2022-11-24
 */
public class PatientRecordFiles extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;

    /** 患者ID */
    @Excel(name = "患者ID")
    private Long patientId;

    /** 档案ID */
    @Excel(name = "档案ID")
    private Long recordId;

    /** medical_form的主键id */
    @Excel(name = "medical_form的主键id")
    private Long medicalFormId;

    /** 图片code */
    @Excel(name = "图片code")
    private String imageCode;

    /** 父级图片code */
    @Excel(name = "父级图片code")
    private String imageParentCode;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long propertyId;


    private Long examRecordId;

    private String propertyCode;

    public String getPropertyCode() {
        return propertyCode;
    }

    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

    public Long getExamRecordId() {
        return examRecordId;
    }

    public void setExamRecordId(Long examRecordId) {
        this.examRecordId = examRecordId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }
    public void setPatientId(Long patientId) 
    {
        this.patientId = patientId;
    }

    public Long getPatientId() 
    {
        return patientId;
    }
    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setMedicalFormId(Long medicalFormId) 
    {
        this.medicalFormId = medicalFormId;
    }

    public Long getMedicalFormId() 
    {
        return medicalFormId;
    }
    public void setImageCode(String imageCode) 
    {
        this.imageCode = imageCode;
    }

    public String getImageCode() 
    {
        return imageCode;
    }
    public void setImageParentCode(String imageParentCode) 
    {
        this.imageParentCode = imageParentCode;
    }

    public String getImageParentCode() 
    {
        return imageParentCode;
    }
    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() 
    {
        return propertyId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("imageUrl", getImageUrl())
            .append("patientId", getPatientId())
            .append("recordId", getRecordId())
            .append("medicalFormId", getMedicalFormId())
            .append("imageCode", getImageCode())
            .append("imageParentCode", getImageParentCode())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("propertyId", getPropertyId())
            .toString();
    }
}
