package com.dchealth.template.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 属性库属性对象 f_library_property
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
public class FLibraryProperty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

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
    private Long libraryId;

    /** 参考卷王里的数据 */
    @Excel(name = "属性")
    private String attribute;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    @Excel(name = "是否开启APP端属性填写 0未开启 1 开启 ")
    private String openProperty;

    /**
     * 排序
     */
    private Integer sortNum;

}
