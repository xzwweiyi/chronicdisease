package com.dchealth.cro.cms.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文章详对象 cms_article_data
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-06
 */
public class CmsArticleData 
{

    /** 编号 */
    private Long id;

    /** 文章内容 */
    private String content;

    /** 文章来源 */
    private String copyFrom;

    /** 相关文章 */
    private String relation;

    /** 是否允许评论 */
    private String allowComment;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setCopyFrom(String copyFrom) 
    {
        this.copyFrom = copyFrom;
    }

    public String getCopyFrom() 
    {
        return copyFrom;
    }
    public void setRelation(String relation) 
    {
        this.relation = relation;
    }

    public String getRelation() 
    {
        return relation;
    }
    public void setAllowComment(String allowComment) 
    {
        this.allowComment = allowComment;
    }

    public String getAllowComment() 
    {
        return allowComment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("content", getContent())
            .append("copyFrom", getCopyFrom())
            .append("relation", getRelation())
            .append("allowComment", getAllowComment())
            .toString();
    }
}
