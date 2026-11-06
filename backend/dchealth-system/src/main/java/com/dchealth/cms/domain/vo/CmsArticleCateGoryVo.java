package com.dchealth.cms.domain.vo;

import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
@Data
@EqualsAndHashCode(callSuper=false)
public class CmsArticleCateGoryVo extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;


    private String title;


    /** 类型（1-文章，2-问卷） */
    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 栏目编号 */
    private Long categoryId;

    private String categoryName;

    /** 标题 */
    private Long parentId;

    //科室id集合
    private String parentIds;

    //链接
    private String forwardLink;

    //视频链接
    private String videoUrl;

    //文章内容
    private String content;

    //上传类型  1、文章 2、转发  3、公众号
    private String uploadType;



}
