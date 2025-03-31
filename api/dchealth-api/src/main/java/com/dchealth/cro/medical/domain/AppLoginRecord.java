package com.dchealth.cro.medical.domain;

import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.Data;


@Data
public class AppLoginRecord{

    /** $column.columnComment */
    private Long id;

    /** 账号id */
    private Long accountId;

    /** 手机号 */
    private String mobile;

    /** 登录类型0未知1医生2患者 */
    private String appType;

    /** 意见 */
    private String deviceToken;

    /** 创建月 */
    private Integer monthStat;

    /** 创建年 */
    private Integer yearStat;

    /** 创建时间 */
    private Date createTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("accountId", getAccountId())
            .append("mobile", getMobile())
            .append("appType", getAppType())
            .append("deviceToken", getDeviceToken())
            .append("monthStat", getMonthStat())
            .append("yearStat", getYearStat())
            .append("createTime", getCreateTime())
            .toString();
    }
}
