package com.dchealth.web.controller.redis;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.redis.domain.*;
import com.dchealth.redis.exception.RedisException;
import com.dchealth.redis.service.RedisManagerService;
import com.dchealth.redis.service.RedisOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

import static com.dchealth.redis.service.impl.RedisOperationServiceImpl.DATA_LIST;
import static com.dchealth.redis.service.impl.RedisOperationServiceImpl.DATA_TOTAL;

@RestController
@RequestMapping(path = "/redis/cache")
public class RedisController extends BaseController {
    @Autowired
    private RedisOperationService redisOperationService;

    @Autowired
    private RedisManagerService redisManagerService;

    @PreAuthorize("@ss.hasPermi('system:cache:list')")
    @PostMapping(path = "/list")
    public TableDataInfo queryCache(@RequestBody CacheRedisQueryVO vo) throws RedisException {
        Map<String, Object> map = redisOperationService.cacheList(vo);
        return getDataTable((List<?>) map.get(DATA_LIST), (int) map.get(DATA_TOTAL));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:query')")
    @Log(title = "缓存")
    @PostMapping(path = "/details")
    public AjaxResult queryCacheDetails(@RequestBody CacheRedisQueryVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheDetails(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:add')")
    @Log(title = "缓存", businessType = BusinessType.SYNC)
    @PostMapping(path = "/synch")
    public AjaxResult cacheSynch(@RequestBody CacheSynchVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheSynch(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:add')")
    @Log(title = "缓存", businessType = BusinessType.OTHER)
    @PostMapping(path = "/publish")
    public AjaxResult cachePublish(@RequestBody CachPublishVO vo) {
        return AjaxResult.success(redisOperationService.cachePublish(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:add')")
    @Log(title = "缓存", businessType = BusinessType.OTHER)
    @PostMapping(path = "/add")
    public AjaxResult cacheCreate(@RequestBody CacheRedisVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheCreate(vo));
    }


    @PreAuthorize("@ss.hasPermi('system:cache:edit')")
    @Log(title = "更新缓存名称", businessType = BusinessType.UPDATE)
    @PostMapping(path = "/name/update")
    public AjaxResult cacheNameUpdate(@RequestBody CacheRedisVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheNameUpdate(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:edit')")
    @Log(title = "更新缓存失效时间", businessType = BusinessType.UPDATE)
    @PostMapping(path = "/expire/update")
    public AjaxResult cacheExpireUpdate(@RequestBody CacheRedisVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheExpireUpdate(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:edit')")
    @Log(title = "创建缓存值", businessType = BusinessType.INSERT)
    @PostMapping(path = "/value/create")
    public AjaxResult cacheValueCreate(@RequestBody CacheRedisVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheValueCreate(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:edit')")
    @Log(title = "更新缓存值", businessType = BusinessType.UPDATE)
    @PostMapping(path = "/value/update")
    public AjaxResult cacheValueUpdate(@RequestBody CacheRedisVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheValueUpdate(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:remove')")
    @Log(title = "删除缓存", businessType = BusinessType.DELETE)
    @PostMapping(path = "/delete")
    public AjaxResult cacheDelete(@RequestBody CacheRedisVO vo) throws RedisException {
        return AjaxResult.success(redisOperationService.cacheDelete(vo));
    }

    @PreAuthorize("@ss.hasPermi('system:cache:list')")
    @Log(title = "db列表")
    @GetMapping(path = "/db/list")
    public AjaxResult selectDb(BaseVO vo) throws RedisException {
        return AjaxResult.success(redisManagerService.selectDBIndex(vo));
    }

    @Log(title = "测试连接", businessType = BusinessType.OTHER)
    @PostMapping(path = "/test/conn")
    public AjaxResult connection(@RequestBody SysRedisConfig config) {
        try {
            Jedis jedis = redisManagerService.getJedis(config, "0");
            String ping = jedis.ping();
            if (StringUtils.equalsAnyIgnoreCase(ping, "PONG")) {
                jedis.close();
                return AjaxResult.success("redis-server连接成功");
            }
        } catch (Exception e) {
        }
        return AjaxResult.error("redis-server连接出现异常");
    }
}
