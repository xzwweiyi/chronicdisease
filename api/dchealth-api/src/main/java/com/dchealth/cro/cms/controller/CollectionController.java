package com.dchealth.cro.cms.controller;

import javax.servlet.http.HttpServletRequest;

import com.dchealth.cro.common.utils.RsaUtil;
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
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.cms.form.CollectionActicleSearchForm;
import com.dchealth.cro.cms.service.ArticleService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;

/**
 * 收藏接口
 */
@Controller
@RequestMapping(value = "/collection")
public class CollectionController extends ApiBaseController {
		private static final Logger L = LoggerFactory.getLogger(CollectionController.class);
		@Autowired
		private ArticleService articleService;
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
			if(accountId != null){
				accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
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
		 * 查询用户收藏信息
		 */
		@ResponseBody
		@RequestMapping(value = "list", method = RequestMethod.POST)
		public String list(@Validated CollectionActicleSearchForm collectionActicleSearchForm, BindingResult bind,
				@RequestAttribute("channel") String channel,
				HttpServletRequest request) {
			try {
				if (bind.hasErrors()) {
					return buildBindErrorParamMView(bind);
				}
				if(collectionActicleSearchForm.getAccountId() != null){
					collectionActicleSearchForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(collectionActicleSearchForm.getAccountId().toString())));
				}
				// 权限校验
				if (!validateAccountIdAccess(collectionActicleSearchForm.getAccountId(), request)) {
					return buildAccessDeniedResponse();
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
		public String list( @RequestParam(value = "accountId", required = true) Long accountId,
				@RequestParam(value = "articleId", required = true) Long articleId,
				@RequestAttribute("appType") String appType,
				@RequestAttribute("channel") String channel,
				HttpServletRequest request) {
			try {
				if(accountId != null){
					accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
				}
				// 权限校验
				if (!validateAccountIdAccess(accountId, request)) {
					return buildAccessDeniedResponse();
				}
				return buildSuccessMView(articleService.update(accountId,articleId,channel,appType));
			} catch (Exception e) {
				L.info("用户收藏信息异常用户ID:{},文章ID:{},{}",accountId,articleId, e);
				return buildExceptionMView();
			}
		}


	}