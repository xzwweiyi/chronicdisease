package com.dchealth.cro.question.domain;

import lombok.Data;

import java.util.Date;

/**
 * 患教设置评测结果对象 cms_article_set_evaluation
 * 
 * @author lchbu
 * @date 2023-04-07
 */
@Data
public class CmsArticleSetEvaluation
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 患教ID */
    private Long cmsArticleId;

    /** 区间最小值 */
    private Long minValue;

    /** 区间最大值 */
    private Long maxValue;

    /** 评价结果 */
    private String evaluationResult;
    private String createBy;
    private Date createTime;

}
