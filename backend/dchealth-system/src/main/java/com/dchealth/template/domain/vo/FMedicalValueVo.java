package com.dchealth.template.domain.vo;

import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class FMedicalValueVo extends BaseEntity {

    /** 主键 */
    private Long id;

    /** 档案Id */
    private Long recordId;

    /** 表单ID */
    private Long fromId;

    /** 来源(1-患者，2-EDC) */
    private String type;

    /** 序号 */
    private String number;

    /** 属性id */
    private Long propertyId;

    /** 属性code */
    private String propertyCode;

    /** 属性值 */
    private String propertyValue;

    /** 其它属性值 */
    private String other;

    /** 队列id */
    private Long researchId;

    /** 问卷id */
    private Long articleId;

    /** 患者id */
    private Long patientId;

    /** 随访id */
    private Long followId;

    /** 提交记录id */
    private Long examRecordId;

    private int suffixId;

    private List<String> imageUrls;

}
