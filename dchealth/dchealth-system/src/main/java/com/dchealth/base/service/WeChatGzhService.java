package com.dchealth.base.service;

/**
 * 微信公众号模板消息服务。
 */
public interface WeChatGzhService {

    /**
     * 发送随访提醒模板消息。
     *
     * @param openId      接收者公众号 openid
     * @param patientName 患者姓名
     * @param content     提醒内容
     * @return true 发送成功
     */
    boolean sendFollowReminder(String openId, String patientName, String content);
}
