package com.dchealth.cro.medical.domain;

import com.dchealth.cro.common.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 【请填写功能名称】对象 patient_informed
 * 
 * @author wangpf
 * @date 2025-09-05
 */
public class PatientInformed extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    private Long personId;

    /** $column.columnComment */
    private String mobile;

    /** $column.columnComment */
    private String card;

    /** $column.columnComment */
    private String deptCode;

    /** $column.columnComment */
    private Long isAgree;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPersonId(Long personId) 
    {
        this.personId = personId;
    }

    public Long getPersonId() 
    {
        return personId;
    }
    public void setMobile(String mobile) 
    {
        this.mobile = mobile;
    }

    public String getMobile() 
    {
        return mobile;
    }
    public void setCard(String card) 
    {
        this.card = card;
    }

    public String getCard() 
    {
        return card;
    }
    public void setDeptCode(String deptCode) 
    {
        this.deptCode = deptCode;
    }

    public String getDeptCode() 
    {
        return deptCode;
    }
    public void setIsAgree(Long isAgree) 
    {
        this.isAgree = isAgree;
    }

    public Long getIsAgree() 
    {
        return isAgree;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("personId", getPersonId())
            .append("mobile", getMobile())
            .append("card", getCard())
            .append("deptCode", getDeptCode())
            .append("isAgree", getIsAgree())
            .toString();
    }
}
