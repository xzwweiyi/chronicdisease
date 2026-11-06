package com.dchealth.base.service;

/**
 * 语音盒（座席）拨号服务抽象。
 *
 * <p>用于随访工作站「一键拨打电话」。当前为可配置的 HTTP 默认实现，
 * 后续接入真实语音盒座席 SDK 时，替换 {@link VoiceBoxService} 的实现 Bean 即可，
 * 调用方无需改动。</p>
 */
public interface VoiceBoxService {

    /**
     * 触发语音盒拨打指定号码。
     *
     * @param phoneNumber 被叫号码
     * @return true 表示拨号请求已成功触发
     */
    boolean dial(String phoneNumber);
}
