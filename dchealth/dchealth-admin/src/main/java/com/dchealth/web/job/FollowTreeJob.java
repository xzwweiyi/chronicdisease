package com.dchealth.web.job;


import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.medical.domain.vo.FollowWorkTree;
import com.dchealth.medical.service.IFollowWorkService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class FollowTreeJob {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFollowWorkService followWorkService;


    /*@XxlJob("followTreeJobHandler")
    public void followTree(){
        try {
            Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
            for (String key : keys) {
                LoginUser loginUser = redisCache.getCacheObject(key);
                FollowWorkTree followerWorkTree = followWorkService.tree(loginUser.getUserId());
                redisCache.setCacheObject(Constants.FOLLOW_TREE_KEY + loginUser.getUserId(), followerWorkTree);
            }
        }catch(Exception ex){
            XxlJobHelper.log("随访树任务异常:", ex.getMessage());
            throw new RuntimeException();
        }
    }*/

//    //lys
//    @XxlJob("followTreeJobHandler")
//    public void followTree(){
//        try {
//            Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
//            for (String key : keys) {
//                LoginUser loginUser = redisCache.getCacheObject(key);
//                // 改为只缓存根节点
//                FollowWorkTree followerWorkTree = followWorkService.treeRoot(loginUser.getUserId());
//                redisCache.setCacheObject(Constants.FOLLOW_TREE_KEY + loginUser.getUserId(), followerWorkTree);
//            }
//        }catch(Exception ex){
//            XxlJobHelper.log("随访树任务异常:", ex.getMessage());
//            throw new RuntimeException();
//        }
//    }
//    //lys

    //lys2
    @XxlJob("followTreeJobHandler")
    public void followTree(){
        try {
            Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
            for (String key : keys) {
                LoginUser loginUser = redisCache.getCacheObject(key);
                // 更新根树缓存
                FollowWorkTree rootTree = followWorkService.rootTree(loginUser.getUserId());
                redisCache.setCacheObject(Constants.FOLLOW_TREE_ROOT_KEY + loginUser.getUserId(), rootTree);

                // 清除所有病种子树缓存，让下次访问时重新加载
                Collection<String> diseaseKeys = redisCache.keys(Constants.FOLLOW_TREE_DISEASE_KEY + loginUser.getUserId() + ":*");
                for (String diseaseKey : diseaseKeys) {
                    redisCache.deleteObject(diseaseKey);
                }
            }
        }catch(Exception ex){
            XxlJobHelper.log("随访树任务异常:", ex.getMessage());
            throw new RuntimeException();
        }
    }
    //lys2

}
