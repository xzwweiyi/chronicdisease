package com.dchealth.cro.minprogram.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dchealth.cro.account.service.PatientAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.constant.Constants;

/**
 * 患者账号管理
 */
@RestController
@RequestMapping(value = "/min_program/patientAccount")
public class MinPatientAccountController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(MinPatientAccountController.class);
    @Autowired
    private PatientAccountService patientAccountService;

    /**
     * 验证码登陆
     * @param mobile
     * @param verifyCode
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loginByMobileSms", method = RequestMethod.POST)
    public String loginByMobileSms(String mobile,String verifyCode,@RequestAttribute("openId") String openId) {
        try {
            boolean b = notEmpty(mobile,verifyCode);
            if (!b) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(patientAccountService.loginByMobileSms(mobile,verifyCode, "MIN", Constants.CHANNEL,openId));
        } catch (Exception e) {
            L.info("患者用户登录异常手机号:{}验证码:{}", mobile,verifyCode, e);
            return buildExceptionMView();
        }

    }
    /**
     * 验证码登陆
     * @param mobile
     * @param verifyCode
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loginOut", method = RequestMethod.POST)
    public String loginOut(Long accountId,@RequestAttribute("openId") String openId) {
    	try {
    		boolean b = notEmpty(accountId,openId);
    		if (!b) {
    			return buildErrorParamMView();
    		}
    		return buildSuccessMView(patientAccountService.loginOut(accountId,openId));
    	} catch (Exception e) {
    		L.info("患者用户退出异常账号ID:{}OpenId:{}", accountId,openId, e);
    		return buildExceptionMView();
    	}
    	
    }

    

    /**
     * App登录发送验证码
     * @param mobile 手机号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendAccountVerifyCode", method = RequestMethod.POST)
    public String sendMobileSmsVerifyCode(String mobile) {
        try {
            boolean b = notEmpty(mobile);
            if (!b) {
                return buildErrorParamMView();
            }
                return buildSuccessMView(patientAccountService.sendMobileSmsVerifyCode(Constants.CHANNEL, mobile));
        } catch (Exception e) {
            L.info("发送验证码异常:{}", mobile, e);
            return buildExceptionMView();
        }
    }

}
