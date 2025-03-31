package com.dchealth.cms.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【请填写功能名称】对象 cms_guest_images
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsGuestImages extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 意见ID */
    @Excel(name = "意见ID")
    private Long guestbookId;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;
    
}
