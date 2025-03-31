package com.dchealth.cro.cms.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 栏目对象 cms_category
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Data
public class CmsCategory 
{

    /** 编号 */
    private Long id;

    /** 父级编号 */
    private Long parentId;

    /** 所有父级编号 */
    private String parentIds;

    /** 渠道 */
    private String channel;

    /** 栏目模块 */
    private String module;

    /** 栏目名称 */
    private String name;

    /** 栏目图片 */
    private String image;

    /** 描述 */
    private String description;

    /** 关键字 */
    private String keywords;

    /** 排序（升序） */
    private Integer sort;

    /** 是否允许评论 */
    private String allowComment;

    /** 是否需要审核 */
    private String isAudit;


    /** 备注信息 */
    private String remark;

    /** 删除标记 */
    private String delFlag;
    
    private String tenantId;
    
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
    
    /** 位置 */
    private String positionType;

  
    
}
