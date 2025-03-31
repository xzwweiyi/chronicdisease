package com.dchealth.redis.service.impl;

import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.medical.domain.vo.FollowWorkTree;
import com.dchealth.medical.service.IFollowWorkService;
import com.dchealth.redis.service.FollowTreeCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FollowTreeCacheServiceImpl implements FollowTreeCacheService {

    private static final Logger log = LoggerFactory.getLogger(FollowTreeCacheServiceImpl.class);
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFollowWorkService followWorkService;

    @Override
    public Boolean cacheFollowTree() {
        return null;
    }

    @Override
    public Boolean cacheFollowTree(Long userId) {
        log.info("cacheFollowTree");
        try {
//            Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
//            for (String key : keys) {
//                LoginUser loginUser = redisCache.getCacheObject(Constants.LOGIN_TOKEN_KEY+userId);
                FollowWorkTree followerWorkTree = followWorkService.tree(userId);
                redisCache.setCacheObject(Constants.FOLLOW_TREE_KEY + userId, followerWorkTree);
//            }
        }catch(Exception ex){
            log.info("随访树任务异常:{}", ex.getMessage());
            throw new RuntimeException();
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean clearCacheFollowTree() {
        return null;
    }
}
