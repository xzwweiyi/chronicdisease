package com.dchealth.warn.domain;

import com.dchealth.warn.domain.form.WarnGuideMsgForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 预警指导信息对象 warn_guide_msg
 * 
 * @author lchbu
 * @date 2022-10-08
 */
@Data
public class WarnGuideMsg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 指导信息表 */
    private Long warnGuideMsgId;

    /** 会话id */
    @Excel(name = "会话id")
    private Long warnGuideConverId;

    /** 消息类型1文本2图片 */
    @Excel(name = "消息类型1文本2图片")
    private String msgType;

    /** 文本消息 */
    @Excel(name = "文本消息")
    private String msgText;

    /** 消息图片路径 */
    @Excel(name = "消息图片路径")
    private String msgImageUrl;

    /** 发送人 */
    @Excel(name = "发送人")
    private Long fromUserId;

    /** 1患者2PC用户 */
    @Excel(name = "1患者2PC用户")
    private String fromUserType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public WarnGuideMsg(){}

    public WarnGuideMsg(WarnGuideMsgForm warnGuideMsgForm){
        this.warnGuideConverId = warnGuideMsgForm.getWarnGuideConverId();
        this.msgType = warnGuideMsgForm.getMsgType();
        this.msgText = warnGuideMsgForm.getMsgText();
        this.msgImageUrl = warnGuideMsgForm.getMsgImageUrl();
        this.fromUserId = warnGuideMsgForm.getFromUserId();
        this.fromUserType = warnGuideMsgForm.getFromUserType();
        this.createTime = warnGuideMsgForm.getCreateTime();
    }

}
