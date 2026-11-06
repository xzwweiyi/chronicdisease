package com.dchealth.research.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 队列-患教关系对象 research_info_article
 * 
 * @author lchbu
 * @date 2022-08-25
 */
public class ResearchInfoArticle extends BaseEntity
{

    private static final long serialVersionUID = 1L;

    /** 队列id */
    @Excel(name = "队列id")
    private Long researchId;

    /** 患教id */
    @Excel(name = "患教id")
    private Long articleId;

    /** 病种id */
    @Excel(name = "病种id")
    private Long diseaseId;

    /** 分类id */
    private Long categoryId;

    /**
     * 0 无 1轮播 2 首页列表
     */
    private String type;

    /** 标题名称 */
    private String title;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setResearchId(Long researchId)
    {
        this.researchId = researchId;
    }

    public Long getResearchId() 
    {
        return researchId;
    }
    public void setArticleId(Long articleId) 
    {
        this.articleId = articleId;
    }

    public Long getArticleId() 
    {
        return articleId;
    }
    public void setDiseaseId(Long diseaseId) 
    {
        this.diseaseId = diseaseId;
    }

    public Long getDiseaseId() 
    {
        return diseaseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("researchId", getResearchId())
            .append("articleId", getArticleId())
            .append("diseaseId", getDiseaseId())
            .toString();
    }
}
