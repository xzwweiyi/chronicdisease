package com.dchealth.base.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 app_manage
 *
 * @author ruoyi
 * @date 2022-06-16
 */
public class AppManage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** appinf的id */
    @Excel(name = "appinf的id")
    private Long appId;

    /** 研究队列id */
    @Excel(name = "研究队列id")
    private Long researchId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setResearchId(Long researchId)
    {
        this.researchId = researchId;
    }

    public Long getResearchId()
    {
        return researchId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("appId", getAppId())
                .append("researchId", getResearchId())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .toString();
    }
}
