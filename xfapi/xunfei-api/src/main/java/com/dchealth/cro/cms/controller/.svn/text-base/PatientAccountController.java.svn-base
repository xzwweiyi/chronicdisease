package com.dchealth.cro.cms.controller;

import com.dchealth.cro.cms.form.SendVerfiyCodeForm;
import com.dchealth.cro.cms.service.DoctorAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.crypto.Sm4Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping(value = "/patientAccount")
public class PatientAccountController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(PatientAccountController.class);
	@Autowired
	private DoctorAccountService doctorAccountService;


	/**
	 *  患者端短信
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
			L.info("患者端发送短信手机号为:{}", mobile);
		} catch (Exception e) {
			L.error("手机号有误:{}", sendVerfiyCodeForm.getMobile());
			return buildErrorParamMView();
		}
		return buildSuccessMView(doctorAccountService.sendPatientVerifyCode(sendVerfiyCodeForm));
	}

	/**
	 *  小程序乡患者端短信
	 */
	@ResponseBody
	@RequestMapping(value = "sendAccountMinVerifyCode", method = RequestMethod.POST)
	public String sendVerifyCodeMin(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		sendVerfiyCodeForm.setAppId("1");
		L.info("小程序端发送短信手机号为:{}", sendVerfiyCodeForm.getMobile());
		return buildSuccessMView(doctorAccountService.sendPatientMinVerifyCode(sendVerfiyCodeForm));
	}

}
