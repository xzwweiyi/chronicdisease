package com.dchealth.cro.cms.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 文章对象 cms_article
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
@Data
public class CmsArticle 
{

    /** 编号 */
    private Long id;

    /** 栏目编号 */
    private Long categoryId;
    
    private String tenantId;

    /** 标题 */
    private String title;

    /** 文章链接 */
    private String link;

    //上传方式1本地上传2转发3云视频
    private String uploadType;
    //转发路径
    private String forwardLink;

    /** 文章图片 */
    private String image;

    /** 关键字 */
    private String keywords;

    /** 描述、摘要 */
    private String description;
    
    private Integer listOrderNum;
    private String listShow;

    /** 备注信息 */
    private String remark;

    /** 删除标记 */
    private String delFlag;
    
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

    /** 文章内容 */
    private String content;

    /** 文章来源 */
    private String copyFrom;

    /** 相关文章 */
    private String relation;

    /** 是否允许评论 */
    private String allowComment;
    
    private String videoUrl;
    
    private String videoImage;
    /** 位置 */
    private String positionType;

    /** 问卷排序值 */
    private Integer quesNum;
   
}
