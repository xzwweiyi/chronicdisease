package com.dchealth.cro.transfer.controller;

import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.transfer.service.AccountNoticeService;
import com.dchealth.cro.transfer.vo.AccountNoticeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/transfer/accountNotice")
public class AccountNoticeController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(AccountNoticeController.class);

	@Autowired
	private AccountNoticeService accountNoticeService;

	/**
	 *  获取需要拨打友盟推送的数据,非9点提醒的
	 */
	@ResponseBody
	@RequestMapping(value = "getList", method = RequestMethod.POST)
	public String getList(@Validated AccountNoticeForm accountNoticeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(accountNoticeService.getNoticeList(accountNoticeForm));
	}


	/**
	 *  获取需要拨打友盟推送的数据,9点提醒的，12,13,14,17
	 */
	@ResponseBody
	@RequestMapping(value = "getNoticeNineList", method = RequestMethod.POST)
	public String getNoticeNineList(@Validated AccountNoticeForm accountNoticeForm, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		return buildSuccessMView(accountNoticeService.getNoticeNineList(accountNoticeForm));
	}

}
