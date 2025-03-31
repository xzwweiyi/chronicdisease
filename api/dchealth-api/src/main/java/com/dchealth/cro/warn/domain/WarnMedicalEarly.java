package com.dchealth.cro.warn.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 随访单预警对象 warn_medical_early
 * 
 * @author lchbu
 * @date 2023-03-01
 */
@Data
public class WarnMedicalEarly {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 预警类型 */
    private String earlyWarnType;

    /** 是否生效1生效0失效 */
    private String isEffect;

    /** 正常值起始 */
    private String normalStart;

    /** 正常值末 */
    private String normalEnd;

    /** 正常值 */
    private String normalValue;

    /** 条件类型1等于2大于3大于等于4小于5小于等于6区间 */
    private String conditionType;

    /** 是否有条件0无1有 */
    private String isCondition;

    /** 字段id */
    private Long propertyId;

    /** form id */
    private Long formId;

    /** 预警等级0无状态1异常2危机 */
    private String earlyWarnLevel;

    /** 随访表单id */
    private Long followId;

    private Date createTime;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("earlyWarnType", getEarlyWarnType())
            .append("isEffect", getIsEffect())
            .append("normalStart", getNormalStart())
            .append("normalEnd", getNormalEnd())
            .append("normalValue", getNormalValue())
            .append("createTime", getCreateTime())
            .append("conditionType", getConditionType())
            .append("isCondition", getIsCondition())
            .append("propertyId", getPropertyId())
            .append("formId", getFormId())
            .append("earlyWarnLevel", getEarlyWarnLevel())
            .append("followId", getFollowId())
            .toString();
    }
}
