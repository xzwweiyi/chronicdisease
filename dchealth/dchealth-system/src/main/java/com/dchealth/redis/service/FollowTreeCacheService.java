package com.dchealth.redis.service;

import com.dchealth.listener.FollowTreeEvent;

/**
 * 随访加载树缓存服务
 */
public interface FollowTreeCacheService {
    /**
     * 缓存随访
     */
    public Boolean cacheFollowTree();

    /**
     * 获取缓存待处理数据
     * @param followTreeEvent
     * @return
     */
    Boolean cacheFollowTree(FollowTreeEvent followTreeEvent);

    /**
     * 清理随访
     */
     Boolean clearCacheFollowTree();
}
