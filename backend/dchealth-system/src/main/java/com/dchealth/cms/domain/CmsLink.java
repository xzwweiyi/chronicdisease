package com.dchealth.cms.domain;

import java.util.Date;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_link
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsLink extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 栏目编号 */
    @Excel(name = "栏目编号")
    private Long categoryId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 链接名称 */
    @Excel(name = "链接名称")
    private String title;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 链接图片 */
    @Excel(name = "链接图片")
    private String image;

    /** 链接地址 */
    @Excel(name = "链接地址")
    private String href;

    /** 权重，越大越靠前 */
    @Excel(name = "权重，越大越靠前")
    private Long weight;

    /** 权重期限 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "权重期限", width = 30, dateFormat = "yyyy-MM-dd")
    private Date weightDate;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updateDate;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String remarks;

    
}
