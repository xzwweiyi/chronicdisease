package com.dchealth.template.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 基础表单 f_library_form
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
public class FLibraryForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单id */
    private Long libraryId;

    /** 表单编码 */
    @Excel(name = "表单编码")
    private String formCode;

    /** 表单名称 */
    @Excel(name = "表单名称")
    private String formName;

    private String formNameSearch;
    //搜索2级时用到
    private String formNameSearch2;

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

    private String delFlag;

    private List<FLibraryForm> libraryForms;

    private List<FLibraryProperty> libraryProperties;

    /**
     * 排序
     */
    private Integer sortNum;

    /**
     * 表单类型1标准项2标准拍照
     */
    private String formType;

    /**
     * 是否动态表格0否1是
     */
    private String dynamic;

    /**
     * 是否折叠（0-否，1-是）
     */
    private String openCollapse;

    //条目数
    private Integer propertyNums;
    //2级数
    private Integer childNums;

    private String scoresTotalVal;
}
