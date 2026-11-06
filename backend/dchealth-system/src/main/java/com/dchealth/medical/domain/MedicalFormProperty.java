package com.dchealth.medical.domain;

import com.dchealth.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 随访单属性对象 medical_form_property
 * 
 * @author wangpf
 * @date 2026-01-08
 */
@Data
public class MedicalFormProperty
{
    private static final long serialVersionUID = 1L;

    /** ID（主键） */
    private Long id;

    /** 记录ID（主键） */
    @Excel(name = "记录ID", readConverterExp = "主=键")
    private Long recordId;

    /** 患者ID */
    @Excel(name = "患者ID")
    private Long patientId;

    /** 研究ID */
    @Excel(name = "研究ID")
    private Long researchId;

    private String personId;

    private String researchName;

    /** 随访ID */
    @Excel(name = "随访ID")
    private Long followId;

    /** 医疗表单ID */
    @Excel(name = "医疗表单ID")
    private Long medicalFormId;

    /** 医疗编码 */
    @Excel(name = "医疗编码")
    private String medicalCode;

    /** 表单类型 */
    @Excel(name = "表单类型")
    private String formType;

    /** 属性编码 */
    @Excel(name = "属性编码")
    private String propertyCode;

    /** 属性类型（如Checkbox/Radio等） */
    @Excel(name = "属性类型", readConverterExp = "如=Checkbox/Radio等")
    private String propertyType;

    /** 随访名称 */
    @Excel(name = "随访名称")
    private String name;

    /** 预警状态 */
    @Excel(name = "预警状态")
    private String warnStatus;

    /** 缺失类型 */
    @Excel(name = "缺失类型")
    private String loseType;

    /** 缺失原因 */
    @Excel(name = "缺失原因")
    private String loseReason;

    /** 表单项名称 */
    @Excel(name = "表单项名称")
    private String medicalName;

    /** 表单项属性 */
    @Excel(name = "表单项属性")
    private String medicalAttribute;

    /** 父级ID */
    @Excel(name = "父级ID")
    private Long parentId;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String propertyName;

    /** 属性特征 */
    @Excel(name = "属性特征")
    private String propertyAttribute;

    /** 标签/选项名称 */
    @Excel(name = "标签/选项名称")
    private String label;

    /** 值/选项值 */
    @Excel(name = "值/选项值")
    private String value;

    /** 其他补充信息 */
    @Excel(name = "其他补充信息")
    private String other;

    /** 属性ID */
    @Excel(name = "属性ID")
    private Long propertyId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;


    private List<Long> propertyIds;

}
