package com.dchealth.cro.cms.domain;

import java.util.Date;

import lombok.Data;

/**
 * 评价黑名单对象 cms_comment_blank
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-05-01
 */
@Data
public class CmsCommentBlank 
{

    /** 编号 */
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 评论ID */
    private Long commentId;

    /** 租户ID */
    private String tenantId;

    /** 用户ID */
    private Long accountId;
    

	private String realName;
	private String cmsContent;
	private Date createTime;
    private String appType;

 
}
