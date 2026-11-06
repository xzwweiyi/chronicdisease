package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Set;

/**
 * 【请填写功能名称】对象 cms_category
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsCategory extends BaseEntity
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    /** 编号 */
    private Long id;

    /** 父级编号 */
    @Excel(name = "父级编号")
    private Long parentId;

    /** 所有父级编号 */
    @Excel(name = "所有父级编号")
    private String parentIds;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 栏目模块 1文章 2 链接类 */
    @Excel(name = "栏目模块 1文章 2 链接类")
    private String module;

    /** 栏目名称 */
    @Excel(name = "栏目名称")
    private String name;

    /** 栏目图片 */
    @Excel(name = "栏目图片")
    private String image;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 关键字 */
    @Excel(name = "关键字")
    private String keywords;

    /** 是否显示 */
    @Excel(name = "是否显示")
    private String isShow;

    /** 医生端排序（升序） */
    @Excel(name = "医生端排序", readConverterExp = "升=序")
    private Long sort;

    /** 患者排序 */
    @Excel(name = "患者排序")
    private Long patientSort;

    /** 小程序排序 */
    @Excel(name = "小程序排序")
    private Long minSort;

    /** 是否允许评论 */
    @Excel(name = "是否允许评论")
    private String allowComment;

    /** 是否需要审核 */
    @Excel(name = "是否需要审核")
    private String isAudit;

    /** 删除标记 */
    private String delFlag;

    /** 位置1医生端轮播  2 患者端轮播  3小程序 */
    @Excel(name = "位置1医生端轮播  2 患者端轮播  3小程序")
    private String positionType;

    /** 科室id */
    private Long deptId;

    private Long createId;

    private String deptIds;


    private Long articleCount;

    @NotBlank(message="是否公共分类必填")
    private Integer commonFlag;


    private Set<String> ids;


    private String title;

    /**
     * 文章ID
     */
    private Long articleId;

    private Long categoryId;
   
}
