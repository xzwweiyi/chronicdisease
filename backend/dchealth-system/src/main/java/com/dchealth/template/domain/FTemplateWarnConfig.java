package com.dchealth.template.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 模板预警信息对象 f_template_warn_config
 * 
 * @author lchbu
 * @date 2022-08-31
 */
@Data
public class FTemplateWarnConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 随访表单id */
    @Excel(name = "随访表单id")
    private Long templateId;

    /** 队列id */
    @Excel(name = "队列id")
    private Long researchId;

    /** 标准值 */
    @Excel(name = "标准值")
    private String standardValue;

    /** 预警公式(用于展示) */
    @Excel(name = "预警公式(用于展示)")
    private String warnFormulaTitle;

    /** 预警公式(用于计算) */
    @Excel(name = "预警公式(用于计算)")
    private String warnFormula;

    /** 属性code */
    @Excel(name = "属性code")
    private String templatePropertyCode;

    /** 表单code */
    @Excel(name = "表单id")
    private Long templateFormId;


}
