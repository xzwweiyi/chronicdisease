package com.dchealth.cro.common.wechat;

import lombok.Data;

@Data
public class WechatUserInfo {

    private String openid;
    /**
     * 微信用户唯一标识
     */
    private String unionid;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 关注时间戳
     */
    private Long subscribe_time;

    /**
     * 是否关注
     */
    private Integer subscribe;

}
