package com.dchealth.cro.base.controller;

import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.MiniProgramBannerShowService;
import com.dchealth.cro.common.exception.base.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiniProgramBannerShowController {
    @Autowired
    private MiniProgramBannerShowService miniProgramBannerShowService;

    @RequestMapping("getIsShow")
    public DchealthCloudResponseEntity<String> getIsShow() {
        try {
            DchealthCloudResponseEntity<String> isShow = miniProgramBannerShowService.getIsShow();
            return isShow;
        } catch (Exception e) {
            throw new ServiceException("获取设置异常");
        }
    }
}
