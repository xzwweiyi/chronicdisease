package com.dchealth.template.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 属性-自评量对象 f_template_exam_subject
 * 
 * @author lchbu
 * @date 2022-11-11
 */
public class FTemplateExamSubject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long templateId;

    /** 属性编码 */
    @Excel(name = "属性编码")
    private String propertyCode;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String propertyName;

    /** 属性父id */
    @Excel(name = "属性父id")
    private Long parentId;

    /** 属性类型 */
    @Excel(name = "属性类型")
    private String propertyType;

    /** 字典表类型 */
    @Excel(name = "字典表类型")
    private String dictType;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long templateFormId;

    /** 参考卷王里的数据 */
    @Excel(name = "参考卷王里的数据")
    private String attribute;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    @Excel(name = "是否开启APP端属性填写 0未开启 1 开启 ")
    private String openProperty;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortNum;

    /** 选型信息 */
    private List<FTemplateExamItem> items;

    public List<FTemplateExamItem> getItems() {
        return items;
    }

    public void setItems(List<FTemplateExamItem> items) {
        this.items = items;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }
    public void setPropertyCode(String propertyCode) 
    {
        this.propertyCode = propertyCode;
    }

    public String getPropertyCode() 
    {
        return propertyCode;
    }
    public void setPropertyName(String propertyName) 
    {
        this.propertyName = propertyName;
    }

    public String getPropertyName() 
    {
        return propertyName;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setPropertyType(String propertyType) 
    {
        this.propertyType = propertyType;
    }

    public String getPropertyType() 
    {
        return propertyType;
    }
    public void setDictType(String dictType) 
    {
        this.dictType = dictType;
    }

    public String getDictType() 
    {
        return dictType;
    }
    public void setTemplateFormId(Long templateFormId) 
    {
        this.templateFormId = templateFormId;
    }

    public Long getTemplateFormId() 
    {
        return templateFormId;
    }
    public void setAttribute(String attribute) 
    {
        this.attribute = attribute;
    }

    public String getAttribute() 
    {
        return attribute;
    }
    public void setOpenProperty(String openProperty) 
    {
        this.openProperty = openProperty;
    }

    public String getOpenProperty() 
    {
        return openProperty;
    }
    public void setSortNum(Integer sortNum)
    {
        this.sortNum = sortNum;
    }

    public Integer getSortNum()
    {
        return sortNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateId", getTemplateId())
            .append("propertyCode", getPropertyCode())
            .append("propertyName", getPropertyName())
            .append("parentId", getParentId())
            .append("propertyType", getPropertyType())
            .append("dictType", getDictType())
            .append("templateFormId", getTemplateFormId())
            .append("attribute", getAttribute())
            .append("openProperty", getOpenProperty())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("sortNum", getSortNum())
            .toString();
    }
}
