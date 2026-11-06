package com.dchealth.base.service.impl;

import com.dchealth.base.service.VoiceBoxService;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.http.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音盒默认实现（HTTP 占位）。
 *
 * <p>通过 {@code voicebox.url} 配置语音盒本地服务地址；调用时携带 {@code phone} 参数。
 * 该实现为占位，真实语音盒座席 SDK 对接时替换此 Bean。</p>
 */
@Service
public class VoiceBoxHttpServiceImpl implements VoiceBoxService {

    private static final Logger log = LoggerFactory.getLogger(VoiceBoxHttpServiceImpl.class);

    /**
     * 语音盒服务地址，未配置时为空。
     */
    @Value("${voicebox.url:}")
    private String voiceBoxUrl;

    @Override
    public boolean dial(String phoneNumber) {
        if (StringUtils.isEmpty(voiceBoxUrl)) {
            throw new ServiceException("语音盒未配置，请联系管理员配置 voicebox.url");
        }
        Map<String, String> params = new HashMap<>();
        params.put("phone", phoneNumber);
        String response;
        try {
            response = HttpClientUtil.getInstance().sendHttpPost(voiceBoxUrl, params);
        } catch (Exception e) {
            log.error("语音盒拨号失败, phone={}", phoneNumber, e);
            throw new ServiceException("语音盒拨号失败：" + e.getMessage());
        }
        log.info("语音盒拨号请求, phone={}, response={}", phoneNumber, response);
        return response != null;
    }
}
