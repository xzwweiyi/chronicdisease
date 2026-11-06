package com.dchealth.redis;

import com.dchealth.redis.exception.RedisException;
import com.dchealth.redis.service.RedisOperations;
import com.dchealth.redis.service.impl.*;

public class RedisOperationsFactory {
    public static RedisOperations build(String type) throws RedisException {
        if (ConSts.STRIGN.equals(type) || ConSts.NONE.equals(type)) {
            return new RedisOperationsForStringImpl();
        } else if (ConSts.LIST.equals(type)) {

            return new RedisOperationsForListImpl();
        } else if (ConSts.HASH.equals(type)) {

            return new RedisOperationsForHashImpl();
        } else if (ConSts.SET.equals(type)) {

            return new RedisOperationsForSetImpl();
        } else if (ConSts.ZSET.equals(type)) {

            return new RedisOperationsForZSetImpl();
        } else {
            throw new RedisException("非法的操作");
        }
    }
}
