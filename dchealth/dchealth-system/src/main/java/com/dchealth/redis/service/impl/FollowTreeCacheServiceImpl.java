package com.dchealth.redis.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.listener.FollowTreeEvent;
import com.dchealth.medical.domain.vo.FollowWorkTree;
import com.dchealth.medical.service.IFollowWorkService;
import com.dchealth.redis.service.FollowTreeCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public Boolean cacheFollowTree(FollowTreeEvent followTreeEvent) {
        log.info("cacheFollowTree");
        try {
            if (followTreeEvent.getFlag()) {
                // 监听到编辑操作，进行缓存更新，二级树节点对应的待处理条数 - 1
//                Collection<String> keys = redisCache.keys(Constants.TOTAL_TREE_LEVEL_NUM + followTreeEvent.getUserId());


                if (followTreeEvent.getDiseasesId() == null) {
                    log.info("随访树缓存异常，病种ID为空");
                    if (CurrentOneSum(followTreeEvent)) return Boolean.TRUE;
                } else {

                    redisCache.setCacheObject(Constants.ONE_TREE_LEVEL_NUM + followTreeEvent.getUserId() + ":" + followTreeEvent.getDiseasesId(), followTreeEvent.getNum());
                    Collection<String> keys = redisCache.keys(Constants.ONE_TREE_LEVEL_NUM + followTreeEvent.getUserId() + ":" + "*");
                    int total = keys.stream().mapToInt(key -> {
                        return redisCache.getCacheObject(key);
                    }).sum();
                    redisCache.setCacheObject(Constants.TOTAL_TREE_LEVEL_NUM + followTreeEvent.getUserId(), total);
                }

            } else {
                // 如果false， 重新加载一级树节点
                if (CurrentOneSum(followTreeEvent)) return Boolean.TRUE;
            }

        } catch (Exception ex) {
            log.info("随访树任务异常:{}", ex.getMessage());
            throw new RuntimeException();
        }
        return Boolean.TRUE;
    }


    private boolean CurrentOneSum(FollowTreeEvent followTreeEvent) {
        List<Long> list = followWorkService.rootTree(followTreeEvent.getUserId())
                .getChildrenList().stream().map(FollowWorkTree::getDiseaseId).collect(Collectors.toList());
        if (ObjectUtil.isEmpty(list)) {
            return false;
        }

        FollowWorkTree followWorkTree = null;
        List<Integer> total = new ArrayList<>();
        for (Long l : list) {
            followWorkTree = followWorkService.diseaseTree(followTreeEvent.getUserId(), l);
            //对对象内num求和
            int sum = followWorkTree.getChildrenList().stream().mapToInt(FollowWorkTree::getHandleNum).sum();
            redisCache.setCacheObject(Constants.ONE_TREE_LEVEL_NUM + followTreeEvent.getUserId() + ":" + l, String.valueOf(sum));
            total.add(sum);
        }

        int result = total.stream().mapToInt(Integer::intValue).sum();
        redisCache.setCacheObject(Constants.TOTAL_TREE_LEVEL_NUM + followTreeEvent.getUserId(), String.valueOf(result));
        return true;
    }

    @Override
    public Boolean clearCacheFollowTree() {
        return null;
    }

//    @Override
//    public Boolean cacheFollowTree(Long userId) {
//        log.info("cacheFollowTree");
//        try {
//            // 改为只缓存根节点
//            FollowWorkTree followerWorkTree = followWorkService.treeRoot(userId);
//            redisCache.setCacheObject(Constants.FOLLOW_TREE_KEY + userId, followerWorkTree);
//        }catch(Exception ex){
//            log.info("随访树任务异常:{}", ex.getMessage());
//            throw new RuntimeException();
//        }
//        return Boolean.TRUE;
//    }
//lys2
//    @Override
//    public Boolean cacheFollowTree(FollowTreeEvent followTreeEvent) {
//        log.info("cacheFollowTree");
//        try {
//            // 缓存根树
//            FollowWorkTree rootTree = followWorkService.rootTree(userId);
//            redisCache.setCacheObject(Constants.FOLLOW_TREE_ROOT_KEY + userId, rootTree);
//
//            // 清除病种子树缓存
//            Collection<String> diseaseKeys = redisCache.keys(Constants.FOLLOW_TREE_DISEASE_KEY + userId + ":*");
//            for (String diseaseKey : diseaseKeys) {
//                redisCache.deleteObject(diseaseKey);
//            }
//        }catch(Exception ex){
//            log.info("随访树任务异常:{}", ex.getMessage());
//            throw new RuntimeException();
//        }
//        return Boolean.TRUE;
//    }
}
