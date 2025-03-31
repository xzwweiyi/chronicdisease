package com.dchealth.cro.account.controller;

import com.dchealth.cro.account.form.ForgetPasswordForm;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.account.service.DoctorAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.common.crypto.Sm4Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 医生端账号管理
 */
@RestController
@RequestMapping(value = "/doctorAccount")
public class DoctorAccountController extends ApiBaseController {

    private static final Logger L = LoggerFactory.getLogger(DoctorAccountController.class);

    @Autowired
    DoctorAccountService doctorAccountService;

    /**
     * 用户登录
     */
    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(String mobile,
                        @RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel) {
        try {
            boolean b = notEmpty(mobile);
            if (!b) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(doctorAccountService.findUserInfoByMobile(mobile, deviceToken, channel));
        } catch (Exception e) {
            L.info("用户登录异常{}", mobile, e);
            return buildExceptionMView();
        }

    }

    /**
     * 手机号登录
     * @param mobile  手机号
     * @param verifyCode 验证码
     * @param deviceToken
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loginByMobileSms", method = RequestMethod.POST)
    public String loginByMobileSms(String mobile,String verifyCode, @RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel) {
        try {
            boolean b = notEmpty(mobile,verifyCode);
            if (!b) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(doctorAccountService.loginByMobileSms(mobile,verifyCode, deviceToken, channel));
        } catch (Exception e) {
            L.info("用户登录异常账号:{}密码:{}", mobile,verifyCode, e);
            return buildExceptionMView();
        }

    }


    /**
     * 发送验证码--------------废弃
     * @param mobile 手机号或邮箱
     * @param bussinessType 1 注册获取验证码  2 登录 3 忘记密码获取验证码  4 直接发验证码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendAccountVerifyCode", method = RequestMethod.POST)
    public String sendMobileSmsVerifyCode(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind, @RequestAttribute("channel") String channel) {
        if (bind.hasErrors()) {
            return buildBindErrorParamMView(bind);
        }
        sendVerfiyCodeForm.setAppId(channel);
        String mobile = "";
        try {
            mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, sendVerfiyCodeForm.getMobile());
        } catch (Exception e) {
            L.error("手机号有误:{},渠道:{}", sendVerfiyCodeForm.getMobile(), channel);
            return buildErrorParamMView();
        }
        sendVerfiyCodeForm.setMobile(mobile);
        if (sendVerfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {
            return buildSuccessMView(doctorAccountService.sendMobileSmsVerifyCode(sendVerfiyCodeForm));
        }
        return buildErrorParamMView();
    }


    /**
     * 账号密码登录
     * @param loginName 账号
     * @param password 密码
     * @param appType
     * @param deviceToken
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loginByAccount", method = RequestMethod.POST)
    public String loginByAccount(String loginName,String password,
                                 @RequestAttribute("appType") String appType,
                                 @RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel) {
        try {
            boolean b = notEmpty(loginName,password);
            if (!b) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(doctorAccountService.loginBySysUser(loginName,password, appType, deviceToken));
        } catch (Exception e) {
            L.info("用户登录异常账号:{}密码:{}", loginName,password, e);
            return buildExceptionMView();
        }

    }

    /**
     * 忘记密码进行设置密码 根据手机号或邮箱修改登录密码
     */
    @ResponseBody
    @RequestMapping(value = "updatePasswordByAccount", method = RequestMethod.POST)
    public String updatePasswordByAccountId(@Validated ForgetPasswordForm forgetPasswordForm, BindingResult bind) {
        if (bind.hasErrors()) {
            return buildBindErrorParamMView(bind);
        }
        return buildSuccessMView(doctorAccountService.updatePasswordBySysUser(forgetPasswordForm));
    }

}
