package com.dchealth.cro.account.form.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 指导意见消息列表
 */
@Data
public class UserMessageListVO {

    /**
     * 姓名
     */
    private String userName;

    private Long id;

    /**
     * 消息时间
     */
    private String msgTime;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 消息类型1文本2图片
     */
    private String msgType;

    /**
     * 消息内容
     */
    private String msgText;

    //用户类型 1医生 2患者
    private String userType;

    /**
     * 消息图片路径
     */
    private String msgImageUrl;

    /**
     * 会话ID
     */
    private Long warnGuideConverId;

    /**
     * 发送方id
     */
    private String senderId;

    /**
     * 接收方id
     */
    private String receiverId;

    /**
     * 发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

    /**
     * 接收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /**
     * 是否已读 0 未读  1  已读
     */
    private Boolean isRead;


    /** 患者id */
    private Long personId;

    /** 患者名称 */
    private String accountName;

    /** 用户ID */
    private Long accountId;


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
    private Boolean isDelete;


}
