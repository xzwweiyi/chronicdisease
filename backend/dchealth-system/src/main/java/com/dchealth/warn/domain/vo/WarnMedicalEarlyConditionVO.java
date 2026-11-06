package com.dchealth.warn.domain.vo;

import lombok.Data;

@Data
public class WarnMedicalEarlyConditionVO {

    private Long id;

    /** 预警主表ID */
    private Long medicalEarlyWarnId;

    /** 条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    private String conditionType;

    /** 预警值 */
    private String earlyWarnValue;

    /** 预警值起始值 */
    private String earlyWarnStart;

    /** 预警值终止值 */
    private String earlyWarnEnd;

    /** 字段id */
    private Long propertyId;

    /** 随访表单id */
    private Long followId;


    /** 预警等级0无状态1异常2危机 */
    private String earlyWarnLevel;
    /** 性别 */
    private String sexCondition;
    /** 年龄条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    private String ageConditionType;
    /** 年龄预警值 */
    private String ageEarlyWarnValue;
    /** 年龄预警值起始值 */
    private String ageEarlyWarnStart;
    /** 年龄预警值终止值 */
    private String ageEarlyWarnEnd;
    
}
