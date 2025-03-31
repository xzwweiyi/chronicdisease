package com.dchealth.cro.cms.domain;

import java.util.Date;

import lombok.Data;

/**
 * 评论对象 cms_comment
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-18
 */
@Data
public class CmsComment 
{

    /** 编号 */
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 租户ID */
    private String tenantId;

    /** 用户ID */
    private Long accountId;

    /** 评论内容 */
    private String content;

    /** 删除标记 */
    private String delFlag;
    
    private Date createTime;

    private String appType;
}
