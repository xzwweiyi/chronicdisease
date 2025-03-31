package com.dchealth.warn.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 预警指导会话对象 warn_guide_conver
 * 
 * @author lchbu
 * @date 2022-10-08
 */
public class WarnGuideConver extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预警指导会话表 */
    private Long warnGuideConverId;

    /** 档案id */
    @Excel(name = "档案id")
    private Long recordId;

    /** 科室id */
    @Excel(name = "科室id")
    private Long deptId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long createUserId;

    public void setWarnGuideConverId(Long warnGuideConverId) 
    {
        this.warnGuideConverId = warnGuideConverId;
    }

    public Long getWarnGuideConverId() 
    {
        return warnGuideConverId;
    }
    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setCreateUserId(Long createUserId) 
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId() 
    {
        return createUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("warnGuideConverId", getWarnGuideConverId())
            .append("recordId", getRecordId())
            .append("deptId", getDeptId())
            .append("createUserId", getCreateUserId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
