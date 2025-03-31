package com.dchealth.redis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.redis.ConSts;
import com.dchealth.redis.RedisOperationsFactory;
import com.dchealth.redis.domain.CachPublishVO;
import com.dchealth.redis.domain.CacheRedisQueryVO;
import com.dchealth.redis.domain.CacheRedisVO;
import com.dchealth.redis.domain.CacheSynchVO;
import com.dchealth.redis.domain.SysRedis;
import com.dchealth.redis.domain.SysRedisConfig;
import com.dchealth.redis.domain.SysRedisDTO;
import com.dchealth.redis.exception.RedisException;
import com.dchealth.redis.service.RedisManagerService;
import com.dchealth.redis.service.RedisOperationService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

@Service
public class RedisOperationServiceImpl implements RedisOperationService {
    private static final Logger log = LoggerFactory.getLogger(RedisOperationServiceImpl.class);
    public static final String DATA_LIST = "data";
    public static final String DATA_TOTAL = "total";
    @Autowired
    private RedisManagerService redisManagerService;

    @Override
    public Long cachePublish(CachPublishVO vo) {
        Jedis jedis = null;
        try {

            jedis = getJedis(getRedisConfig(vo.getHost(), vo.getPort()), vo.getDb());

            Long publish = jedis.publish(vo.getChannel(), vo.getMessage());

//            log.info("{} 推送消息：{}", vo.getChannel(), vo.getMessage());

            return 200L;
        } catch (RedisException e) {
            e.printStackTrace();
        } finally {
            assert jedis != null;
            jedis.close();
        }
        return 0L;
    }

    @Override
    public Map<String, Object> cacheList(CacheRedisQueryVO vo) throws RedisException {
        Map<String,Object> result = new HashMap<>();
        SysRedisConfig redisConfig = getRedisConfigById(vo.getId());
        Jedis jedis = getJedis(redisConfig, vo.getDb());
        String match = "*";
        if (StringUtils.isNotBlank(vo.getKey())) {
            match = "*" + vo.getKey() + "*";
        }
        ScanParams scanParams = new ScanParams();
        int count = 10000;
        scanParams.count(count);
        scanParams.match(match);
        String scanRet = "0";
        List<String> keyList = new ArrayList<>();
        do {
            ScanResult<String> scanResult = jedis.scan(scanRet, scanParams);
            scanRet = scanResult.getCursor();
            //scan 487439 MATCH * COUNT 10000
            log.info("scan {} MATCH {} COUNT {}", scanRet, vo.getKey(), count);
            keyList.addAll(scanResult.getResult());
        } while (!"0".equals(scanRet));
        if (!CollectionUtils.isEmpty(keyList)) {
            List<SysRedis> list = new ArrayList<>();
            int start = vo.getPageNo() == 1 ? 0 : (vo.getPageNo() - 1) * vo.getPageSize();
            for (int i = start, j = 0; i < keyList.size() && j < vo.getPageSize(); i++, j++) {
                String key = keyList.get(i);
                SysRedis base = baseInfo(key, jedis);
                list.add(base);
            }
            jedis.close();
            int total = keyList.size();
            // 清空加载出来全部
            keyList.clear();
            result.put(DATA_LIST, list);
            result.put(DATA_TOTAL, total);
        } else {
            result.put(DATA_LIST, new ArrayList<>());
            result.put(DATA_TOTAL, 0);
        }
        return result;
    }

    @Override
    public SysRedisDTO cacheDetails(CacheRedisQueryVO vo) throws RedisException {

        SysRedisDTO dto = RedisOperationsFactory.build(vo.getType()).details(getJedis(getRedisConfigById(vo.getId()), vo.getDb()), vo);

        return dto;
    }

    @Override
    public Boolean cacheCreate(CacheRedisVO vo) throws RedisException {

        boolean b = RedisOperationsFactory.build(vo.getDataType()).create(getJedis(getRedisConfigById(vo.getId()), vo.getDb()), vo);

        return new Boolean(b);
    }

    @Override
    public Boolean cacheDelete(CacheRedisVO vo) throws RedisException {

        long count = RedisOperationsFactory.build(vo.getDataType()).delete(getJedis(getRedisConfig(vo.getHost(), vo.getPort()), vo.getDb()), vo);

        return Boolean.TRUE;
    }

    @Override
    public Boolean cacheNameUpdate(CacheRedisVO vo) throws RedisException {

        RedisOperationsFactory.build(vo.getDataType()).rename(getJedis(getRedisConfigById(vo.getId()), vo.getDb()), vo);

        return Boolean.TRUE;
    }

    @Override
    public Boolean cacheValueCreate(CacheRedisVO vo) throws RedisException {
        return cacheCreate(vo);
    }

    @Override
    public Boolean cacheValueUpdate(CacheRedisVO vo) throws RedisException {
        return cacheCreate(vo);
    }

    @Override
    public Boolean cacheExpireUpdate(CacheRedisVO vo) throws RedisException {
        Jedis jedis = getJedis(getRedisConfigById(vo.getId()), vo.getDb());
        try {
            RedisOperationsFactory.build(ConSts.STRIGN).setRedisExpire(jedis, vo.getRedisKey(), vo.getExpire(), vo.getUnit());
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("更新缓存失效时间出现错误", e);
            return Boolean.FALSE;
        } finally {
            jedis.close();
        }
    }

