package com.dchealth.cro.account.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 预警指导聊天对象 warn_leave_message
 * 
 * @author lchbu
 * @date 2023-03-17
 */
@Data
@Accessors(chain = true)
public class AccountLeaveMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 患者随访ID */
    private Long recordId;

    /**
     * 发送方id
     */
    private Long senderId;

    /**
     * 接收方id
     */
    private Long receiverId;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /**
     * 是否已读 0 未读  1  已读
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Boolean isRead;


    /** 患者id */
    private Long personId;

    /** 患者名称 */
    private String accountName;

    /** 用户ID */
    private Long accountId;

    /** 用户类型 1患者 2医生 */
    private String userType;

    /** 留言类型 1文本  2 图片路径 */
    private String contentType;

    /** 留言内容、图片url */
    private String content;

    /** 搜索值 */
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;
    /**
     * 删除标识
     * @return
     */
    private Boolean isDeleteDc;
    private Boolean isDeletePt;

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
