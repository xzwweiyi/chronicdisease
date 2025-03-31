package com.dchealth.template.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * CRF库模板分类对象 f_template_category
 * 
 * @author lchbu
 * @date 2022-08-30
 */
@Data
public class FTemplateCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板分类表 */
    private Long templateCategoryId;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String templateCategoryName;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long deptId;

    private String deptIds;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;

    /**
     * 模板数量
     */
    private int templateNum;

    private Long createId;

    private Integer commonFlag;

}
