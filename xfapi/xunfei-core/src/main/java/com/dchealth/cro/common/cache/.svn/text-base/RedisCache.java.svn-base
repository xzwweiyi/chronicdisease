package com.dchealth.cro.common.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisCache {
    private static final Logger L = LoggerFactory.getLogger(RedisCache.class);

    @Autowired
    private StringRedisTemplate stringTemplate;

    /**
     * 设置缓存数据 
     * @param key 键
     * @param val 键值
     */
    public void cacheString(String key, String val) {
        stringTemplate.opsForValue().set(key, val);
    }
    
    /**
     * 设置缓存数据
     * @param key 键
     * @param value 键值
     * @param timeout  失效时长
     * @param unit 时间单位
     */
    public void cacheString(String key,String value,long timeout, TimeUnit unit) {
    	stringTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取值
     * @param key 键
     * @return
     */
    public String getCacheString(String key) {
        return stringTemplate.opsForValue().get(key);
    }

    public HashMap<String, String> getWildCardString(String pattern) {
        Set<String> keys = stringTemplate.keys(pattern);
        HashMap<String, String> values = new HashMap<>(keys.size());
        ValueOperations<String, String> ops = stringTemplate.opsForValue();

        for (String key : keys) {
            values.put(key, ops.get(key));
        }

        return values;
    }

    public boolean cacheExists(String key) {
        L.debug("list size [{}]", stringTemplate.opsForList().size(key));
        return stringTemplate.opsForList().size(key) > 0;
    }
    public String getNextFromList(String key) {
        return stringTemplate.opsForList().leftPop(key);
    }

    public void putList(String key, List<String> list) {
        for (String o : list) {
            stringTemplate.opsForList().rightPush(key, o);
        }
    }

    public long getListLength(String key) {
        return stringTemplate.opsForList().size(key);
    }

    public boolean delCacheString(String key) {
    	stringTemplate.delete(key);
        return true;
    }
}
