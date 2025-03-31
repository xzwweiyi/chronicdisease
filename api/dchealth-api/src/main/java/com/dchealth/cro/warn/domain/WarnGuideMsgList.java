package com.dchealth.cro.warn.domain;

import com.dchealth.cro.common.utils.DateUtils;
import lombok.Data;

import java.util.Map;

/**
 * 指导意见消息列表
 */
@Data
public class WarnGuideMsgList {

    /**
     * 姓名
     */
    private String userName;

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

    /**
     * 消息图片路径
     */
    private String msgImageUrl;

    private Long fromUserId;
    /**
     * 会话ID
     */
    private Long warnGuideConverId;

    public WarnGuideMsgList(WarnGuideMsg warnGuideMsg, Map<String, Map<String,String>> user){
        if ("1".equals(warnGuideMsg.getFromUserType()) && user.containsKey("p_"+warnGuideMsg.getFromUserId())){
            Map<String,String> p = user.get("p_"+warnGuideMsg.getFromUserId());
            this.userName = p.get("userName");
            this.headImage = p.get("headImage");
        }else if ("2".equals(warnGuideMsg.getFromUserType()) && user.containsKey("s_"+warnGuideMsg.getFromUserId())){
            Map<String,String> s = user.get("s_"+warnGuideMsg.getFromUserId());
            this.userName = s.get("userName");
            this.headImage = s.get("headImage");
        }
        this.msgType = warnGuideMsg.getMsgType();
        if ("1".equals(warnGuideMsg.getMsgType())){
            this.msgText = warnGuideMsg.getMsgText();
        }else if ("2".equals(warnGuideMsg.getMsgType())){
            this.msgImageUrl = warnGuideMsg.getMsgImageUrl();
        }
        if (warnGuideMsg.getCreateTime() != null){
            this.msgTime = DateUtils.parseDateToStrFull(warnGuideMsg.getCreateTime());
        }
        this.warnGuideConverId = warnGuideMsg.getWarnGuideConverId();
        this.fromUserId = warnGuideMsg.getFromUserId();
    }


}
