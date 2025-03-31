package com.dchealth.user.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_login_logs
 *
 * @author ruoyi
 * @date 2022-06-15
 */
public class UserLoginLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 事件类型 1 短信登录  2 账号密码登录 5 注册  6 退出  11 提交审核 12 审核通过 13 审核未通过 */
    @Excel(name = "事件类型 1 短信登录  2 账号密码登录 5 注册  6 退出  11 提交审核 12 审核通过 13 审核未通过")
    private String eventName;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getEventName()
    {
        return eventName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("eventName", getEventName())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .toString();
    }
}
