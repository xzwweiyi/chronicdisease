package com.dchealth.cro.research.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 队列-患教关系表(ResearchInfoArticle)实体类
 *
 * @author makejava
 * @since 2023-06-08 13:48:46
 */
@Data
@NoArgsConstructor
public class ResearchInfoArticle implements Serializable {
    private static final long serialVersionUID = -70076420137635636L;
    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 患教id
     */
    private Long articleId;
    /**
     * 病种id
     */
    private Long diseaseId;
    /**
     * 分类id
     */
    private Long categoryId;


    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public ResearchInfoArticle(Long diseaseTypeId,Long researchId, Long categoryId,Long articleId) {
        this.diseaseId = diseaseTypeId;
        this.researchId = researchId;
        this.articleId = articleId;
        this.categoryId = categoryId;
    }
}

