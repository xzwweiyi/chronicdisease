package com.dchealth.health.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 患者档案健康管理对象 patient_record_health
 * 
 * @author lchbu
 * @date 2025-06-24
 */
public class PatientRecordHealth extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 健康档案id */
    private Long healthId;

    /** 患者档案id */
    @Excel(name = "患者档案id")
    private Long recordId;

    /** 患者id */
    @Excel(name = "患者id")
    private Long patientId;

    /** 研究项目id */
    @Excel(name = "研究项目id")
    private Long researchId;

    /** 随访节点id */
    @Excel(name = "随访节点id")
    private Long followId;

    /** 随访提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "随访提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date submitTime;

    /** 健康管理类型 */
    @Excel(name = "健康管理类型")
    private Integer healthManageType;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDelete;

    public void setHealthId(Long healthId) 
    {
        this.healthId = healthId;
    }

    public Long getHealthId() 
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
