package com.dchealth.cro.form.domain.form;

import lombok.Data;

/**
 * 属性值
 */
@Data
public class FMedicalPropertyForm {

//    private Long id;

    /** 属性编码 */
    private String propertyCode;

    /** 属性类型 */
    private String propertyType;

    /** 模板ID */
    private Long medicalFormId;

    /**
     * 表单类型
     */
    private String medicalFormType;

    /**
     * 属性值
     */
    private String propertyValue;

    /**
     * 属性ID
     */
    private Long propertyId;

    /**
     * 行号
     */
    private Integer number;

    /** 是否动态表格0否1是 */
    private String dynamic;

    /**
     * 其它属性值
     */
    private String other;


}
