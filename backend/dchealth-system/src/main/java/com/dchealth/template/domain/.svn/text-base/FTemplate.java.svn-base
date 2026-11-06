package com.dchealth.template.domain;

import lombok.Data;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 模板对象 f_template
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
public class FTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long templateId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String templateName;

    /** 模板类型 单个组件  整个表单   */
    @Excel(name = "模板类型 单个组件  整个表单  ")
    private String templateType;

    /** 是否删除 0未删除 1 删除 */
    @Excel(name = "是否删除 0未删除 1 删除")
    private String isDeleted;

    /** 共享类型1私有库 2 公共库 */
    @Excel(name = "共享类型1私有库 2 公共库")
    private String shareType;

    private List<FTemplateForm> templateForms;

    /**
     * 排序
     */
    private Integer sortNum;

    private Long templateCategoryId;

    /**
     * 模块数量
     */
    private Integer formNum;

    /**
     * 项目数量
     */
    private Integer propertyNum;

    private Long createId;

    private Long deptId;

    private String deptIds;

    /**
     * 0 未加入、展示加入科室库  1已经加入 展示移除科室库
     */
    private Integer isJoin;

    /**
     * 1 展示移除 0不展示
     *
     */
    private Integer isShowRemove;
}
