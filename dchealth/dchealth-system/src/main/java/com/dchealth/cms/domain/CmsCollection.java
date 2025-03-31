package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_collection
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsCollection extends BaseEntity
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 资讯ID */
    @Excel(name = "资讯ID")
    private Long articleId;
   

   
}
