package com.dchealth.cro.account.domain;

import lombok.Data;

/**
 * @Auther: cuitao
 * @Date: 2023/5/29 - 05 - 29 - 15:40
 * @Description: com.dchealth.cro.minprogram.domain
 */
@Data
public class MessagePushInfo {
    private String appId;
    private String accountId;
    private String content;
}
