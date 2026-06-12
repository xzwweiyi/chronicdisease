package com.dchealth.cro.account.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.account.form.AccountForm;
import com.dchealth.cro.account.form.UserUpdateOldPasswordForm;
import com.dchealth.cro.account.service.AccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.config.KeyConfig;
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
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

@Controller
@RequestMapping(value = "/doctor")
public class AccountController extends ApiBaseController {
	private static final Logger L = LoggerFactory.getLogger(AccountController.class);
	@Autowired
	private AccountService accountService;
	@Autowired
	private CroCoreConfig croCoreConfig;
	@Value("${spring.profiles.active}")
	private String active;
    @Autowired
	private RsaUtil rsaUtil;
	/**
	 * 更新账户信息
	 * @param appType
	 * @param channel
	 * @param accountForm
	 * @param bind
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateAccountInfo", method = RequestMethod.POST)
	public String updateAccountInfo(@RequestAttribute(value ="appType", required = true)String appType, @RequestAttribute("channel") String channel,
									@Validated AccountForm accountForm, BindingResult bind, MultipartHttpServletRequest request){
		try {
			if (bind.hasErrors()) {
				return buildBindErrorParamMView(bind);
			}
			if(StringUtils.isNotEmpty(accountForm.getAccountId())){
				accountForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(accountForm.getAccountId())));
			}
			accountForm.setTenantId(channel);
			accountForm.setPositionType(appType);
			accountForm = dealFile(accountForm,request);
//			return buildSuccessMView(accountService.updateAccountInfo(accountForm));
			return buildExceptionMView();
		} catch (IOException e) {
			L.info("修改账户信息异常{}", JSON.toJSONString(accountForm), e);
			return buildExceptionMView();
		}
	}

	private AccountForm dealFile(AccountForm accountForm,MultipartHttpServletRequest request) throws IOException {
		//头像
		MultipartFile headImageFile = request.getFile("headImageFile");
		if(headImageFile!=null) {
			String fastPath = null;
			if("prod".equals(active)){
				fastPath = croCoreConfig.getProdFastPath();
			}else {
				fastPath = croCoreConfig.getTestFastPath();
			}
			String httpUrl = fastPath + "ngupload/single";
			String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, headImageFile);
			if (StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)) {
				JSONObject jsonObject = JSON.parseObject(result);
				if (jsonObject != null) {
					int  code = jsonObject.getInteger("code");
					if(code ==1000) {
						accountForm.setHeadImage(jsonObject.getString("data"));
					}
				}
			}
		}
		return accountForm;
	}
	
	

	
	/**
	 * 个人中心
	 */
	@ResponseBody
	@RequestMapping(value = "index", method = RequestMethod.POST)
	public String index(String accountId) {
		Long accountIds = null;
		if(StringUtils.isNotEmpty(accountId)){
			accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
		}
		Boolean  b = notEmpty(accountId);
		if (!b) {
			return buildErrorParamMView();
		}
		return buildSuccessMView(accountService.index(accountIds));
	}
	/**
	 * 个人中心
	 */
	@ResponseBody
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public String detail(String accountId) {
		Long accountIds = null;
		if(StringUtils.isNotEmpty(accountId)){
			accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
		}
		Boolean  b = notEmpty(accountId);
		if (!b) {
			return buildErrorParamMView();
		}
		return buildSuccessMView(accountService.detail(accountIds));
	}
	
	/**
	 * 根据账号ID 旧密码修改用户登录密码
	 */
	@ResponseBody
	@RequestMapping(value = "updatePasswordByOldPassword", method = RequestMethod.POST)
	public String updatePasswordByAccountId(@Validated UserUpdateOldPasswordForm userUpdateOldPasswordForm,@RequestAttribute("channel") String channel, BindingResult bind) {
		if (bind.hasErrors()) {
			return buildBindErrorParamMView(bind);
		}
		if(userUpdateOldPasswordForm.getAccountId() !=null){
			userUpdateOldPasswordForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(userUpdateOldPasswordForm.getAccountId().toString())));
		}
		userUpdateOldPasswordForm.setTenantId(channel);
		return buildSuccessMView(accountService.updatePasswordByOldPassword(userUpdateOldPasswordForm));
	}


	

	

}
