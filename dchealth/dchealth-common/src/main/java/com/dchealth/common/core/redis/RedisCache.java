package com.dchealth.common.core.redis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

/**
 * spring redis 工具类 (with in-memory fallback for local dev without Redis)
 */
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache
{
    private static final Logger log = LoggerFactory.getLogger(RedisCache.class);

    @Autowired(required = false)
    public RedisTemplate redisTemplate;

    @Autowired(required = false)
    private StringRedisTemplate stringTemplate;

    // In-memory fallback cache
    private final ConcurrentHashMap<String, Object> memoryCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> memoryCacheExpire = new ConcurrentHashMap<>();

    private boolean redisAvailable() {
        if (redisTemplate == null) return false;
        try {
            redisTemplate.keys("*");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void cleanExpired(String key) {
        Long expireTime = memoryCacheExpire.get(key);
        if (expireTime != null && System.currentTimeMillis() > expireTime) {
            memoryCache.remove(key);
            memoryCacheExpire.remove(key);
        }
    }

    public <T> void setCacheObject(final String key, final T value)
    {
        if (redisAvailable()) {
            try { redisTemplate.opsForValue().set(key, value); return; } catch (Exception e) { log.warn("Redis set failed, using memory: {}", e.getMessage()); }
        }
        memoryCache.put(key, value);
        memoryCacheExpire.remove(key);
    }

    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit)
    {
        if (redisAvailable()) {
            try { redisTemplate.opsForValue().set(key, value, timeout, timeUnit); return; } catch (Exception e) { log.warn("Redis set with timeout failed, using memory: {}", e.getMessage()); }
        }
        memoryCache.put(key, value);
        if (timeout != null) {
            long millis = timeUnit.toMillis(timeout);
            memoryCacheExpire.put(key, System.currentTimeMillis() + millis);
        }
    }

    public boolean expire(final String key, final long timeout)
    {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    public boolean expire(final String key, final long timeout, final TimeUnit unit)
    {
        if (redisAvailable()) {
            try { return redisTemplate.expire(key, timeout, unit); } catch (Exception e) { log.warn("Redis expire failed: {}", e.getMessage()); }
        }
        memoryCacheExpire.put(key, System.currentTimeMillis() + unit.toMillis(timeout));
        return true;
    }

    public <T> T getCacheObject(final String key)
    {
        if (redisAvailable()) {
            try {
                ValueOperations<String, T> operation = redisTemplate.opsForValue();
                T val = operation.get(key);
                if (val != null) return val;
            } catch (Exception e) { log.warn("Redis get failed, trying memory: {}", e.getMessage()); }
        }
        cleanExpired(key);
        return (T) memoryCache.get(key);
    }

    public boolean deleteObject(final String key)
    {
        if (redisAvailable()) {
            try { redisTemplate.delete(key); } catch (Exception e) { log.warn("Redis delete failed: {}", e.getMessage()); }
        }
        memoryCache.remove(key);
        memoryCacheExpire.remove(key);
        return true;
    }

    public long deleteObject(final Collection collection)
    {
        if (redisAvailable()) {
            try { redisTemplate.delete(collection); } catch (Exception e) { log.warn("Redis batch delete failed: {}", e.getMessage()); }
        }
        for (Object key : collection) {
            memoryCache.remove(String.valueOf(key));
            memoryCacheExpire.remove(String.valueOf(key));
        }
        return collection.size();
    }

    public <T> long setCacheList(final String key, final List<T> dataList)
    {
        if (redisAvailable()) {
            try { Long count = redisTemplate.opsForList().rightPushAll(key, dataList); return count == null ? 0 : count; } catch (Exception e) { log.warn("Redis list set failed: {}", e.getMessage()); }
        }
        memoryCache.put(key, new ArrayList<>(dataList));
        return dataList.size();
    }

    public <T> List<T> getCacheList(final String key)
    {
        if (redisAvailable()) {
            try { return redisTemplate.opsForList().range(key, 0, -1); } catch (Exception e) { log.warn("Redis list get failed: {}", e.getMessage()); }
        }
        cleanExpired(key);
        Object val = memoryCache.get(key);
        return val instanceof List ? (List<T>) val : new ArrayList<>();
    }

    public <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet)
    {
        if (redisAvailable()) {
            try {
                BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(key);
                for (T it : dataSet) setOperation.add(it);
                return setOperation;
            } catch (Exception e) { log.warn("Redis set failed: {}", e.getMessage()); }
        }
        memoryCache.put(key, new HashSet<>(dataSet));
        return null;
    }

    public <T> Set<T> getCacheSet(final String key)
    {
        if (redisAvailable()) {
            try { return redisTemplate.opsForSet().members(key); } catch (Exception e) { log.warn("Redis set get failed: {}", e.getMessage()); }
        }
        cleanExpired(key);
        Object val = memoryCache.get(key);
        return val instanceof Set ? (Set<T>) val : new HashSet<>();
    }

    public <T> void setCacheMap(final String key, final Map<String, T> dataMap)
    {
        if (dataMap != null) {
            if (redisAvailable()) {
                try { redisTemplate.opsForHash().putAll(key, dataMap); return; } catch (Exception e) { log.warn("Redis map set failed: {}", e.getMessage()); }
            }
            memoryCache.put(key, new HashMap<>(dataMap));
        }
    }

    public <T> Map<String, T> getCacheMap(final String key)
    {
        if (redisAvailable()) {
            try { return redisTemplate.opsForHash().entries(key); } catch (Exception e) { log.warn("Redis map get failed: {}", e.getMessage()); }
        }
        cleanExpired(key);
        Object val = memoryCache.get(key);
        return val instanceof Map ? (Map<String, T>) val : new HashMap<>();
    }

    public <T> void setCacheMapValue(final String key, final String hKey, final T value)
    {
        if (redisAvailable()) {
            try { redisTemplate.opsForHash().put(key, hKey, value); return; } catch (Exception e) { log.warn("Redis map value set failed: {}", e.getMessage()); }
        }
        Object val = memoryCache.get(key);
        Map<String, T> map = val instanceof Map ? (Map<String, T>) val : new HashMap<>();
        map.put(hKey, value);
        memoryCache.put(key, map);
    }

    public <T> T getCacheMapValue(final String key, final String hKey)
    {
        if (redisAvailable()) {
            try {
                HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
                T val = opsForHash.get(key, hKey);
                if (val != null) return val;
            } catch (Exception e) { log.warn("Redis map value get failed: {}", e.getMessage()); }
        }
        cleanExpired(key);
        Object val = memoryCache.get(key);
        if (val instanceof Map) {
            return (T) ((Map) val).get(hKey);
        }
        return null;
    }

    public void delCacheMapValue(final String key, final String hkey)
    {
        if (redisAvailable()) {
            try { redisTemplate.opsForHash().delete(key, hkey); } catch (Exception e) { log.warn("Redis map value delete failed: {}", e.getMessage()); }
        }
        Object val = memoryCache.get(key);
        if (val instanceof Map) {
            ((Map) val).remove(hkey);
        }
    }

    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys)
    {
        if (redisAvailable()) {
            try { return redisTemplate.opsForHash().multiGet(key, hKeys); } catch (Exception e) { log.warn("Redis multi get failed: {}", e.getMessage()); }
        }
        cleanExpired(key);
        Object val = memoryCache.get(key);
        if (val instanceof Map) {
            return hKeys.stream().map(k -> (T) ((Map) val).get(k)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public Collection<String> keys(final String pattern)
    {
        if (redisAvailable()) {
            try { return redisTemplate.keys(pattern); } catch (Exception e) { log.warn("Redis keys failed: {}", e.getMessage()); }
        }
        String prefix = pattern.replace("*", "");
        return memoryCache.keySet().stream()
                .filter(k -> k.startsWith(prefix))
                .collect(Collectors.toSet());
    }

    public void cacheString(String key, String value, long timeout, TimeUnit unit) {
        if (stringTemplate != null && redisAvailable()) {
            try { stringTemplate.opsForValue().set(key, value, timeout, unit); return; } catch (Exception e) { log.warn("Redis string cache failed: {}", e.getMessage()); }
        }
        memoryCache.put(key, value);
        if (timeout > 0) {
            memoryCacheExpire.put(key, System.currentTimeMillis() + unit.toMillis(timeout));
        }
    }

    public String getCacheString(String key) {
        if (stringTemplate != null && redisAvailable()) {
            try { return stringTemplate.opsForValue().get(key); } catch (Exception e) { log.warn("Redis string get failed: {}", e.getMessage()); }
        }
        cleanExpired(key);
        Object val = memoryCache.get(key);
        return val != null ? String.valueOf(val) : null;
    }
}
