package com.dchealth.warn.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 预警指导发送form
 */
@Data
public class WarnGuideMsgForm {

    /**
     * 档案ID
     */
    private Long recordId;

    /** 会话id */
    private Long warnGuideConverId;

    /** 消息类型1文本2图片 */
    private String msgType;

    /** 文本消息 */
    private String msgText;

    /** 消息图片路径 */
    private String msgImageUrl;

    /**
     * 发送人
     */
    private Long fromUserId;

    private Long deptId;

    private String fromUserType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
