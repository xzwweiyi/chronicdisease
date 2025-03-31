package com.dchealth.cro.warn.domain;

import lombok.Data;

/**
 * 预警信息对象 warn_config
 * 
 * @author lchbu
 * @date 2022-08-30
 */
@Data
public class WarnConfig
{

    /** 主键id */
    private Long id;

    /** 随访表单id */
    private Long followId;

    /** 队列id */
    private Long researchId;

    /** 标准值 */
    private String standardValue;

    /** 预警公式(用于展示) */
    private String warnFormulaTitle;

    /** 预警公式(用于计算) */
    private String warnFormula;

    /** 属性code */
    private String medicalPropertyCode;

    /** 表单id */
    private Long medicalFormId;

}
