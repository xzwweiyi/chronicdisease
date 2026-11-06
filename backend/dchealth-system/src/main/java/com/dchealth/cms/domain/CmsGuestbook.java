package com.dchealth.cms.domain;

import java.util.List;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 反馈管理对象 cms_guestbook
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsGuestbook extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    /** 留言内容 */
    @Excel(name = "留言内容")
    private String content;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    @Excel(name = "用户名")
    private String realName;

    /** 姓名 */
    @Excel(name = "姓名")
    private String userName;

    /** 电话 */
    @Excel(name = "电话")
    private String mobile;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 删除标记 */
    private String delFlag;

    private String positionType;

    private List<String> guestImages;

    private  List<CmsGuestImages> cmsGuestImagesList;
    
    

    
}
