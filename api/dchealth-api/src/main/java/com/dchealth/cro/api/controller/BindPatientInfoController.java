package com.dchealth.cro.api.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.api.service.BindPatientInfoService;
import com.dchealth.cro.api.vo.BindPatientVo;
import com.dchealth.cro.common.base.ApiBaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class BindPatientInfoController extends ApiBaseController {

    @Resource
    private BindPatientInfoService bindPatientInfoService;

    @ResponseBody
    @PostMapping(value = "bind")
    public String bindPatientInfo(@RequestBody @Validated BindPatientVo bindPatientVo, BindingResult bind) {

        log.info("bindPatient:{}", JSON.toJSONString(bindPatientVo));
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }

            return buildSuccessMView(bindPatientInfoService.bindPatient(bindPatientVo));
        } catch (Exception e) {
            log.info("绑定患者异常{}", JSON.toJSONString(bindPatientVo), e);
            return buildExceptionMView();
        }
    }
}

