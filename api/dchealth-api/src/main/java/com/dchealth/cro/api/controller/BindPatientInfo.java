package com.dchealth.cro.api.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.api.service.BindPatientInfoService;
import com.dchealth.cro.api.vo.BindPatientVo;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.medical.form.BindPatientForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class BindPatientInfo extends ApiBaseController {

    @Resource
    private BindPatientInfoService bindPatientInfoService;

    @ResponseBody
    @RequestMapping(value = "bind", method = RequestMethod.POST)
    public String bindPatientInfo( BindPatientVo bindPatientVo, BindingResult bind) {
        BindPatientForm bindPatientForm = new BindPatientForm();
        bindPatientForm.setIdCardNo(bindPatientVo.getCard());
        bindPatientForm.setRegistrySigns(bindPatientVo.getPatientId());
        bindPatientForm.setMobile(bindPatientVo.getMobile());
        log.info("bindPatient:{}", JSON.toJSONString(bindPatientForm));
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            return buildSuccessMView(bindPatientInfoService.bindPatient(bindPatientForm));
        } catch (Exception e) {
            log.info("绑定患者异常{}", JSON.toJSONString(bindPatientForm), e);
            return buildExceptionMView();
        }
    }
}

