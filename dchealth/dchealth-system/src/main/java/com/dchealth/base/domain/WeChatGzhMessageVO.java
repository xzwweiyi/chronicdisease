package com.dchealth.base.domain;

import lombok.Data;

import java.util.Map;

/**
 * 公众号模板消息实体。
 */
@Data
public class WeChatGzhMessageVO {

    /**
     * 接收者 openid
     */
    private String touser;

    /**
     * 模板 ID
     */
    private String template_id;

    /**
     * 模板数据，形如：
     * {
     *   "first": {"value": "..."},
     *   "keyword1": {"value": "..."},
     *   "remark": {"value": "..."}
     * }
     */
    private Map<String, Map<String, String>> data;
}
