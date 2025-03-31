package com.dchealth.cms.domain;


import lombok.Data;

@Data
public class CmsArticleMoveParam {


    /**
     * 转移的文章
     */
    private Long articleId;


    /**
     * 转移文章所属分类
     */
    private Long categoryId;


    /**
     * 要转移的新分类
     */
    private Long newCategoryId;


    private Long deptId;
}
