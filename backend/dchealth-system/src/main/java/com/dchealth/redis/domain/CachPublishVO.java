package com.dchealth.redis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CachPublishVO extends BaseVO {

    private String channel;

    private String message;

}
