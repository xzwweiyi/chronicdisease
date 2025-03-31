package com.dchealth.cro.form.domain;

import java.util.List;

import lombok.Data;

/**
 * 属性对象 f_medical_property
 * 
 * @author ruoyi
 * @date 2022-09-09
 */
@Data
public class FMedicalProperty
{

    /**  */
    private Long id;

    /** 属性编码 */
    private String propertyCode;

    /** 属性名称 */
    private String propertyName;

    /** 属性父id */
    private Long parentId;

    /** 属性类型 */
    private String propertyType;

    /** 字典表类型 */
    private String dictType;

    /** 模板ID */
    private Long medicalFormId;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    private String openProperty;

    /** 参考卷王里的数据 */
    private String attribute;

    /** 随访表单id */
    private Long followId;

    /** 排序 */
    private Integer sortNum;

    private String checked;

    //为展示自评量表新添加的字段
    private Integer subjectScores;
    private List<FMedicalExamItem> itemList;

}
