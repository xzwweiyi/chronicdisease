package com.dchealth.notice.service.impl;


import com.dchealth.notice.mapper.HisNoticeMapper;
import com.dchealth.notice.noticevo.HisNoticeVo;
import com.dchealth.notice.service.HisNoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/9/10 17:35
 * @Version: 1.0
 */
@Slf4j
@Service

public class HisNoticeServiceImpl implements HisNoticeService {

    @Resource
    private HisNoticeMapper hisNoticeMapper;

    @Override
//    @DataSource(DataSourceType.HISMYSQL)
    public int batchInsert(List<HisNoticeVo> hisNoticeVo) {
        int count = hisNoticeMapper.batchInsert(hisNoticeVo);
        log.info("批量插入数据成功，数据量：{}", count);
        return count;
    }
}
