package com.dchealth.template.domain;

import lombok.Data;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 单模板对象 f_template_form
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
public class FTemplateForm extends BaseEntity
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

    private List<FTemplateForm> templateForms;

    private List<FTemplateProperty> templateProperties;
    
    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 表单属性1标准项2拍照项
     */
    private String formType;

    //总分
    private String scoresTotalVal;

    /**
     * 标准项的组件id
     */
    private Long baseFormId;

    /**
     * 是否动态表格0否1是
     */
    private String dynamic;

    /**
     * 项目数量
     */
    private Integer propertyNum;

    private Integer edcNum;

    private Integer healthManageType;
}