    @Override
    public Boolean cacheSynch(CacheSynchVO vo) throws RedisException {
        // 查询【源】缓存的详情
        CacheRedisQueryVO cacheRedisQueryVo = new CacheRedisQueryVO();
        BeanUtils.copyProperties(vo, cacheRedisQueryVo);
        SysRedisDTO sysRedisDto = cacheDetails(cacheRedisQueryVo);

        if (!Objects.isNull(sysRedisDto)) {
            String type = sysRedisDto.getDataType();

            // 创建【目标】缓存
            CacheRedisVO cacheRedisVo = new CacheRedisVO();
            cacheRedisVo.setHost(vo.getTargetHost());
            cacheRedisVo.setPort(vo.getTargetPort());
            cacheRedisVo.setDb(vo.getTargetDb());
            cacheRedisVo.setDataType(type);

            int expire = StringUtils.isNotBlank(sysRedisDto.getExpire()) ? Integer.parseInt(sysRedisDto.getExpire()) : -1;
            cacheRedisVo.setExpire(expire);

            cacheRedisVo.setRedisKey(sysRedisDto.getRedisKey());

            if (ConSts.STRIGN.equals(type)) {
                cacheRedisVo.setRedisValue(sysRedisDto.getRedisValue());
                return Boolean.TRUE;
            }
            return assemblyCacheSynch(sysRedisDto, cacheRedisVo, vo, cacheRedisQueryVo);
        }

        return Boolean.TRUE;
    }

    private Boolean assemblyCacheSynch(SysRedisDTO sysRedisDto, CacheRedisVO cacheRedisVo, CacheSynchVO vo, CacheRedisQueryVO cacheRedisQueryVo) throws RedisException {
        List<Map<String, Object>> values = sysRedisDto.getValues();

        String type = sysRedisDto.getDataType();

        if (values != null && values.size() > 0) {
            // 循环存储缓存
            AtomicReference<Boolean> responseAtomicReference = new AtomicReference<>();
            values.forEach(val -> {

                if (ConSts.LIST.equals(type) || ConSts.SET.equals(type)) {

                    Object svalue = val.get("svalue");
                    cacheRedisVo.setRedisValue(svalue.toString());

                } else if (ConSts.HASH.equals(type)) {

                    Object hkey = val.get("hkey");
                    Object hvalue = val.get("hvalue");
                    cacheRedisVo.setRedisHKey(hkey.toString());
                    cacheRedisVo.setRedisValue(hvalue.toString());

                } else if (ConSts.ZSET.equals(type)) {
                    Object zvalue = val.get("zvalue");
                    Object zscore = val.get("zscore");

                    cacheRedisVo.setScore(Double.parseDouble(zscore.toString()));
                    cacheRedisVo.setRedisValue(zvalue.toString());
                }

                Boolean response = null;
                try {
                    response = cacheCreate(cacheRedisVo);
                } catch (RedisException e) {
                    e.printStackTrace();
                }
                responseAtomicReference.set(response);
                String msg = response.booleanValue() ? "同步成功" : "同步失败";
                log.info("{} - {} {}", type, sysRedisDto.getRedisKey(), msg);
            });

            if (responseAtomicReference.get().booleanValue()) {
                long pageCount = (values.size() + cacheRedisQueryVo.getPageSize() - 1) / cacheRedisQueryVo.getPageSize();
                int nextPage = cacheRedisQueryVo.getPageNo() + 1;

                log.info("{} - 总条数：{}，总页数：{}，当前页：{}，下一页：{}", type, values.size(), pageCount, cacheRedisQueryVo.getPageNo(), nextPage);
                boolean isNextPage = nextPage <= pageCount;
                log.info("是否自动查询下一页数据：{}", isNextPage);
                if (isNextPage) {
                    vo.setPageNo(nextPage);
                    cacheSynch(vo);
                }
            }

            Boolean response = responseAtomicReference.get();
            String msg = response.booleanValue() ? "同步成功" : "同步失败";
            log.info("{} - {}", type, msg);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private SysRedis baseInfo(String key, Jedis jedis) {
        String type = jedis.type(key);
        Long expire = jedis.ttl(key);
        log.info("key={}，type={}", key, type);
        String expireStr = "";
        if (expire == -1) {
            expireStr = "永不过期";
        } else {
            expireStr = DateUtils.getTimeStrBySecond(expire);
        }

        SysRedis sysRedis = new SysRedis(type, key, null, expireStr);

        Long elCount = 0L;

        if (ConSts.STRIGN.equals(type)) {
            elCount = 1L;
        } else if (ConSts.LIST.equals(type)) {
            elCount = jedis.llen(key);
        } else if (ConSts.HASH.equals(type)) {
            elCount = jedis.hlen(key);
        } else if (ConSts.SET.equals(type)) {
            elCount = jedis.scard(key);
        } else if (ConSts.ZSET.equals(type)) {
            elCount = jedis.zcard(key);
        }

        sysRedis.setElCount(elCount);
        return sysRedis;
    }

    private Jedis getJedis(SysRedisConfig redisConfig, String db) throws RedisException {
        return redisManagerService.getJedis(redisConfig, db);
    }

    private SysRedisConfig getRedisConfig(String host, int port) throws RedisException {
        return redisManagerService.getRedisConfig(host, port);
    }
    private SysRedisConfig getRedisConfigById(int id) throws RedisException {
        return redisManagerService.getRedisConfigById(id);
    }
}
