package com.dchealth.user.vo.req;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/6/18 18:42
 * @Version: 1.0
 */
@Data
public class UserMessageListReq {
    /**
     * 档案ID
     */
    private String personId;

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
