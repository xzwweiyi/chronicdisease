package com.dchealth.medical.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 随访表单vo
 */

@Data
public class MedicalFormVO extends FormVO{

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

    /**  */
    private String attribute;

    /** 0模块1标准项2拍照项3自评量表4预警填报 */
    private String formType;

    /** cms问卷 */
    private Long articleId;

    /** 表单来源1CRF,2CMS */
    private String formSource;

    /** 是否动态表格0否1是 */
    private String dynamic;

    /**
     * 质疑是否勾选0未勾选1勾选
     */
    private String checked;

    /**
     * 子表单
     */
    private List<MedicalFormVO> childrens;

    private List<MedicalFormVO> childMedicalFormVOS;

    /**
     * 属性
     */
    private List<MedicalPropertyVO> propertyVOS;

    //问卷属性
    private List<MedicalPropertyVO> propertyExamVOS;

    /**
     * 预警
     */
    private List<WarnConfigVO> warnConfigVOS;

    /**
     * 图片
     */
    private List<String> imageUrls;

    /**
     * 动态表格数据
     * key为行号
     * value 为值
     */
    private Map<Integer, List<String>> dynamicTable;

    /**
     * 动态行容器数据
     * key：number,value 数据行列表
     */
    private Map<String, List<MedicalPropertyVO>> dynamicTableNew;

    /**
     * 是否预警源 false：否   true：是
     */
    private boolean isWarnSource;
}
