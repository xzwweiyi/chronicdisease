package com.dchealth.medical.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * app登录记录对象 app_login_record
 * 
 * @author lchbu
 * @date 2023-09-11
 */
@Data
public class AppLoginRecord extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 账号id */
    @Excel(name = "账号id")
    private Long accountId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String mobile;

    /** 登录类型0未知1医生2患者 */
    @Excel(name = "登录类型0未知1医生2患者")
    private String appType;

    /** 意见 */
    @Excel(name = "意见")
    private String deviceToken;

    /** 创建月 */
    @Excel(name = "创建月")
    private Integer monthStat;

    /** 创建年 */
    @Excel(name = "创建年")
    private Integer yearStat;


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
