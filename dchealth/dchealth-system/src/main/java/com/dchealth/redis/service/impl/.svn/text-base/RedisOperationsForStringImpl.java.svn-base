package com.dchealth.redis.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.redis.ConSts;
import com.dchealth.redis.domain.CacheRedisVO;
import com.dchealth.redis.domain.SysRedisDTO;
import com.dchealth.redis.service.RedisOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisOperationsForStringImpl extends RedisOperations {

    @Override
    public SysRedisDTO getInfo(Jedis jedis, String key) {
        SysRedisDTO dto = new SysRedisDTO();

        String value = jedis.get(key);
        dto.setRedisValue(value);

        dto.setDataType(ConSts.STRIGN);

        Long expire = jedis.ttl(key);

        String expireStr = "";
        if (expire == -1) {
            expireStr = "永不过期";
        } else {
            expireStr = DateUtils.getTimeStrBySecond(expire);
        }
        // 过期时间 数值类型
        dto.setExpire(expire.toString());
        dto.setExpireStr(expireStr);

        dto.setElCount(1L);
        dto.setRedisKey(key);
        dto.setOldRedisKey(key);

        return dto;
    }

    @Override
    public boolean set(Jedis jedis, CacheRedisVO vo) {

        jedis.set(vo.getRedisKey(), vo.getRedisValue());

        return true;
    }

    @Override
    protected long del(Jedis jedis, CacheRedisVO vo) {

        return jedis.del(vo.getRedisKey());
    }
}
