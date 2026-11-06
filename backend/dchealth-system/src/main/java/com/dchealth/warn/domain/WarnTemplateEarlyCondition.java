package com.dchealth.warn.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 预警模板子表对象 warn_template_early_condition
 * 
 * @author lchbu
 * @date 2023-01-09
 */
public class WarnTemplateEarlyCondition extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 预警主表ID */
    @Excel(name = "预警主表ID")
    private Long templateEarlyWarnId;

    /** 条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    @Excel(name = "条件类型1等于2大于3大于等于4小于5小于等于6区间")
    private String conditionType;

    /** 预警值 */
    @Excel(name = "预警值")
    private String earlyWarnValue;

    /** 预警值起始值 */
    @Excel(name = "预警值起始值")
    private String earlyWarnStart;

    /** 预警值终止值 */
    @Excel(name = "预警值终止值")
    private String earlyWarnEnd;

    /** 字段id */
    @Excel(name = "字段id")
    private Long propertyId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long templateId;

    /** 预警等级0无状态1异常2危机 */
    private String earlyWarnLevel;
    /** 性别 */
    private String sexCondition;
    /** 年龄条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    private String ageConditionType;
    /** 年龄预警值 */
    private String ageEarlyWarnValue;
    /** 年龄预警值起始值 */
    private String ageEarlyWarnStart;
    /** 年龄预警值终止值 */
    private String ageEarlyWarnEnd;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTemplateEarlyWarnId(Long templateEarlyWarnId) 
    {
        this.templateEarlyWarnId = templateEarlyWarnId;
    }

    public Long getTemplateEarlyWarnId() 
    {
        return templateEarlyWarnId;
    }
    public void setConditionType(String conditionType) 
    {
        this.conditionType = conditionType;
    }

    public String getConditionType() 
    {
        return conditionType;
    }
    public void setEarlyWarnValue(String earlyWarnValue) 
    {
        this.earlyWarnValue = earlyWarnValue;
    }

    public String getEarlyWarnValue() 
    {
        return earlyWarnValue;
    }
    public void setEarlyWarnStart(String earlyWarnStart) 
    {
        this.earlyWarnStart = earlyWarnStart;
    }

    public String getEarlyWarnStart() 
    {
        return earlyWarnStart;
    }
    public void setEarlyWarnEnd(String earlyWarnEnd) 
    {
        this.earlyWarnEnd = earlyWarnEnd;
    }

    public String getEarlyWarnEnd() 
    {
        return earlyWarnEnd;
    }
    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() 
    {
        return propertyId;
    }
    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateEarlyWarnId", getTemplateEarlyWarnId())
            .append("conditionType", getConditionType())
            .append("earlyWarnValue", getEarlyWarnValue())
            .append("earlyWarnStart", getEarlyWarnStart())
            .append("earlyWarnEnd", getEarlyWarnEnd())
            .append("createTime", getCreateTime())
            .append("propertyId", getPropertyId())
            .append("templateId", getTemplateId())
            .toString();
    }

    public String getEarlyWarnLevel() {
        return earlyWarnLevel;
    }

    public void setEarlyWarnLevel(String earlyWarnLevel) {
        this.earlyWarnLevel = earlyWarnLevel;
    }

    public String getSexCondition() {
        return sexCondition;
    }

    public void setSexCondition(String sexCondition) {
        this.sexCondition = sexCondition;
    }

    public String getAgeConditionType() {
        return ageConditionType;
    }

    public void setAgeConditionType(String ageConditionType) {
        this.ageConditionType = ageConditionType;
    }

    public String getAgeEarlyWarnValue() {
        return ageEarlyWarnValue;
    }

    public void setAgeEarlyWarnValue(String ageEarlyWarnValue) {
        this.ageEarlyWarnValue = ageEarlyWarnValue;
    }

    public String getAgeEarlyWarnStart() {
        return ageEarlyWarnStart;
    }

    public void setAgeEarlyWarnStart(String ageEarlyWarnStart) {
        this.ageEarlyWarnStart = ageEarlyWarnStart;
    }

    public String getAgeEarlyWarnEnd() {
        return ageEarlyWarnEnd;
    }

    public void setAgeEarlyWarnEnd(String ageEarlyWarnEnd) {
        this.ageEarlyWarnEnd = ageEarlyWarnEnd;
    }
}
