package com.dchealth.template.domain;

import com.dchealth.template.domain.vo.FMedicalValueVo;
import com.dchealth.warn.domain.WarnMedicalEarly;
import com.dchealth.warn.domain.form.WarnMedicalEarlyForm;
import com.dchealth.warn.domain.vo.WarnMedicalEarlyVO;
import lombok.Data;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * 属性对象 f_medical_property
 *
 * @author ruoyi
 * @date 2022-06-08
 */
@Data
public class FMedicalProperty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 属性编码 */
    @Excel(name = "属性编码")
    private String propertyCode;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String propertyName;

    /** 属性父id */
    @Excel(name = "属性父id")
    private Long parentId;

    /** 属性类型 */
    @Excel(name = "属性类型")
    private String propertyType;

    /** 字典表类型 */
    @Excel(name = "字典表类型")
    private String dictType;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long medicalFormId;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    @Excel(name = "是否开启APP端属性填写 0未开启 1 开启 ")
    private String openProperty;

    /** 参考卷王里的数据 */
    @Excel(name = "属性")
    private String attribute;

    private Long followId;

    /**
     * 排序
     */
    private Integer sortNum;

    private List<FMedicalValueVo> propertyValues;

    /** 属性值 */
    private String propertyValue;

    /** 其它属性值 */
    private String other;

    /**
     * 图片
     */
    private List<String> imageUrls;

    private Long articleId;

    /**
     * 预警
     */
    private List<WarnMedicalEarlyForm> warnMedicalEarlyForms;

    /** 是否有条件0无1有 */
    private String isCondition;

    /**
     * 预警返回
     */
    private List<WarnMedicalEarlyVO> warnMedicalEarlyVOS;

    /** 题目选项 */
    private List<FMedicalExamItem> itemList;
}
