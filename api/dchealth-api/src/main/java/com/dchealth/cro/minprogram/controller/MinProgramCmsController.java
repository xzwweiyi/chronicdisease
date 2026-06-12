package com.dchealth.cro.minprogram.controller;

import com.dchealth.cro.common.utils.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.service.ArticleService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
@RestController
@RequestMapping(value = "/min_program/cms")
public class MinProgramCmsController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(MinProgramController.class);
	@Autowired
	private ArticleService articleService;
	@Autowired
	private RsaUtil rsaUtil;
	/**
	 * 综合查询资讯信息
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String list(@Validated ActicleListSearchForm acticleListSearchForm, BindingResult bind) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			return buildSuccessMView(articleService.list(acticleListSearchForm));
		} catch (Exception e) {
			L.info("综合查询资讯信息异常{}", JSON.toJSONString(acticleListSearchForm), e);
			return buildExceptionMView();
		}
	}
	
	/**
	 * 查询资讯分类信息
	 */
	@ResponseBody
	@RequestMapping(value = "catalog", method = RequestMethod.POST)
	public String catalog(
			@RequestParam(value = "positionType",required = false, defaultValue="3") String positionType) {
		try {
		
			return buildSuccessMView(articleService.listCatalog(Constants.CHANNEL,positionType));
		} catch (Exception e) {
			L.info(" 查询资讯分类异常{}",  e);
			return buildExceptionMView();
		}
	}

	/**
	 * 综合查询资讯信息 
	 */
	@ResponseBody
	@RequestMapping(value = "detailArticle" , method = RequestMethod.POST)
	@CrossOrigin
	public DchealthCloudResponseEntity<String> detailArticle(@RequestParam(value = "articleId", required = true)  Long articleId) {
		try {
			if (articleId < 1) {
				DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
				result.setMsg(CroApiCodeConstant.ERROR_PARAM.getMsg());	
				result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());	
				return result;
			}
			return articleService.detailArticle(articleId);
		} catch (Exception e) {
			L.info("查询资讯详情异常{}", articleId, e);
			DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
			result.setMsg(CroApiCodeConstant.SYSTEM_ERROR.getMsg());	
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());	
			return result;
		}
	}
	/**
	 * 综合查询资讯信息
	 */
	@ResponseBody
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public String detail(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) Long accountId) {
		try {
			if (articleId < 1) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(articleService.detail(articleId,accountId,"3"));
		} catch (Exception e) {
			L.info("查询资讯详情异常{}", articleId, e);
			return buildExceptionMView();
		}
	}
	
	/**
	 * 查询资讯信息评论
	 */
	@ResponseBody
	@RequestMapping(value = "detailArticleComment", method = RequestMethod.POST)
	public String detailComment(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) Long accountId,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
		try {
			if (articleId < 1) {
				return buildErrorParamMView();
			}
			if(accountId !=null){
				accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
			}
			return buildSuccessMView(articleService.detailComment(articleId,accountId, pageNo, pageSize));
		} catch (Exception e) {
			L.info("查询资讯详情异常{}", articleId, e);
			return buildExceptionMView();
		}
	}
	
}
