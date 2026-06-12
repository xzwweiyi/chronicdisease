package com.dchealth.cro.cms.controller;
import java.util.ArrayList;
import java.util.List;

import com.dchealth.cro.common.utils.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.cms.form.GuestBookForm;
import com.dchealth.cro.cms.service.GuestBookService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;

/**
 * 资讯管理
 */
@Controller
@RequestMapping(value = "/guestbook")
public class GuestBookController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(GuestBookController.class);
	@Autowired
	private GuestBookService guestBookService;
	@Autowired
	private CroCoreConfig  croCoreConfig;
	@Value("${spring.profiles.active}")
	private String active;
	@Autowired
	private RsaUtil rsaUtil;

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
			if(StringUtils.isNotEmpty(guestBookForm.getAccountId())){
				guestBookForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(guestBookForm.getAccountId())));
			}
			List<MultipartFile> files = request.getFiles("files");
			if (files != null && files.size() > 0) {
				String fastPath = null;
				if("prod".equals(active)){
					fastPath = croCoreConfig.getProdFastPath();
				}else {
					fastPath = croCoreConfig.getTestFastPath();
				}
				String httpUrl = fastPath + "upload/singleFiles";
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
			return buildSuccessMView(guestBookService.save(guestBookForm));
		} catch (Exception e) {
			L.info("保存投诉意见异常{}", JSON.toJSONString(guestBookForm), e);
			return buildExceptionMView();
		}
	}
	
	
	/**
	 * 小程序保存投诉意见
	 */
	@ResponseBody
	@RequestMapping(value = "minSave", method = RequestMethod.POST)
	public String minSave(@Validated GuestBookForm guestBookForm, BindingResult bind,
			@RequestAttribute("channel") String channel) {
		try {
			guestBookForm.setTenantId(channel);
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			if(StringUtils.isNotEmpty(guestBookForm.getAccountId())){
				guestBookForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(guestBookForm.getAccountId())));
			}
			String imageList = guestBookForm.getImageListJson();
			if(StringUtils.isNotBlankAndNotEmpty(imageList)) {
				List<String>  imageArr = JSON.parseArray(imageList,String.class);
				guestBookForm.setList(imageArr);
			}
			return buildSuccessMView(guestBookService.save(guestBookForm));
		} catch (Exception e) {
			L.info("小程序保存投诉意见异常{}", JSON.toJSONString(guestBookForm), e);
			return buildExceptionMView();
		}
	}

	
}