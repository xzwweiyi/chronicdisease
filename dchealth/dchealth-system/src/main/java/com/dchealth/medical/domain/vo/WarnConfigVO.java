package com.dchealth.medical.domain.vo;

import lombok.Data;

/**
 * 预警配置
 */
@Data
public class WarnConfigVO {

    /** 主键id */
    private Long id;

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