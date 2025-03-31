package com.dchealth.cro.cms.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.cms.form.SendVerfiyCodeForm;
import com.dchealth.cro.cms.service.DoctorAccountService;
import com.dchealth.cro.cms.vo.SendTencentSmsVO;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.crypto.Sm4Util;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping(value = "/doctorAccount")
public class DoctorAccountController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(DoctorAccountController.class);
	@Autowired
	private DoctorAccountService doctorAccountService;


	// todo 这个也可以发送短信
	/**
	 *  医生端短信
	 */
	@ResponseBody
	@RequestMapping(value = "sendAccountVerifyCode", method = RequestMethod.POST)
	public String sendVerifyCode(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		sendVerfiyCodeForm.setAppId("1");
		String mobile = "";
		try {
			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY, sendVerfiyCodeForm.getMobile());
			L.info("医生端发送短信手机号为:{}", mobile);
		} catch (Exception e) {
			L.error("手机号有误:{}", sendVerfiyCodeForm.getMobile());
			return buildErrorParamMView();
		}
		return buildSuccessMView(doctorAccountService.sendAccountVerifyCode(sendVerfiyCodeForm));
	}


//	/**
//	 *  发送邀请链接
//	 */
////	@ResponseBody
//	@RequestMapping(value = "sendJoinResearch", method = RequestMethod.POST)
//	public String sendJoinResearch(@RequestBody Map<String,String> sendVerfiyCodeForm, BindingResult bind) {
//		L.info("sendJoinResearch="+ JSON.toJSONString(json));
////		if (bind.hasErrors()) {
////			return buildBindErrorParamMView(bind);
////		}
////		sendVerfiyCodeForm.setAppId("1");
////		L.info("小程序端发送短信手机号为:{}", sendVerfiyCodeForm.getMobile());
//		JSONObject parse = JSON.parseObject(json,JSONObject.class);
//
//		return buildSuccessMView(doctorAccountService.sendPatientJoinResearch(parse));
//	}

	/**
	 *  发送邀请链接
	 */
//	@ResponseBody
//	@RequestMapping(value = "sendJoinResearchSms", method = RequestMethod.POST)
//	public String sendJoinResearchSms(@RequestBody SendTencentSmsVO sendTencentSmsVO, BindingResult bind) {
//		L.info("sendJoinResearch="+ JSON.toJSONString(sendTencentSmsVO));
//		if (bind.hasErrors()) {
//			return buildBindErrorParamMView(bind);
//		}
//		return buildSuccessMView(doctorAccountService.sendPatientJoinResearch(sendTencentSmsVO));
//	}

}
