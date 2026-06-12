package com.dchealth.cro.cms.controller;

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

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.form.CmsCommentBlankForm;
import com.dchealth.cro.cms.form.CmsCommentTipForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.service.CommentService;
import com.dchealth.cro.common.base.ApiBaseController;

/**
 * 评论管理
 */
@Controller
@RequestMapping(value = "/comment")
public class CommentController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(CommentController.class);
	@Autowired
	private CommentService commentService;
	@Autowired
	private RsaUtil rsaUtil;

	/**
	 * 保存投诉意见
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Validated CommentSaveForm commentSaveForm, BindingResult bind,
			@RequestAttribute("channel") String channel,@RequestAttribute("appType") String appType) {
		try {
			if(StringUtils.isNotEmpty(commentSaveForm.getAccountId())){
				commentSaveForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(commentSaveForm.getAccountId())));
			}
			commentSaveForm.setTenantId(channel);
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			
			return buildSuccessMView(commentService.save(commentSaveForm,appType));
		} catch (Exception e) {
			L.info("文章评论意见异常{}", JSON.toJSONString(commentSaveForm), e);
			return buildExceptionMView();
		}
	}
	/**
	 * 举报
	 */
	@ResponseBody
	@RequestMapping(value = "saveTip", method = RequestMethod.POST)
	public String saveTip(@Validated CmsCommentTipForm cmsCommentTipForm, BindingResult bind,
			@RequestAttribute("channel") String channel) {
		try {
			if(StringUtils.isNotEmpty(cmsCommentTipForm.getAccountId())){
				cmsCommentTipForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(cmsCommentTipForm.getAccountId())));
			}
			cmsCommentTipForm.setTenantId(channel);
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			
			return buildSuccessMView(commentService.saveTip(cmsCommentTipForm));
		} catch (Exception e) {
			L.info("举报文章评论意见异常{}", JSON.toJSONString(cmsCommentTipForm), e);
			return buildExceptionMView();
		}
	}
	/**
	 * 屏蔽
	 */
	@ResponseBody
	@RequestMapping(value = "saveBlank", method = RequestMethod.POST)
	public String saveBlank(@Validated CmsCommentBlankForm cmsCommentBlankForm, BindingResult bind,
			@RequestAttribute("channel") String channel) {
		try {
			if(StringUtils.isNotEmpty(cmsCommentBlankForm.getAccountId())){
				cmsCommentBlankForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(cmsCommentBlankForm.getAccountId())));
			}
			cmsCommentBlankForm.setTenantId(channel);
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			
			return buildSuccessMView(commentService.saveBlank(cmsCommentBlankForm));
		} catch (Exception e) {
			L.info("屏蔽文章评论意见异常{}", JSON.toJSONString(cmsCommentBlankForm), e);
			return buildExceptionMView();
		}
	}
	
	

	
}
