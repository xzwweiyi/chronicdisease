package com.dchealth.cro.warn.form;

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
public class WarnGuideMsgForm
{
    private static final long serialVersionUID = 1L;
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
     * 发送人id
     */
    private Long fromUserId;

    private Long deptId;

    private String fromUserType;//1患者 2医生

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
