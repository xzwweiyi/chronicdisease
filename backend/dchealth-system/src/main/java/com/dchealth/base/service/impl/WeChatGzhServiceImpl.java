package com.dchealth.base.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.base.domain.WeChatGzhMessageVO;
import com.dchealth.base.service.WeChatGzhService;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.http.HttpClientUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号模板消息服务实现。
 */
@Service
public class WeChatGzhServiceImpl implements WeChatGzhService {

    private static final Logger log = LoggerFactory.getLogger(WeChatGzhServiceImpl.class);

    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
    private static final String ACCESS_TOKEN_CACHE_KEY = "td:wechat:gzh:access_token";

    /**
     * 随访提醒模板，与 api 模块 WeChatConstants.TEMPLATE_SUIFANF 一致
     */
    private static final String TEMPLATE_SUIFANF = "jCatk5_tGl2WWIR2XhzgqPGIpy0GkRJeIfP_nFetisE";

    @Value("${wechat.gzh.appid:}")
    private String appid;

    @Value("${wechat.gzh.secret:}")
    private String secret;

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean sendFollowReminder(String openId, String patientName, String content) {
        if (StringUtils.isEmpty(openId)) {
            throw new ServiceException("患者未绑定微信");
        }
        String accessToken = getAccessToken();
        WeChatGzhMessageVO messageVO = new WeChatGzhMessageVO();
        messageVO.setTouser(openId);
        messageVO.setTemplate_id(TEMPLATE_SUIFANF);

        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("first", buildDataItem("您好，" + patientName + "，您有一条随访提醒："));
        data.put("keyword1", buildDataItem(patientName));
        data.put("keyword2", buildDataItem(content));
        data.put("remark", buildDataItem("请及时关注，谢谢。"));
        messageVO.setData(data);

        String json = JSON.toJSONString(messageVO);
        String response = doPostJson(SEND_URL + accessToken, json);
        log.info("微信模板消息发送, openId={}, response={}", openId, response);
        if (StringUtils.isEmpty(response)) {
            throw new ServiceException("微信模板消息发送失败：无响应");
        }
        JSONObject rs = JSON.parseObject(response);
        Integer errcode = rs.getInteger("errcode");
        if (errcode != null && errcode == 0) {
            return true;
        }
        throw new ServiceException("微信模板消息发送失败：" + rs.getString("errmsg"));
    }

    private Map<String, String> buildDataItem(String value) {
        Map<String, String> item = new HashMap<>();
        item.put("value", value);
        return item;
    }

    private String getAccessToken() {
        String cached = redisCache.getCacheString(ACCESS_TOKEN_CACHE_KEY);
        if (StringUtils.isNotEmpty(cached)) {
            return cached;
        }
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(secret)) {
            throw new ServiceException("公众号未配置 appid/secret，请联系管理员");
        }
        String url = String.format(TOKEN_URL, appid, secret);
        String json = HttpClientUtil.getInstance().sendHttpGet(url);
        if (StringUtils.isEmpty(json)) {
            throw new ServiceException("获取微信 access_token 失败：无响应");
        }
        JSONObject rs = JSON.parseObject(json);
        String accessToken = rs.getString("access_token");
        if (StringUtils.isEmpty(accessToken)) {
            throw new ServiceException("获取微信 access_token 失败：" + rs.getString("errmsg"));
        }
        Long expiresIn = rs.getLong("expires_in");
        long timeout = (expiresIn != null && expiresIn > 60) ? expiresIn - 60 : 3600L;
        redisCache.cacheString(ACCESS_TOKEN_CACHE_KEY, accessToken, timeout, TimeUnit.SECONDS);
        return accessToken;
    }

    private String doPostJson(String url, String json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            StringEntity entity = new StringEntity(json, StandardCharsets.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(15000)
                    .setConnectTimeout(15000)
                    .setConnectionRequestTimeout(15000)
                    .build();
            httpPost.setConfig(requestConfig);
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                return responseEntity != null ? EntityUtils.toString(responseEntity, StandardCharsets.UTF_8) : null;
            }
        } catch (Exception e) {
            log.error("微信模板消息 HTTP 请求异常", e);
            throw new ServiceException("微信模板消息请求异常：" + e.getMessage());
        } finally {
            try {
                httpClient.close();
            } catch (Exception ignore) {
            }
        }
    }
}
