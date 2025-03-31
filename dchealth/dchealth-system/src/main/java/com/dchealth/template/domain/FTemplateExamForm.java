package com.dchealth.template.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 单模板-自评量对象 f_template_exam_form
 * 
 * @author lchbu
 * @date 2022-11-11
 */
public class FTemplateExamForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单id */
    private Long templateFormId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long templateId;

    /** 表单编码 */
    @Excel(name = "表单编码")
    private String templateCode;

    /** 表单名称 */
    @Excel(name = "表单名称")
    private String templateName;

    /** 父id */
    @Excel(name = "父id")
    private Long parentId;

    /** 是否开启APP端图片上传 0未开启 1 开启  */
    @Excel(name = "是否开启APP端图片上传 0未开启 1 开启 ")
    private String openImage;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    @Excel(name = "是否开启APP端属性填写 0未开启 1 开启 ")
    private String openProperty;

    /** 是否开启动态添加 0未开启 1 开启  */
    @Excel(name = "是否开启动态添加 0未开启 1 开启 ")
    private String openMultiple;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String attribute;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortNum;

    /** 0模块1标准项2拍照项3自评量表4预警填报 */
    @Excel(name = "0模块1标准项2拍照项3自评量表4预警填报")
    private String formType;

    /** 是否动态表格0否1是 */
    @Excel(name = "是否动态表格0否1是")
    private String dynamic;

    public void setTemplateFormId(Long templateFormId) 
    {
        this.templateFormId = templateFormId;
    }

    public Long getTemplateFormId() 
    {
        return templateFormId;
    }
    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }
    public void setTemplateCode(String templateCode) 
    {
        this.templateCode = templateCode;
    }

    public String getTemplateCode() 
    {
        return templateCode;
    }
    public void setTemplateName(String templateName) 
    {
        this.templateName = templateName;
    }

    public String getTemplateName() 
    {
        return templateName;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setOpenImage(String openImage) 
    {
        this.openImage = openImage;
    }

    public String getOpenImage() 
    {
        return openImage;
    }
    public void setOpenProperty(String openProperty) 
    {
        this.openProperty = openProperty;
    }

    public String getOpenProperty() 
    {
        return openProperty;
    }
    public void setOpenMultiple(String openMultiple) 
    {
        this.openMultiple = openMultiple;
    }

    public String getOpenMultiple() 
    {
        return openMultiple;
    }
    public void setAttribute(String attribute) 
    {
        this.attribute = attribute;
    }

    public String getAttribute() 
    {
        return attribute;
    }
    public void setSortNum(Long sortNum) 
    {
        this.sortNum = sortNum;
    }

    public Long getSortNum() 
    {
        return sortNum;
    }
    public void setFormType(String formType) 
    {
        this.formType = formType;
    }

    public String getFormType() 
    {
        return formType;
    }
    public void setDynamic(String dynamic) 
    {
        this.dynamic = dynamic;
    }

    public String getDynamic() 
    {
        return dynamic;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateFormId", getTemplateFormId())
            .append("templateId", getTemplateId())
            .append("templateCode", getTemplateCode())
            .append("templateName", getTemplateName())
            .append("parentId", getParentId())
            .append("openImage", getOpenImage())
            .append("openProperty", getOpenProperty())
            .append("openMultiple", getOpenMultiple())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("attribute", getAttribute())
            .append("sortNum", getSortNum())
            .append("formType", getFormType())
            .append("dynamic", getDynamic())
            .toString();
    }
}
