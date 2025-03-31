package com.dchealth.redis.service;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.redis.domain.BaseVO;
import com.dchealth.redis.domain.SelectDBIndexDTO;
import com.dchealth.redis.domain.SysRedisConfig;
import com.dchealth.redis.exception.RedisException;
import redis.clients.jedis.Jedis;

import java.util.List;

public interface RedisManagerService {
    /**
     * 根据host 获取ERR数据库索引
     *
     * @param vo
     * @return ERR数据库索引 集合
     */
    List<SelectDBIndexDTO> selectDBIndex(BaseVO vo) throws RedisException;

    SysRedisConfig getRedisConfig(String host, int port) throws RedisException;

    SysRedisConfig getRedisConfigById(int id) throws RedisException;

    Jedis getJedis(SysRedisConfig redisConfig, String db) throws RedisException;

    List<SysRedisConfig> select(SysUser sysUser);
}
