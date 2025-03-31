package com.dchealth.cro.account.controller;

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

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.form.MessageAccountNoticeVo;
import com.dchealth.cro.account.form.MessageSearchForm;
import com.dchealth.cro.account.service.MessageService;
import com.dchealth.cro.common.base.ApiBaseController;

/**
 * 资讯管理
 */
@Controller
@RequestMapping(value = "/message")
public class MessageController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private MessageService messageService;

	/**
	 * 查询消息列表
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String list(@Validated MessageSearchForm messageSearchForm,
					   @RequestAttribute(value = "appType", required = true) String appType,BindingResult bind) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
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
								 @RequestAttribute(value = "appType", required = true) String appType) {
		return buildSuccessMView(messageService.setMessageRead(messageAccountNoticeVo,appType));
	}


	/**
	 *  个人中心红点
	 */
	@ResponseBody
	@RequestMapping(value = "personalCenterRedDot", method = RequestMethod.POST)
	public String personalCenterRedDot(@Validated MessageAccountNoticeVo messageAccountNoticeVo,
									   @RequestAttribute(value = "appType", required = true) String appType, BindingResult bind){
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
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
	public String getMenuRedDotVo(@Validated MessageAccountNoticeVo messageAccountNoticeVo,
									   @RequestAttribute(value = "appType", required = true) String appType, BindingResult bind){
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			return buildSuccessMView(messageService.getMenuRedDotVo(messageAccountNoticeVo,appType));
		} catch (Exception e) {
			L.info("获取个人中心红点信息异常{}", JSON.toJSONString(messageAccountNoticeVo), e);
			return buildExceptionMView();
		}
	}

	/**
	 *  患者端消息详情
	 */
	@ResponseBody
	@RequestMapping(value = "getMessageVo", method = RequestMethod.POST)
	public String getMessageVo(Long messageId, @RequestAttribute(value = "appType", required = true) String appType){
		try {

			return buildSuccessMView(messageService.getMessageVo(messageId,appType));
		} catch (Exception e) {
			L.info("获取患者端消息详情信息异常{}", JSON.toJSONString(messageId), e);
			return buildExceptionMView();
		}
	}

	/**
	 *  获取导航栏红点
	 */
	@ResponseBody
	@RequestMapping(value = "getPopHanderInfo", method = RequestMethod.POST)
	public String getPopHanderInfo(@Validated MessageAccountNoticeVo messageAccountNoticeVo,
								  @RequestAttribute(value = "appType", required = true) String appType, BindingResult bind){
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			return buildSuccessMView(messageService.getPopHanderInfo(messageAccountNoticeVo,appType));
		} catch (Exception e) {
			L.info("获取个人中心红点信息异常{}", JSON.toJSONString(messageAccountNoticeVo), e);
			return buildExceptionMView();
		}
	}


}
