package com.dchealth.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 app_info
 *
 * @author ruoyi
 * @date 2022-06-16
 */
public class AppInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** app名称 */
    @Excel(name = "app名称")
    private String appName;

    /** 使用状态 0未使用 1使用 */
    @Excel(name = "使用状态 0未使用 1使用")
    private String status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppName()
    {
        return appName;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appName", getAppName())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
