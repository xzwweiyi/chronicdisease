package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_comment_tip
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsCommentTip extends BaseEntity
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

    /** 评论ID */
    @Excel(name = "评论ID")
    private Long commentId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 类型 1色情低俗 2 政治敏感  3 广告 4 令人恶心 5 违纪违法 6 其它 */
    @Excel(name = "类型 1色情低俗 2 政治敏感  3 广告 4 令人恶心 5 违纪违法 6 其它")
    private String tipType;

    /** 审核状态 1 待处理 2 已处理  */
    @Excel(name = "审核状态 1 待处理 2 已处理 ")
    private String authStatus;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String authSuggest;

    /** 删除标记 */
    private String delFlag;
    

  
}
