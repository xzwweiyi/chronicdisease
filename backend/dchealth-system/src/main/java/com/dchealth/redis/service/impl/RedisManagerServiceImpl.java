package com.dchealth.redis.service.impl;


import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.utils.AESUtils;
import com.dchealth.redis.domain.BaseVO;
import com.dchealth.redis.domain.SelectDBIndexDTO;
import com.dchealth.redis.domain.SysRedisConfig;

import com.dchealth.redis.exception.RedisException;
import com.dchealth.redis.mapper.SysRedisConfigMapper;
import com.dchealth.redis.service.RedisManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RedisManagerServiceImpl implements RedisManagerService {

    @Autowired
    private SysRedisConfigMapper redisConfigRepository;

    @Override
    public List<SysRedisConfig> select(SysUser sysUser) {
        List<SysRedisConfig> list = new ArrayList<>();
        List<SysRedisConfig> sysUserHostList = redisConfigRepository.selectSysRedisConfigList(null);
        for (SysRedisConfig redisConfig : sysUserHostList) {
            if (redisConfig != null && Boolean.TRUE.equals(redisConfig.getStatus())) {
                list.add(redisConfig);
            }
        }
        return list;
    }

    @Override
    public Jedis getJedis(SysRedisConfig redisConfig, String db) throws RedisException {
        SysRedisConfig sysRedisConfig = redisConfigRepository.selectSysRedisConfigById(redisConfig.getId());
        try {
            JedisShardInfo shardInfo = new JedisShardInfo("redis://" + sysRedisConfig.getHost() + ":" + sysRedisConfig.getPort() + "/" + db);
            if (StringUtils.isNotBlank(redisConfig.getPassword())) {
                shardInfo.setPassword(AESUtils.decrypt(redisConfig.getPassword()));
            } else {
                shardInfo.setPassword(null);
            }
            return new Jedis(shardInfo);

        } catch (Exception e) {
            throw new RedisException(e.getMessage());
        }
    }

    @Override
    public SysRedisConfig getRedisConfig(String host, int port) throws RedisException {
        List<SysRedisConfig> redisConfig = redisConfigRepository.selectSysRedisConfigList(new SysRedisConfig(host, port));
        if (CollectionUtils.isEmpty(redisConfig)) {
            throw new RedisException("redis-server配置不存在");
        }
        return redisConfig.get(0);
    }

    @Override
    public SysRedisConfig getRedisConfigById(int id) throws RedisException {
        SysRedisConfig sysRedisConfig = redisConfigRepository.selectSysRedisConfigById(new Integer(id).longValue());
        if (Objects.isNull(sysRedisConfig)) {
            throw new RedisException("redis-server:" + id + "不存在");
        }
        return sysRedisConfig;
    }

    @Override
    public List<SelectDBIndexDTO> selectDBIndex(BaseVO vo) throws RedisException {

        SysRedisConfig redisConfig =  redisConfigRepository.selectSysRedisConfigById(Long.valueOf(vo.getId()));

        if (Objects.isNull(redisConfig)) {
            throw new RedisException("redis-server不存在");
        }
        try {
            redisConfig.setPassword(AESUtils.decrypt(redisConfig.getPassword()));
        } catch (Exception e) {
           throw new RedisException(e.getMessage());
        }
        // 创建redis连接
        JedisShardInfo jedisShardInfo = new JedisShardInfo(redisConfig.getHost(), redisConfig.getPort());
        if (StringUtils.isNotBlank(redisConfig.getPassword())) {
            jedisShardInfo.setPassword(redisConfig.getPassword());
        } else {
            jedisShardInfo.setPassword(null);
        }

        Jedis jedis = new Jedis(jedisShardInfo);

        List<SelectDBIndexDTO> list = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            log.info("SELECT {}", i);

            try {
                // 检查db是否连接扫描成功
                String selectDb = jedis.select(i);
                log.info(selectDb);

                SelectDBIndexDTO dbIndex = new SelectDBIndexDTO();
                dbIndex.setName("db" + i);
                dbIndex.setDb(i + "");
                list.add(dbIndex);
            } catch (Exception e) {
                log.info(e.getMessage());
                if (e instanceof JedisConnectionException) {
                    if (e.getMessage().contains("Unknown reply: N")) {
                        throw new RedisException("连接redis-server异常：" + e.getMessage());
                    }
                }
                if (e instanceof JedisDataException) {
                    if (e.getMessage().contains("ERR invalid password")) {
                        throw new RedisException("连接redis-server密码错误：" + e.getMessage());
                    }

                    if (e.getMessage().contains("NOAUTH Authentication required")) {
                        throw new RedisException("连接redis-server需要密码认证：" + e.getMessage());
                    }

                    if (e.getMessage().contains("ERR DB index is out of range")) {
                        // 查询数据库下标越界时 跳出循环
                        break;
                    }
                }

            }
        }

        // 关闭连接
        jedis.close();

        if (list.size() > 0) {
            return list;
        }

        // ERR DB index is out of range ：ERR数据库索引超出范围
        return new ArrayList<>();
    }
}
