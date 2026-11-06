package com.dchealth.cms.domain;

import java.util.Date;
import java.util.Map;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_comment_blank
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsCommentBlank  extends BaseEntity
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
    /** 搜索值 */
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 请求参数 */
    private Map<String, Object> params;

   
}
