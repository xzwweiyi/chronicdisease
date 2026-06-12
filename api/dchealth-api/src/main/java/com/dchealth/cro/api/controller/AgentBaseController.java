package com.dchealth.cro.api.controller;


import cn.hutool.core.net.URLDecoder;
import com.dchealth.cro.api.service.AgentInfoService;
import com.dchealth.cro.api.vo.BaseInfoVo;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/28 13:53
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/info")
public class AgentBaseController extends ApiBaseController {


    @Autowired
    private AgentInfoService agentInfoService;

    @Autowired
    private RsaUtil rsaUtil;

    @GetMapping("bases/{personId}")
    public String getBaseInfoBases(@PathVariable("personId") String personId) {
        if(StringUtils.isNotEmpty(personId)){
            personId = rsaUtil.decryptValue(personId);
        }
        BaseInfoVo result = agentInfoService.getBaseInfoBases(personId);

        return buildSuccessMView(result);
    }

}
