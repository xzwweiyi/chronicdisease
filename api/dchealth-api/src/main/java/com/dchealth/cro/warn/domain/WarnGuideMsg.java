package com.dchealth.cro.warn.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预警指导信息对象 warn_guide_msg
 * 
 * @author lchbu
 * @date 2022-10-08
 */
@Data
public class WarnGuideMsg
{
    private static final long serialVersionUID = 1L;

    /** 指导信息表 */
    private Long warnGuideMsgId;

    /** 会话id */
    private Long warnGuideConverId;

    /** 消息类型1文本2图片 */
    private String msgType;

    /** 文本消息 */
    private String msgText;

    /** 消息图片路径 */
    private String msgImageUrl;

    /** 发送人 */
    private Long fromUserId;

    /** 1患者2PC用户 */
    private String fromUserType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;



}
