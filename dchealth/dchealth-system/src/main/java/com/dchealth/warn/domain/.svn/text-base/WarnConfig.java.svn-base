package com.dchealth.warn.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 预警信息对象 warn_config
 * 
 * @author lchbu
 * @date 2022-08-30
 */
public class WarnConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 随访表单id */
    @Excel(name = "随访表单id")
    private Long followId;

    /** 队列id */
    @Excel(name = "队列id")
    private Long researchId;

    /** 标准值 */
    @Excel(name = "标准值")
    private String standardValue;

    /** 预警公式(用于展示) */
    @Excel(name = "预警公式(用于展示)")
    private String warnFormulaTitle;

    /** 预警公式(用于计算) */
    @Excel(name = "预警公式(用于计算)")
    private String warnFormula;

    /** 属性code */
    private String medicalPropertyCode;

    /** 表单id */
    private Long medicalFormId;

    public String getMedicalPropertyCode() {
        return medicalPropertyCode;
    }

    public void setMedicalPropertyCode(String medicalPropertyCode) {
        this.medicalPropertyCode = medicalPropertyCode;
    }

    public Long getMedicalFormId() {
        return medicalFormId;
    }

    public void setMedicalFormId(Long medicalFormId) {
        this.medicalFormId = medicalFormId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFollowId(Long followId) 
    {
        this.followId = followId;
    }

    public Long getFollowId() 
    {
        return followId;
    }
    public void setResearchId(Long researchId) 
    {
        this.researchId = researchId;
    }

    public Long getResearchId() 
    {
        return researchId;
    }
    public void setStandardValue(String standardValue) 
    {
        this.standardValue = standardValue;
    }

    public String getStandardValue() 
    {
        return standardValue;
    }
    public void setWarnFormulaTitle(String warnFormulaTitle) 
    {
        this.warnFormulaTitle = warnFormulaTitle;
    }

    public String getWarnFormulaTitle() 
    {
        return warnFormulaTitle;
    }
    public void setWarnFormula(String warnFormula) 
    {
        this.warnFormula = warnFormula;
    }

    public String getWarnFormula() 
    {
        return warnFormula;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("followId", getFollowId())
            .append("researchId", getResearchId())
            .append("standardValue", getStandardValue())
            .append("warnFormulaTitle", getWarnFormulaTitle())
            .append("warnFormula", getWarnFormula())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
