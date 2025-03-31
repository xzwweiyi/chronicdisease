package com.dchealth.redis.service;

import com.dchealth.redis.domain.*;
import com.dchealth.redis.exception.RedisException;

import java.util.List;
import java.util.Map;

public interface RedisOperationService {
    /**
     * 缓存 - 列表查询
     *
     * @param vo 查询参数
     * @return 缓存列表
     */
    Map<String, Object> cacheList(CacheRedisQueryVO vo) throws RedisException;

    /**
     * 缓存 - 详情
     *
     * @param vo 参数
     * @return 缓存详情
     */
    SysRedisDTO cacheDetails(CacheRedisQueryVO vo) throws RedisException;

    /**
     * 缓存 - 创建
     *
     * @param vo 参数
     * @return 是否缓存成功
     */
    Boolean cacheCreate(CacheRedisVO vo) throws RedisException;

    /**
     * 缓存 - 删除
     *
     * @param vo 参数
     * @return 是否成功
     */
    Boolean cacheDelete(CacheRedisVO vo) throws RedisException;

    /**
     * 缓存 - 更新名称
     *
     * @param vo 参数
     * @return 是否成功
     */
    Boolean cacheNameUpdate(CacheRedisVO vo) throws RedisException;

    /**
     * 缓存 - 创建缓存值
     *
     * @param vo 参数
     * @return 是否成功
     */
    Boolean cacheValueCreate(CacheRedisVO vo) throws RedisException;

    /**
     * 缓存 - 更新缓存值
     *
     * @param vo 参数
     * @return 是否成功
     */
    Boolean cacheValueUpdate(CacheRedisVO vo) throws RedisException;

    /**
     * 更新过期时间
     *
     * @param vo 参数
     * @return 是否成功
     */
    Boolean cacheExpireUpdate(CacheRedisVO vo) throws RedisException;

    Boolean cacheSynch(CacheSynchVO vo) throws RedisException;

    Long cachePublish(CachPublishVO vo);
}
