package com.dchealth.base.domain;

import com.dchealth.base.service.BaseRef;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.List;

/**
 * APP 与队列关系对象 app_research
 *
 * @author ruoyi
 * @date 2022-06-23
 */
public class AppResearch extends BaseEntity implements BaseRef
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 队列ID */
    @Excel(name = "队列ID")
    private Long researchId;

    /** 应用ID */
    @Excel(name = "应用ID")
    private Long appId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setResearchId(Long researchId)
    {
        this.researchId = researchId;
    }

    public Long getResearchId()
    {
        return researchId;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("researchId", getResearchId())
                .append("appId", getAppId())
                .toString();
    }

    @Override
    public List<Long> getList() {
        return list;
    }
}
