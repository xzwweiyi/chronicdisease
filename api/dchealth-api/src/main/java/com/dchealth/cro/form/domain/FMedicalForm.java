package com.dchealth.cro.form.domain;

import java.util.Date;
import java.util.List;

import com.dchealth.cro.warn.domain.WarnConfig;

import lombok.Data;

/**
 * 单模板对象 f_medical_form
 * 
 * @author ruoyi
 * @date 2022-09-09
 */
@Data
public class FMedicalForm
{

    /** 表单id */
    private Long medicalFormId;

    /** 随访表单id */
    private Long followId;

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

    /**  */
    private String attribute;

    /** 版本号 */
    private String versionCode;

    /** 0模块1标准项2拍照项3自评量表4预警填报 */
    private String formType;

    /** cms问卷 */
    private Long articleId;

    /** 表单来源1CRF,2CMS */
    private String formSource;

    /** 是否动态表格0否1是 */
    private String dynamic;

    private Date createTime;

    private Date updateTime;

    /**
     * 质疑是否勾选0未勾选1勾选
     */
    private String checked;

    private List<FMedicalForm> medicalForms;

    private List<FMedicalProperty> medicalProperties;

    private List<WarnConfig> warnConfigs;

    private String openCollapse;


    /**
     * 0 默认 1营养分析 2康复运动 3 不限
     */
    private Integer selfEvaluationType;

    private Integer healthManageType;

    private String createBy;

    private String updateBy;
}
