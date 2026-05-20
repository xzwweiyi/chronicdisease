package com.dchealth.cro.minprogram.controller;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.form.MessageAccountNoticeVo;
import com.dchealth.cro.account.form.MessageSearchForm;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.account.service.MessageService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 资讯管理
 */
@Controller
@RequestMapping(value = "/min_program/message")
public class MiniMessageController extends ApiBaseController {
		private static final Logger L = LoggerFactory.getLogger(MiniMessageController.class);
		@Autowired
		private MessageService messageService;
		@Autowired
		private PatientAccountMapper patientAccountMapper;
	@Autowired
	private RsaUtil rsaUtil;

		/**
		 * 权限校验：验证 accountId 是否属于当前登录用户
		 */
		private boolean validateAccountIdAccess(Long accountId, HttpServletRequest request) {
			String deviceToken = request.getHeader("deviceToken");
			if (!StringUtils.isNotBlankAndNotEmpty(deviceToken)) {
				return true; // 无deviceToken时跳过校验（兼容旧逻辑）
			}
			PatientAccount currentAccount = patientAccountMapper.selectPatientAccountByOpenId(deviceToken);
			if (currentAccount == null) {
				L.warn("越权访问尝试: deviceToken={}, 用户不存在", deviceToken);
				return false;
			}
			if (accountId != null && !currentAccount.getAccountId().equals(accountId)) {
				L.warn("越权访问尝试: deviceToken={}, 请求accountId={}, 实际accountId={}",
					deviceToken, accountId, currentAccount.getAccountId());
				return false;
			}
			return true;
		}

		/**
		 * 构建权限拒绝响应
		 */
		private String buildAccessDeniedResponse() {
			DchealthCloudResponseEntity<Object> result = new DchealthCloudResponseEntity<>();
			result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
			result.setMsg("无权访问该用户信息");
			return JSON.toJSONString(result);
		}

		/**
		 * 查询消息列表
		 */
		@ResponseBody
		@RequestMapping(value = "list", method = RequestMethod.POST)
		public String list(@Validated MessageSearchForm messageSearchForm,
						   @RequestAttribute(value = "appType", required = true) String appType,
						   BindingResult bind,
						   HttpServletRequest request) {
			try {
				if (bind.hasErrors()) {
					return buildBindErrorParamMView(bind);
				}
				if(messageSearchForm.getAccountId() !=null){
					messageSearchForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageSearchForm.getAccountId().toString())));
				}
				// 权限校验
				if (!validateAccountIdAccess(messageSearchForm.getAccountId(), request)) {
					return buildAccessDeniedResponse();
				}
				return buildSuccessMView(messageService.list(messageSearchForm,appType));
			} catch (Exception e) {
				L.info("综合查询资讯信息异常{}", JSON.toJSONString(messageSearchForm), e);
				return buildExceptionMView();
			}
		}


		/**
		 * 设置消息为已读
		 */
		@ResponseBody
		@RequestMapping(value = "setMessageRead", method = RequestMethod.POST)
		public String setMessageRead(@Validated MessageAccountNoticeVo messageAccountNoticeVo,
										 @RequestAttribute(value = "appType", required = true) String appType,
										 HttpServletRequest request) {
			if(messageAccountNoticeVo.getAccountId() !=null){
				messageAccountNoticeVo.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageAccountNoticeVo.getAccountId().toString())));
			}
			// 权限校验
			if (!validateAccountIdAccess(messageAccountNoticeVo.getAccountId(), request)) {
				return buildAccessDeniedResponse();
			}
			return buildSuccessMView(messageService.setMessageRead(messageAccountNoticeVo,appType));
		}


		/**
		 *  个人中心红点
		 */
		@ResponseBody
		@RequestMapping(value = "personalCenterRedDot", method = RequestMethod.POST)
		public String personalCenterRedDot(@Validated MessageAccountNoticeVo messageAccountNoticeVo,
											   @RequestAttribute(value = "appType", required = true) String appType,
											   BindingResult bind,
											   HttpServletRequest request){
			try {
				if (bind.hasErrors()) {
					return buildBindErrorParamMView(bind);
				}
				if(messageAccountNoticeVo.getAccountId() !=null){
					messageAccountNoticeVo.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageAccountNoticeVo.getAccountId().toString())));
				}
				// 权限校验
				if (!validateAccountIdAccess(messageAccountNoticeVo.getAccountId(), request)) {
					return buildAccessDeniedResponse();
				}
				return buildSuccessMView(messageService.personalCenterRedDot(messageAccountNoticeVo,appType));
			} catch (Exception e) {
				L.info("获取个人中心红点信息异常{}", JSON.toJSONString(messageAccountNoticeVo), e);
				return buildExceptionMView();
			}
		}

		/**
		 *  获取导航栏红点
		 */
		@ResponseBody
		@RequestMapping(value = "getMenuRedDotVo", method = RequestMethod.POST)
		public String getMenuRedDotVo(MessageAccountNoticeVo messageAccountNoticeVo,
												BindingResult bind,
												HttpServletRequest request){
			try {
				if (bind.hasErrors()) {
					return buildBindErrorParamMView(bind);
				}
				if(messageAccountNoticeVo.getAccountId() !=null){
					messageAccountNoticeVo.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageAccountNoticeVo.getAccountId().toString())));
				}
				// 权限校验
				if (!validateAccountIdAccess(messageAccountNoticeVo.getAccountId(), request)) {
					return buildAccessDeniedResponse();
				}
				return buildSuccessMView(messageService.getMenuRedDotVo(messageAccountNoticeVo,"3"));
			} catch (Exception e) {
				L.info("获取个人中心红点信息异常{}", JSON.toJSONString(messageAccountNoticeVo), e);
				return buildExceptionMView();
			}
		}


	}