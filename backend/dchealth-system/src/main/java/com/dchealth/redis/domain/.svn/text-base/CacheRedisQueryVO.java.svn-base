package com.dchealth.redis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class CacheRedisQueryVO extends BaseVO implements Serializable {
    private String key;

    private String cacheKeyword;

    /**
     * 数据类型
     */
    private String type;

    private int pageNo = 1;

    /**
     * 默认100条分页
     */
    private int pageSize = 100;
}
