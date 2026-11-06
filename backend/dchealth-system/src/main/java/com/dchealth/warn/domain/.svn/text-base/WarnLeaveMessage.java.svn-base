package com.dchealth.warn.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 预警指导聊天对象 warn_leave_message
 * 
 * @author lchbu
 * @date 2023-03-17
 */
@Data
public class WarnLeaveMessage extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 患者随访ID */
    @Excel(name = "患者随访ID")
    private Long recordId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    /** 用户类型 1患者 2医生 */
    @Excel(name = "用户类型 1患者 2医生")
    private String userType;

    /** 留言类型 1文本  2 图片路径 */
    @Excel(name = "留言类型 1文本  2 图片路径")
    private String contentType;

    /** 留言内容、图片url */
    @Excel(name = "留言内容、图片url")
    private String content;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("recordId", getRecordId())
            .append("accountId", getAccountId())
            .append("userType", getUserType())
            .append("contentType", getContentType())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .toString();
    }
}
