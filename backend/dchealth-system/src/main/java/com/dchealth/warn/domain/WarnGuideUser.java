package com.dchealth.warn.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 预警指导人员对象 warn_guide_user
 * 
 * @author lchbu
 * @date 2022-10-08
 */
public class WarnGuideUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 预警用户关系表 */
    private Long warnGuideUserId;

    /** 会话id */
    @Excel(name = "会话id")
    private Long warnGuideConverId;

    /** 1患者2PC用户 */
    @Excel(name = "1患者2PC用户")
    private String fromUserType;

    /** 成员id */
    @Excel(name = "成员id")
    private Long memberId;

    public void setWarnGuideUserId(Long warnGuideUserId) 
    {
        this.warnGuideUserId = warnGuideUserId;
    }

    public Long getWarnGuideUserId() 
    {
        return warnGuideUserId;
    }
    public void setWarnGuideConverId(Long warnGuideConverId) 
    {
        this.warnGuideConverId = warnGuideConverId;
    }

    public Long getWarnGuideConverId() 
    {
        return warnGuideConverId;
    }
    public void setFromUserType(String fromUserType) 
    {
        this.fromUserType = fromUserType;
    }

    public String getFromUserType() 
    {
        return fromUserType;
    }
    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("warnGuideUserId", getWarnGuideUserId())
            .append("warnGuideConverId", getWarnGuideConverId())
            .append("fromUserType", getFromUserType())
            .append("memberId", getMemberId())
            .toString();
    }
}
