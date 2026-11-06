package com.dchealth.template.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 单模板设置评测结果对象 f_template_form_set_evaluation
 * 
 * @author lchbu
 * @date 2023-04-04
 */
@Data
public class FTemplateFormSetEvaluation extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 表单模板ID */
    @Excel(name = "表单模板ID")
    private Long templateFormId;

    /** 区间最小值 */
    @Excel(name = "区间最小值")
    private Float minValue;

    /** 区间最大值 */
    @Excel(name = "区间最大值")
    private Float maxValue;

    /** 评价结果 */
    @Excel(name = "评价结果")
    private String evaluationResult;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateFormId", getTemplateFormId())
            .append("minValue", getMinValue())
            .append("maxValue", getMaxValue())
            .append("evaluationResult", getEvaluationResult())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .toString();
    }
}
