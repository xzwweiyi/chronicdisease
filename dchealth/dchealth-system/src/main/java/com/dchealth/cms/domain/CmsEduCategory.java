package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宣教分类对象 cms_edu_category
 *
 * @author dchealth
 * @date 2026-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmsEduCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 分类编号 */
    private Long id;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String name;

    /** 显示排序 */
    @Excel(name = "显示排序")
    private Integer sort;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标记（N未删除 Y已删除） */
    private String delFlag;

    /** 文章数量（统计展示用，非表字段） */
    private Long articleCount;

}
