package com.dchealth.cro.cms.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 举报对象 cms_comment_tip
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-04-29
 */
@Data
public class CmsCommentTipForm 
{


    /** 文章ID */
	  @NotNull(message = "文章ID不能为空")
    private Long articleId;

    /** 评论ID */
    @NotNull(message = "评论ID不能为空")
    private Long commentId;

    /** 租户ID */
    private String tenantId;

    /** 用户ID */
    @NotNull(message = "当前用户ID不能为空")
    private Long accountId;

    /** 评论内容 */
    private String content;

    /** 类型 1色情低俗 2 政治敏感  3 广告 4 令人恶心 5 违纪违法 6 其它 */
    @NotNull(message =  "类型不能为空")
    private String tipType;

  

  
}
