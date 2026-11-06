package com.dchealth.warn.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 预警模板对象 warn_template_early
 * 
 * @author lchbu
 * @date 2023-01-09
 */
public class WarnTemplateEarly extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    /** 预警类型 */
    @Excel(name = "预警类型")
    private String earlyWarnType;

    /** 是否生效1生效0失效 */
    @Excel(name = "是否生效1生效0失效")
    private String isEffect;

    /** 正常值起始 */
    @Excel(name = "正常值起始")
    private String normalStart;

    /** 正常值末 */
    @Excel(name = "正常值末")
    private String normalEnd;

    /** 正常值 */
    @Excel(name = "正常值")
    private String normalValue;

    /** 条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    @Excel(name = "条件类型1等于2大于3大于等于4小于5小于等于6区间")
    private String conditionType;

    /** 是否有条件0无1有 */
    @Excel(name = "是否有条件0无1有")
    private String isCondition;

    /** 字段id */
    @Excel(name = "字段id")
    private Long propertyId;

    /** form id */
    @Excel(name = "form id")
    private Long formId;

    /** 预警等级0无状态1异常2危机 */
    @Excel(name = "预警等级0无状态1异常2危机")
    private String earlyWarnLevel;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long templateId;

    /** 预设预警记录描述 */
//    @Excel(name = "预设预警记录描述")
    private String warnPreDesc;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setEarlyWarnType(String earlyWarnType) 
    {
        this.earlyWarnType = earlyWarnType;
    }

    public String getEarlyWarnType() 
    {
        return earlyWarnType;
    }
    public void setIsEffect(String isEffect) 
    {
        this.isEffect = isEffect;
    }

    public String getIsEffect() 
    {
        return isEffect;
    }
    public void setNormalStart(String normalStart) 
    {
        this.normalStart = normalStart;
    }

    public String getNormalStart() 
    {
        return normalStart;
    }
    public void setNormalEnd(String normalEnd) 
    {
        this.normalEnd = normalEnd;
    }

    public String getNormalEnd() 
    {
        return normalEnd;
    }
    public void setNormalValue(String normalValue) 
    {
        this.normalValue = normalValue;
    }

    public String getNormalValue() 
    {
        return normalValue;
    }
    public void setConditionType(String conditionType) 
    {
        this.conditionType = conditionType;
    }

    public String getConditionType() 
    {
        return conditionType;
    }
    public void setIsCondition(String isCondition) 
    {
        this.isCondition = isCondition;
    }

    public String getIsCondition() 
    {
        return isCondition;
    }
    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() 
    {
        return propertyId;
    }
    public void setFormId(Long formId) 
    {
        this.formId = formId;
    }

    public Long getFormId() 
    {
        return formId;
    }
    public void setEarlyWarnLevel(String earlyWarnLevel) 
    {
        this.earlyWarnLevel = earlyWarnLevel;
    }

    public String getEarlyWarnLevel() 
    {
        return earlyWarnLevel;
    }
    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }

    public String getWarnPreDesc() {
        return warnPreDesc;
    }
    public void setWarnPreDesc(String warnPreDesc) {
        this.warnPreDesc = warnPreDesc;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("earlyWarnType", getEarlyWarnType())
            .append("isEffect", getIsEffect())
            .append("normalStart", getNormalStart())
            .append("normalEnd", getNormalEnd())
            .append("normalValue", getNormalValue())
            .append("createTime", getCreateTime())
            .append("conditionType", getConditionType())
            .append("isCondition", getIsCondition())
            .append("propertyId", getPropertyId())
            .append("formId", getFormId())
            .append("earlyWarnLevel", getEarlyWarnLevel())
            .append("templateId", getTemplateId())
            .append("warnPreDesc", getWarnPreDesc())
            .toString();
    }
}
