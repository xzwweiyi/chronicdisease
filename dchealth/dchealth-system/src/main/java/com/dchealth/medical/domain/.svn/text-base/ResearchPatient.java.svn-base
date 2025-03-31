package com.dchealth.medical.domain;

import com.dchealth.base.service.BaseRef;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 【请填写功能名称】对象 research_patient
 *
 * @author ruoyi
 * @date 2022-06-15
 */
public class ResearchPatient extends BaseEntity implements BaseRef
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 患者基本信息id */
    @Excel(name = "患者基本信息id")
    private Long basePatientId;

    /** 患者id */
    @Excel(name = "患者id")
    private Long patientId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long researchId;
    
    private String personId;
    
    
    

    public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBasePatientId(Long basePatientId)
    {
        this.basePatientId = basePatientId;
    }

    public Long getBasePatientId()
    {
        return basePatientId;
    }
    public void setResearchId(Long researchId)
    {
        this.researchId = researchId;
    }

    public Long getResearchId()
    {
        return researchId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("basePatientId", getBasePatientId())
                .append("researchId", getResearchId())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("patientId", getPatientId())
                .toString();
    }

    @Override
    public List<Long> getList() {
        return list;
    }
}

