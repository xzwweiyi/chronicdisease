package com.dchealth.cro.account.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dchealth.cro.account.form.ForgetPasswordForm;
import com.dchealth.cro.account.form.VerfiyCodeForm;
import com.dchealth.cro.account.service.AccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.common.crypto.Sm4Util;
import com.dchealth.cro.common.enums.AccountType;
import com.dchealth.cro.common.utils.StringUtils;

/**
 * 无登录请求方法都写入此类
 *
 */
@RestController
@RequestMapping(value = "/user")
public class UserController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private AccountService accountService;

	/**
	 * 用户登录 appType 1 医生端 2患者端
	 */
	@ResponseBody
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(String mobile,
			@RequestParam(value = "appType", defaultValue = "1", required = true) String appType,
			@RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel) {
		try {
			boolean b = notEmpty(mobile);
			if (!b) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(accountService.findUserInfoByMobile(mobile, appType, deviceToken, channel));
		} catch (Exception e) {
			L.info("用户登录异常{}", mobile, e);
			return buildExceptionMView();
		}

	}
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
			return buildSuccessMView(accountService.loginBySysUser(loginName,password, appType, deviceToken, channel));
		} catch (Exception e) {
			L.info("用户登录异常账号:{}密码:{}", loginName,password, e);
			return buildExceptionMView();
		}
		
	}
	@ResponseBody
	@RequestMapping(value = "loginByMobileSms", method = RequestMethod.POST)
	public String loginByMobileSms(String mobile,String verifyCode,
			@RequestAttribute(value = "appType") String appType,
			@RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel) {
		try {
			boolean b = notEmpty(mobile,verifyCode);
			if (!b) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(accountService.loginByMobileSms(mobile,verifyCode, appType, deviceToken, channel));
		} catch (Exception e) {
			L.info("用户登录异常账号:{}密码:{}", mobile,verifyCode, e);
			return buildExceptionMView();
		}
		
	}
	
	
	/**
	 * 专家详情
	 */
	@ResponseBody
	@RequestMapping(value = "detailDoctor", method = RequestMethod.POST)
	public String detailDoctor(@RequestParam(value = "doctorId", required = true) Long doctorId,
							   @RequestAttribute(value ="appType", required = true)String appType,@RequestAttribute("channel") String channel) {
		try {
			return buildSuccessMView(accountService.detailDoctor(doctorId,appType,channel));
		} catch (Exception e) {
			L.info(" 专家详情异常{}", doctorId, e);
			return buildExceptionMView();
		}

	}
	
	
	

	/**
	 * 查询账号是否注册
	 */
	@ResponseBody
	@RequestMapping(value = "existSysUser", method = RequestMethod.POST)
	public String existSysUser(String account) {
		boolean b = notEmpty(account);
		if (!b) {
			return buildErrorParamMView();
		}
		if (account.matches(PatternConstant.PHONE)) {

			return buildSuccessMView(accountService.existSysUser(account, AccountType.PHONE.getCode()));
		}

		if (account.matches(PatternConstant.EMAIL)) {
			return buildSuccessMView(accountService.existSysUser(account, AccountType.EMAIL.getCode()));
		}
		return buildErrorParamMView();
	}
	
	/**
	 * 发送验证码
	 * @param mobile 手机号或邮箱
	 * @param bussinessType 1 注册获取验证码  2 登录 3 忘记密码获取验证码  4 直接发验证码
	 * @return
	 */
//	@ResponseBody
//	@RequestMapping(value = "sendAccountVerifyCode", method = RequestMethod.POST)
//	public String sendMobileSmsVerifyCode(@Validated SendVerfiyCodeForm sendVerfiyCodeForm,BindingResult bind,@RequestAttribute("channel") String channel) {
//		if (bind.hasErrors()) {
//			return buildBindErrorParamMView(bind);
//		}
//		sendVerfiyCodeForm.setAppId(channel);
//		String mobile = "";
//		try {
//			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, sendVerfiyCodeForm.getMobile());
//		} catch (Exception e) {
//			L.error("手机号有误:{},渠道:{}", sendVerfiyCodeForm.getMobile(), channel);
//			return buildErrorParamMView();
//		}
//		sendVerfiyCodeForm.setMobile(mobile);
//		if (sendVerfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {
//			return buildSuccessMView(accountService.sendMobileSmsVerifyCode(sendVerfiyCodeForm));
//		}
//		return buildErrorParamMView();
//	}

	@ResponseBody
	@RequestMapping(value = "checkAccount", method = RequestMethod.POST)
	public String checkAccount(@RequestParam("phone") String phone,@RequestAttribute("channel") String channel) {
		if (!StringUtils.isNotNullOrEmpty(phone)) {
			return buildErrorParamMView();
		}
		String mobile = "";
		try {
			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, phone);
		} catch (Exception e) {
			L.error("手机号有误:{},渠道:{}", mobile, channel);
			return buildErrorParamMView();
		}
		if (mobile.matches(PatternConstant.PHONE) ) {
			return buildSuccessMView(accountService.checkSysUser(mobile));
		}
		return buildErrorParamMView();
	}

	/**
	 * 校验手机验证码 新手机号获取校验码的
	 * 
	 * @param phone 手机号
	 */
	@ResponseBody
	@RequestMapping(value = "checkVerifyCode", method = RequestMethod.POST)
	public String checkVerifyCode(@Validated VerfiyCodeForm  verfiyCodeForm,BindingResult bind,@RequestAttribute("channel") String channel) {
		
	
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		verfiyCodeForm.setTenantId(channel);
		String mobile = "";
		try {
			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, verfiyCodeForm.getMobile());
		} catch (Exception e) {
			L.error("手机号有误:{},渠道:{}", verfiyCodeForm.getMobile(), channel);
			return buildErrorParamMView();
		}
		verfiyCodeForm.setMobile(mobile);
		if (verfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {

			return buildSuccessMView(accountService.checkVerifyCode(verfiyCodeForm));
		}else {
			return buildErrorParamMView();
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
		return buildSuccessMView(accountService.updatePasswordBySysUser(forgetPasswordForm));
	}


}
