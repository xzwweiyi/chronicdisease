package com.dchealth.web.job;


import com.dchealth.DchealthApplication;
import com.dchealth.notice.service.HisNoticeService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/9/10 15:22
 * @Version: 1.0
 */
@SpringBootTest(classes = DchealthApplication.class)
public class TestController {

    @Resource
    private HisNoticeService hisNoticeService;

    @Resource
    private FollowPatientInfoListJob followPatientInfoListJob;

//    @Test
//    public void test(){
//        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.HISMYSQL.name());
//
//        try {
//            // 执行数据库操作
//            hisNoticeService.batchInsert(new ArrayList<>());
//        } finally {
//            // 恢复默认数据源
//            DynamicDataSourceContextHolder.clearDataSourceType();
//        }
//
//        System.out.println("测试");
//    }

    @Test
    public void test1(){
//        DynamicDataSourceContextHolder.setDataSourceType(DataSourceType.HISMYSQL.name());

            // 执行数据库操作
            followPatientInfoListJob.followPatientInfoList();


        System.out.println("测试");
    }
}
