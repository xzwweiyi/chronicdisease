package com.dchealth.cro.cms.domain;

import java.util.Date;

import lombok.Data;

/**
 * 资讯收藏对象 cms_collection
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-17
 */
@Data
public class CmsCollection
{

    /** null */
    private Long id;

    /** 用户ID */
    private Long accountId;

    /** 租户ID */
    private String tenantId;

    /** 资讯ID */
    private Long articleId;

    private Date createTime;
}
