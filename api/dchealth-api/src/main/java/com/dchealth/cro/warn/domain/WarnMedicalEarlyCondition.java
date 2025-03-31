package com.dchealth.cro.warn.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 随访单预警条件对象 warn_medical_early_condition
 * 
 * @author lchbu
 * @date 2023-03-01
 */
@Data
public class WarnMedicalEarlyCondition {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 预警主表ID */
    private Long medicalEarlyWarnId;

    /** 预警等级0无状态1异常2危机 */
    private String earlyWarnLevel;

    /** 性別 */
    private String sexCondition;

    /** 年龄条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    private String ageConditionType;

    /** 年龄预警值 */
    private String ageEarlyWarnValue;

    /** 年龄预警值起始值 */
    private String ageEarlyWarnStart;

    /** 年龄预警值终止值 */
    private String ageEarlyWarnEnd;

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

    private Date createTime;


    /** 是否有条件0无1有 */
    private String isCondition;
    /** 预设预警记录描述 */
    private String warnPreDesc;
    /** 是否生效1生效0失效 */
    private String isEffect;
    private String attribute;//配置json字符串


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("medicalEarlyWarnId", getMedicalEarlyWarnId())
            .append("earlyWarnLevel", getEarlyWarnLevel())
            .append("sexCondition", getSexCondition())
            .append("ageConditionType", getAgeConditionType())
            .append("ageEarlyWarnValue", getAgeEarlyWarnValue())
            .append("ageEarlyWarnStart", getAgeEarlyWarnStart())
            .append("ageEarlyWarnEnd", getAgeEarlyWarnEnd())
            .append("conditionType", getConditionType())
            .append("earlyWarnValue", getEarlyWarnValue())
            .append("earlyWarnStart", getEarlyWarnStart())
            .append("earlyWarnEnd", getEarlyWarnEnd())
            .append("createTime", getCreateTime())
            .append("propertyId", getPropertyId())
            .append("followId", getFollowId())
            .toString();
    }
}
