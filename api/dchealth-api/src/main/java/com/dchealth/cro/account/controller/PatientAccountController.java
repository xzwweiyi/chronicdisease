package com.dchealth.cro.account.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import cn.hutool.core.net.URLDecoder;
import com.dchealth.cro.common.utils.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.account.service.DoctorAccountService;
import com.dchealth.cro.account.service.PatientAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.common.crypto.Sm4Util;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;

/**
 * 患者账号管理
 */
@RestController
@RequestMapping(value = "/patientAccount")
public class PatientAccountController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(PatientAccountController.class);
    @Autowired
    private PatientAccountService patientAccountService;
    @Autowired
    private CroCoreConfig croCoreConfig;
    
    @Autowired
    private DoctorAccountService doctorAccountService;
	@Value("${spring.profiles.active}")
	private String active;
	@Autowired
	private RsaUtil rsaUtil;

    /**
     * 验证码登陆
     * @param mobile
     * @param verifyCode
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loginByMobileSms", method = RequestMethod.POST)
    public String loginByMobileSms(String mobile,String verifyCode, @RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel,
                                   @RequestAttribute("appType") String appType) {
        try {
            boolean b = notEmpty(mobile,verifyCode);
            if (!b) {
                return buildErrorParamMView();
            }
            String openId = deviceToken;
            return buildSuccessMView(patientAccountService.loginByMobileSms(mobile,verifyCode, deviceToken, channel,openId));
        } catch (Exception e) {
            L.info("患者用户登录异常手机号:{}验证码:{}", mobile,verifyCode, e);
            return buildExceptionMView();
        }

    }

    /**
     * 静默登录
     * @param mobile
     * @param deviceToken
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "loginByMobileSilent", method = RequestMethod.POST)
    public String loginByMobileSilent(String mobile, @RequestAttribute("deviceToken") String deviceToken, @RequestAttribute("channel") String channel) {
        L.info("deviceToken:{} channel:{} mobile:{}",deviceToken,channel,mobile);
        try {
            boolean b = notEmpty(mobile);
            if (!b) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(patientAccountService.loginByMobileSilent(mobile, deviceToken, channel));
        } catch (Exception e) {
            L.info("患者用户静默登录异常手机号:{}", mobile, e);
            return buildExceptionMView();
        }

    }

    /**
     * App登录发送验证码
     * @param mobile 手机号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "sendAccountVerifyCode", method = RequestMethod.POST)
    public String sendMobileSmsVerifyCode(String mobile, @RequestAttribute("channel") String channel) {
        try {
            boolean b = notEmpty(mobile);
            if (!b) {
                return buildErrorParamMView();
            }
            	String phone = "";
        		try {
        			phone = Sm4Util.decryptEcb(Constants.SM4_KEY, mobile);
        		} catch (Exception e) {
        			L.error("手机号有误:{},渠道:{}", phone, channel);
        			return buildErrorParamMView();
        		}
            if (phone.matches(PatternConstant.PHONE) ) {
            	SendVerfiyCodeForm  sendVerfiyCodeForm  = new SendVerfiyCodeForm();
            	sendVerfiyCodeForm.setAppId(channel);
            	sendVerfiyCodeForm.setMobile(phone);
                return buildSuccessMView(doctorAccountService.sendMobileSmsVerifyCode(sendVerfiyCodeForm));
            }
            return buildErrorParamMView();
        } catch (Exception e) {
            L.info("发送验证码异常:{}", mobile, e);
            return buildExceptionMView();
        }
    }
    
    /**
	 *  根据OpenId 获取患者信息
	 * 
	 * @param accountVO
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getPatientInfoByOpenId", method = RequestMethod.POST)
	public String  getUserByOpenId(String  openId) {
		try {
			boolean b = notEmpty(openId);
			if (!b) {
				return buildErrorParamMView();
			}
			return buildSuccessMView(patientAccountService.loginByOpenId(openId));
			
		} catch (Exception e) {
			L.error("根据OpenId获取患者信息异常{}",openId, e);
			return buildExceptionMView();
		}
	}
	
	

	/**
	 * 上传文件
	 */
	@ResponseBody
	@RequestMapping(value = "uploadFile", method = RequestMethod.POST)
	public String updateDoctorInfo(MultipartHttpServletRequest request) {
		DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
		try {
			//头像
			MultipartFile headImageFile = request.getFile("file");
			if (headImageFile != null) {
				String fastPath = null;
				if("prod".equals(active)){
					fastPath = croCoreConfig.getProdFastPath();
				}else {
					fastPath = croCoreConfig.getTestFastPath();
				}
				String httpUrl = fastPath + "ngupload/single";
				String filesResult = HttpClientUtil.getInstance().sendHttpPost(httpUrl, headImageFile);
				if (StringUtils.isNotNullOrEmpty(filesResult)) {
					JSONObject jsonObject = JSON.parseObject(filesResult);
					if (jsonObject != null) {
						int code = jsonObject.getInteger("code");
						if (code == 1000) {
							result.setData(jsonObject.getString("data"));
							return buildSuccessMView(result);
						}
					}
				}
			}
			result.setCode(CroApiCodeConstant.FAIL.getCode());
			return buildSuccessMView(result);
		} catch (IOException e) {
			L.info("小程序上传文件异常{}", e);
			return buildExceptionMView();
		}
	}

	 /**
     * 小程序退出
     * @param mobile
     * @param verifyCode
     * @param channel
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "minLoginOut", method = RequestMethod.POST)
    public String minLoginOut(String accountId,String openId) {
    	try {
			Long accountIds = null;
			if(StringUtils.isNotEmpty(accountId)){
				accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
			}
    		boolean b = notEmpty(accountIds,openId);
    		if (!b) {
    			return buildErrorParamMView();
    		}
    		return buildSuccessMView(patientAccountService.loginOut(accountIds,openId));
    	} catch (Exception e) {
    		L.info("患者用户退出异常账号ID:{}OpenId:{}", accountId,openId, e);
    		return buildExceptionMView();
    	}
    	
    }

}
