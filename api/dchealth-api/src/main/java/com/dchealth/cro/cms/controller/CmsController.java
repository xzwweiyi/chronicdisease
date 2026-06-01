package com.dchealth.cro.cms.controller;

import cn.hutool.core.net.URLDecoder;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.GuestBookForm;
import com.dchealth.cro.cms.service.ArticleService;
import com.dchealth.cro.cms.service.GuestBookService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 资讯管理
 */
@Controller
@RequestMapping(value = "/article")
public class CmsController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(CmsController.class);
	@Autowired
	private ArticleService articleService;
	@Autowired
	private GuestBookService guestBookService;
	@Autowired
	private CroCoreConfig  croCoreConfig;
	@Value("${spring.profiles.active}")
	private String active;

	@Autowired
	private RsaUtil rsaUtil;

	/**
	 * 综合查询资讯信息
	 */
	@ResponseBody
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public String list(@Validated ActicleListSearchForm acticleListSearchForm, BindingResult bind,
			@RequestAttribute(value = "appType") String appType, @RequestAttribute("channel") String channel) {
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			acticleListSearchForm.setTenantId(channel);
			acticleListSearchForm.setPositionType(appType);
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
			@RequestAttribute(value = "appType") String appType,@RequestAttribute("channel") String channel) {
		try {
		
			return buildSuccessMView(articleService.listCatalog(channel,appType));
		} catch (Exception e) {
			L.info(" 查询资讯分类异常{}", channel, e);
			return buildExceptionMView();
		}
	}

	/**
	 * 综合查询资讯信息
	 */
	@ResponseBody
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public String detail(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) String accountId,@RequestAttribute("appType") String appType) {
		try {
			if (articleId < 1) {
				return buildErrorParamMView();
			}
			Long accountIds = null;
			if(StringUtils.isNotEmpty(accountId)){
				accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
			}
			return buildSuccessMView(articleService.detail(articleId,accountIds,appType));
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
	public String detailComment(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) String accountId,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
		try {
			if (articleId < 1) {
				return buildErrorParamMView();
			}
			Long accountIds = null;
			if(StringUtils.isNotEmpty(accountId)){
				accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
			}
			return buildSuccessMView(articleService.detailComment(articleId,accountIds, pageNo, pageSize));
		} catch (Exception e) {
			L.info("查询资讯详情异常{}", articleId, e);
			return buildExceptionMView();
		}
	}
	
	/**
	 * 保存投诉意见
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@Validated GuestBookForm guestBookForm, BindingResult bind,
			@RequestAttribute("channel") String channel, MultipartHttpServletRequest request) {
		try {
			guestBookForm.setTenantId(channel);
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			List<String> images = new ArrayList<String>();
			List<MultipartFile> files = request.getFiles("files");
			
			if (files != null && files.size() > 0) {
				String fastPath = null;
				if("prod".equals(active)){
					fastPath = croCoreConfig.getProdFastPath();
				}else {
					fastPath = croCoreConfig.getTestFastPath();
				}
				String httpUrl = fastPath + "ngupload/singleFiles";
				String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, files);
				if (StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)) {
					JSONObject jsonObject = JSON.parseObject(result);
					if (jsonObject != null) {
						int  code = jsonObject.getInteger("code");
						if(code ==1000) {
							JSONArray  jsonArray = jsonObject.getJSONArray("data");
							List<String>  urls = new ArrayList<String>();
							for (Object url : jsonArray) {
								urls.add(String.valueOf(url));
							}
							guestBookForm.setList(urls);
						}
					}
				}
			}
			if(StringUtils.isNotEmpty(guestBookForm.getAccountId())){
				guestBookForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(guestBookForm.getAccountId())));
			}
			guestBookForm.setList(images);
			return buildSuccessMView(guestBookService.save(guestBookForm));
		} catch (Exception e) {
			L.info("保存投诉意见异常{}", JSON.toJSONString(guestBookForm), e);
			return buildExceptionMView();
		}
	}
	
	

}
