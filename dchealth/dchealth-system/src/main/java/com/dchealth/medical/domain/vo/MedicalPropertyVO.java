package com.dchealth.medical.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 随访表单属性
 */
@Data
public class MedicalPropertyVO extends PropertyVO{

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

    /**
     * 结果
     */
    private String propertyValue;

    /**
     * 图片
     */
    private List<String> imageUrls;

    /**
     * 其他内容
     */
    private String other;

}
