package com.dchealth.user.vo;

import lombok.Data;

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

    private Long accountId;


}
