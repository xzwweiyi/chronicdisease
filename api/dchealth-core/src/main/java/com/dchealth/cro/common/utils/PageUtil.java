package com.dchealth.cro.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @Auther: cuitao
 * @Date: 2023/4/28 - 04 - 28 - 9:20
 * @Description: com.dchealth.cro.common.utils
 */
public class PageUtil {
    private static final Logger L = LoggerFactory.getLogger(PageUtil.class);

    public static Boolean verifyPage(Long pageNo, Long pageSize) {
        if (Objects.isNull(pageNo) || Objects.isNull(pageSize)) {
            L.error("分页信息为空 pageNo:{} pageSize:{}", pageNo, pageSize);
            return false;
        }
        if (pageNo < 0) {
            L.error("初始页码不能小于0 pageNo:{}", pageNo);
            return false;
        }
        if (pageSize < 0) {
            L.error("单页查询数目不能小于0 pageSize:{}", pageSize);
            return false;
        }
        return true;
    }

}
