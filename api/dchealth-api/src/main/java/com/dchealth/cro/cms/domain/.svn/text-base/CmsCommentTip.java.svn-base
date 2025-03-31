package com.dchealth.cro.cms.domain;

import java.util.Date;

import lombok.Data;

/**
 * 举报对象 cms_comment_tip
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-04-29
 */
@Data
public class CmsCommentTip 
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

    /** 评论内容 */
    private String content;

    /** 类型 1色情低俗 2 政治敏感  3 广告 4 令人恶心 5 违纪违法 6 其它 */
    private String tipType;

    /** 审核状态 1 待处理 2 已处理 */
    private String authStatus;

    /** 处理结果 */
    private String authSuggest;

    /** 删除标记 */
    private String delFlag;
    private Date createTime;

  
}
