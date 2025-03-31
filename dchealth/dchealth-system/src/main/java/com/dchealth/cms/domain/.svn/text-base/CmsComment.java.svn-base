package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_comment
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsComment  extends BaseEntity
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 编号 */
    private Long id;

    /** 文章ID */
    @Excel(name = "文章ID")
    private Long articleId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 删除标记 */
    private String delFlag;

    @Excel(name = "用户名")
    private String realName;

    @Excel(name = "文章")
    private String title;
    /** 搜索值 */
    private String searchValue;

   

   
}
