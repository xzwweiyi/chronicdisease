package com.dchealth.cro.common.cache;

import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Slf4j
public class MiniProgramBannerShowService {

    @Resource
    private RedisTemplate redisTemplate;

    public DchealthCloudResponseEntity<String> getIsShow() {
        DchealthCloudResponseEntity<String> ajaxResult = new DchealthCloudResponseEntity<String>();
        // 设置显示标志
        Object isShow = redisTemplate.opsForValue().get("isShow");
        log.info("获取开关缓存值isShow=",isShow);
        if (Objects.isNull(isShow)) {
            isShow = "false";
        }
        ajaxResult.setData((String) isShow);
        return ajaxResult;
    }
}
