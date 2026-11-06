package com.dchealth.cms.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 患教设置评测结果对象 cms_article_set_evaluation
 * 
 * @author lchbu
 * @date 2023-04-07
 */
@Data
public class CmsArticleSetEvaluation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 患教ID */
    @Excel(name = "患教ID")
    private Long cmsArticleId;

    /** 区间最小值 */
    @Excel(name = "区间最小值")
    private Long minValue;

    /** 区间最大值 */
    @Excel(name = "区间最大值")
    private Long maxValue;

    /** 评价结果 */
    @Excel(name = "评价结果")
    private String evaluationResult;

    public void setId(Long id) 
    {
        this.id = id;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cmsArticleId", getCmsArticleId())
            .append("minValue", getMinValue())
            .append("maxValue", getMaxValue())
            .append("evaluationResult", getEvaluationResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
