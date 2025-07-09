package com.dchealth.cro.medical.domain;

import com.dchealth.cro.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 患者档案健康管理对象 patient_record_health
 * 
 * @author lchbu
 * @date 2025-06-24
 */
@Data
public class PatientRecordHealth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 健康档案id */
    private String healthId;

    /** 患者档案id */
    private Long recordId;

    /** 患者id */
    private Long patientId;

    /** 研究项目id */
    private Long researchId;

    /** 随访节点id */
    private Long followId;

    /** 随访提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date submitTime;

    /** 健康管理类型 */
    private Integer healthManageType;

    /** 是否删除 */
    private Integer isDelete;

    private Date startTime;

    private Date endTime;

    public void setHealthId(String healthId)
    {
        this.healthId = healthId;
    }

    public String getHealthId()
    {
        return healthId;
    }
    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setPatientId(Long patientId) 
    {
        this.patientId = patientId;
    }

    public Long getPatientId() 
    {
        return patientId;
    }
    public void setResearchId(Long researchId) 
    {
        this.researchId = researchId;
    }

    public Long getResearchId() 
    {
        return researchId;
    }
    public void setFollowId(Long followId) 
    {
        this.followId = followId;
    }

    public Long getFollowId() 
    {
        return followId;
    }
    public void setSubmitTime(Date submitTime) 
    {
        this.submitTime = submitTime;
    }

    public Date getSubmitTime() 
    {
        return submitTime;
    }
    public void setHealthManageType(Integer healthManageType)
    {
        this.healthManageType = healthManageType;
    }

    public Integer getHealthManageType()
    {
        return healthManageType;
    }
    public void setIsDelete(Integer isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete() 
    {
        return isDelete;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("healthId", getHealthId())
            .append("recordId", getRecordId())
            .append("patientId", getPatientId())
            .append("researchId", getResearchId())
            .append("followId", getFollowId())
            .append("submitTime", getSubmitTime())
            .append("healthManageType", getHealthManageType())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("isDelete", getIsDelete())
            .toString();
    }
}
