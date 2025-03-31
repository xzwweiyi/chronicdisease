package com.dchealth.template.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 属性题目选项-自评量对象 f_template_exam_item
 * 
 * @author lchbu
 * @date 2022-11-11
 */
public class FTemplateExamItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 题目属性ID */
    @Excel(name = "题目属性ID")
    private Long propertyId;

    /** 下拉选项值 */
    @Excel(name = "下拉选项值")
    private String itemValue;

    /** 选项标题 */
    @Excel(name = "选项标题")
    private String itemLabel;

    /** 排序 */
    @Excel(name = "排序")
    private Integer orderNum;

    /** 分数 */
    @Excel(name = "分数")
    private Long itemScores;

    /** 模板ID */
    private Long templateFormId;

    public Long getTemplateFormId() {
        return templateFormId;
    }

    public void setTemplateFormId(Long templateFormId) {
        this.templateFormId = templateFormId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPropertyId(Long propertyId) 
    {
        this.propertyId = propertyId;
    }

    public Long getPropertyId() 
    {
        return propertyId;
    }
    public void setItemValue(String itemValue) 
    {
        this.itemValue = itemValue;
    }

    public String getItemValue() 
    {
        return itemValue;
    }
    public void setItemLabel(String itemLabel) 
    {
        this.itemLabel = itemLabel;
    }

    public String getItemLabel() 
    {
        return itemLabel;
    }
    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum()
    {
        return orderNum;
    }
    public void setItemScores(Long itemScores) 
    {
        this.itemScores = itemScores;
    }

    public Long getItemScores() 
    {
        return itemScores;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("propertyId", getPropertyId())
            .append("itemValue", getItemValue())
            .append("itemLabel", getItemLabel())
            .append("orderNum", getOrderNum())
            .append("createTime", getCreateTime())
            .append("itemScores", getItemScores())
            .toString();
    }
}
