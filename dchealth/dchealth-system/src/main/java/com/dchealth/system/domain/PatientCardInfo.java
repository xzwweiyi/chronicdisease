package com.dchealth.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 患者主号管理对象 patient_card_info
 *
 * @author lchbu
 * @date 2024-10-31
 */
public class PatientCardInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 身份证 */
    @Excel(name = "身份证")
    private String idCard;

    /** 患者id */
    @Excel(name = "患者id")
    private String personId;

    /** 是否主号 */
    @Excel(name = "是否主号")
    private Integer patientMasterNo;

    /** 更新状态 */
    @Excel(name = "更新状态")
    private Integer updateStatus;

    /** 更新状态 */
    private String name;

    /** 患者id生成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "患者id生成时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date personIdGenerateTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setIdCard(String idCard)
    {
        this.idCard = idCard;
    }

    public String getIdCard()
    {
        return idCard;
    }
    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getPersonId()
    {
        return personId;
    }
    public void setPatientMasterNo(Integer patientMasterNo)
    {
        this.patientMasterNo = patientMasterNo;
    }

    public Integer getPatientMasterNo()
    {
        return patientMasterNo;
    }
    public void setUpdateStatus(Integer updateStatus)
    {
        this.updateStatus = updateStatus;
    }

    public Integer getUpdateStatus()
    {
        return updateStatus;
    }
    public void setPersonIdGenerateTime(Date personIdGenerateTime)
    {
        this.personIdGenerateTime = personIdGenerateTime;
    }

    public Date getPersonIdGenerateTime()
    {
        return personIdGenerateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("idCard", getIdCard())
                .append("personId", getPersonId())
                .append("patientMasterNo", getPatientMasterNo())
                .append("updateStatus", getUpdateStatus())
                .append("personIdGenerateTime", getPersonIdGenerateTime())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("createBy", getCreateBy())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
