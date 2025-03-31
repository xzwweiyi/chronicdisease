package com.dchealth.cro.form.domain;

import java.util.Date;

import com.dchealth.cro.common.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 属性题目选项对象 f_medical_exam_item
 *
 * @author lchbu
 * @date 2022-10-31
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FMedicalExamItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 题目属性ID */
    private Long propertyId;

    /** 下拉选项值 */
    private String itemValue;

    /** 选项标题 */
    private String itemLabel;

    /** 排序 */
    private Integer orderNum;

    /** 分数 */
    private Long itemScores;

    /** 模板ID */
    private Long medicalFormId;

    private String tableName;
    private Date creatTime;
}
