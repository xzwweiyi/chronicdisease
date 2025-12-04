package com.dchealth.template.domain;

import com.dchealth.common.core.domain.BaseEntity;
import com.dchealth.medical.domain.PatientRecordFiles;
import lombok.Data;

import java.util.List;

/**
 * 单模板对象 f_medical_form
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
public class FMedicalForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单id */
    private Long medicalFormId;

    /** 表单编码 */
    private String medicalCode;

    /** 表单名称 */
    private String medicalName;

    /** 父id */
    private Long parentId;

    /** 是否开启APP端图片上传 0未开启 1 开启  */
    private String openImage;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    private String openProperty;

    /** 是否开启动态添加 0未开启 1 开启  */
    private String openMultiple;

    /** $column.columnComment */
    private String attribute;

    /**
     * 1标准项2拍照项3其他 自评量表 ？
     */
    private String formType;

    //总分
    private String scoresTotalVal;

    private Long followId;

    private List<FMedicalForm> medicalFormList;

    private List<FMedicalProperty> medicalProperties;

    /**
     * 标准项的组件id
     */
    private Long baseFormId;

    private Long articleId;

    private String formSource;
    
    /**
     * 是否动态表格0否1是
     */
    private String dynamic;

    private List<PatientRecordFiles> patientRecordFiles;

    /**
     * 问卷信息
     */
    private List<FMedicalExamSubject> subjectList;

    private Integer propertyNum;

    private Integer edcNum;

    private String lockFlag;//锁库

    /**
     * 是否折叠（0-否，1-是）
     */
    private String openCollapse;

    private Integer sortNum;


    /**
     * 0 默认 1营养分析 2康复运动 3 不限
     */
    private Integer selfEvaluationType;


    private Integer healthManageType;


}
