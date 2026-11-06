package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_article_data
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsArticleData extends BaseEntity
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 文章内容 */
    @Excel(name = "文章内容")
    private String content;

    /** 文章来源 */
    @Excel(name = "文章来源")
    private String copyFrom;

    /** 相关文章 */
    @Excel(name = "相关文章")
    private String relation;

    /** 是否允许评论 */
    @Excel(name = "是否允许评论")
    private String allowComment;

  
}
