package com.dchealth.cro.warn.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 周期预警记录对象 warn_early_record
 * 
 * @author lchbu
 * @date 2023-03-03
 */
@Data
public class WarnEarlyRecord {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    private Long recordId;

    /** 预警配置表id */
    private Long warnMedicalEarlyConditionId;

    /** 预警记录描述 */
    private String warnDesc;

    /** 属性值 */
    private String propertyValue;

    private Date createTime;
    private Date updateTime;
    private String updateBy;

    private String propertyName;
    private String earlyWarnLevel;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("warnMedicalEarlyConditionId", getWarnMedicalEarlyConditionId())
            .append("warnDesc", getWarnDesc())
            .append("propertyValue", getPropertyValue())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
