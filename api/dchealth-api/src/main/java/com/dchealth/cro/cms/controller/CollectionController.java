package com.dchealth.cro.cms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.form.CollectionActicleSearchForm;
import com.dchealth.cro.cms.service.ArticleService;
import com.dchealth.cro.common.base.ApiBaseController;

/**
 * 收藏接口
 */
@Controller
@RequestMapping(value = "/collection")
public class CollectionController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(CollectionController.class);
	@Autowired
	private ArticleService articleService;

	/**
	 * 查询用户收藏信息
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String list(@Validated CollectionActicleSearchForm collectionActicleSearchForm, BindingResult bind,
			@RequestAttribute("channel") String channel) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			collectionActicleSearchForm.setTenantId(channel);
			return buildSuccessMView(articleService.collenctionList(collectionActicleSearchForm));
		} catch (Exception e) {
			L.info("查询用户收藏信息异常{}", JSON.toJSONString(collectionActicleSearchForm), e);
			return buildExceptionMView();
		}
	}
	
	/**
	 * 查询用户收藏信息
	 */
	@ResponseBody
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String list( @RequestParam(value = "accountId", required = true) Long accountId,@RequestParam(value = "articleId", required = true) Long articleId,@RequestAttribute("appType") String appType, @RequestAttribute("channel") String channel) {
		try {
			return buildSuccessMView(articleService.update(accountId,articleId,channel,appType));
		} catch (Exception e) {
			L.info("用户收藏信息异常用户ID:{},文章ID:{},{}",accountId,articleId, e);
			return buildExceptionMView();
		}
	}

	
}
