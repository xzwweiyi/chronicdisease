package com.dchealth.cro.minprogram.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.account.service.PatientAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.medical.form.PatientLoginForm;
import com.dchealth.cro.minprogram.service.MinProgramService;
import com.dchealth.cro.research.service.ResearchInfoService;

/**
 * 无登录请求方法都写入此类
 *
 */
@RestController
@RequestMapping(value = "/min_program")
public class MinProgramController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(MinProgramController.class);
	@Autowired
	private ResearchInfoService researchInfoService;
	@Autowired
	private MinProgramService minProgramService;
	@Autowired
	private PatientAccountService patientAccountService;
	/**
	 * 项目介绍详情
	 */
	@ResponseBody
	@RequestMapping(value = "detailStandard", method = RequestMethod.POST)
	public DchealthCloudResponseEntity<String> detailStandard(@RequestParam(value = "researchId", required = true)  Long researchId  , HttpServletResponse response) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
			return researchInfoService.detailContent(researchId);
		} catch (Exception e) {
			L.info("查询研究项内容目详情异常researchId:{},type:{}",researchId);
			DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
			result.setMsg(CroApiCodeConstant.SYSTEM_ERROR.getMsg());	
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());	
			return result;
		}

	}
	
	
	
	/**
	 * 用户注册 微信用户授权时调用
	 * 
	 * @param accountVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOpenId", method = RequestMethod.POST)
	public String  getOpenId(String  accountCode,String tenantId) {
		try {
			boolean b = notEmpty(accountCode,tenantId);
			if (!b) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(minProgramService.getOpenId(accountCode,tenantId));

		} catch (Exception e) {
			L.error("获取用户OpenId异常{}",accountCode, e);
			return buildExceptionMView();
		}
	}
	
	/**
	 *  根据OpenId 获取患者信息
	 * 
	 * @param accountVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPatientInfoByOpenId", method = RequestMethod.POST)
	public String  getUserByOpenId(String  openId) {
		try {
			boolean b = notEmpty(openId);
			if (!b) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(patientAccountService.loginByOpenId(openId));
			
		} catch (Exception e) {
			L.error("根据OpenId获取患者信息异常{}",openId, e);
			return buildExceptionMView();
		}
	}
	/**
	 *  根据身份证号获取患者信息
	 * 
	 * @param accountVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String  login(@Validated PatientLoginForm patientLoginForm, BindingResult bind) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			return buildSuccessMView(minProgramService.login(patientLoginForm));
			
		} catch (Exception e) {
			L.error("根据OpenId获取患者信息异常{}",JSON.toJSONString(patientLoginForm), e);
			return buildExceptionMView();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "loginByMobile", method = RequestMethod.POST)
	public String loginByMobile( PatientLoginForm patientLoginForm) {
		try {
			L.info("登录参数:",JSON.toJSONString(patientLoginForm));
			boolean b = notEmpty(patientLoginForm,patientLoginForm.getMobile(),patientLoginForm.getVerifyCode());
			if (!b) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(minProgramService.loginByMobile(patientLoginForm));
		} catch (Exception e) {
			L.info("用户登录异常:{}", JSON.toJSONString(patientLoginForm), e);
			return buildExceptionMView();
		}

	}
	/**
	 *  首页信息
	 * 
	 * @param accountVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "index", method = RequestMethod.POST)
	public String  index(@Validated PatientLoginForm patientLoginForm, BindingResult bind) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			return buildSuccessMView(minProgramService.minIndex(patientLoginForm));
			
		} catch (Exception e) {
			L.error("根据OpenId获取患者信息异常{}",JSON.toJSONString(patientLoginForm), e);
			return buildExceptionMView();
		}
	}


		
		/**
		 * 发送验证码
		 * @param mobile 手机号或邮箱
		 * @param bussinessType 1 注册获取验证码  2 登录 3 忘记密码获取验证码  4 直接发验证码
		 * @return
		 */
		@ResponseBody
		@RequestMapping(value = "sendMobileCode", method = RequestMethod.POST)
		public String sendMobileCode(@Validated SendVerfiyCodeForm sendVerfiyCodeForm, BindingResult bind) {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			if (sendVerfiyCodeForm.getMobile().matches(PatternConstant.PHONE) ) {
				return buildSuccessMView(minProgramService.sendMobileSmsVerifyCode(sendVerfiyCodeForm));
			}
			return buildErrorParamMView();
		}
		
	 
	
	

}
