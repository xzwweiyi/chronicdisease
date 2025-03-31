package com.dchealth.cro.core.tencent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.core.mapper.AppSetMapper;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;

@Service
public class TencentSmsSevice {
	private static final Logger L = LoggerFactory.getLogger(TencentSmsSevice.class);
	@Autowired
	private  CroCoreConfig croCoreConfig;
//	@Autowired
//	private RedisCache redisCache;
//	@Autowired
//	private  AppSetMapper  appSetMapper;
	
    /**
     * 
     * @param phones {"+8618515574691"} 
     * @param templateId "509184" 模版id,见《创建短信签名和模版》小节
     * @param templateParam 模版参数，从前往后对应的是模版的{1}、{2}等,见《创建短信签名和模版》小节
     * @return
     */
	public boolean sendTencentSMS(String appId ,String [] phones,String templateId, String [] templateParam,TencentSmsVO  tencentSmsVO) {
//		TencentSmsVO  tencentSmsVO = getTencentSmsVO(appId);
		if(tencentSmsVO.getAppId() == null ) {
			return false;
		}
		// appId：1 phones： +8618392642013  templateId：1172347 消息发送模板枚举编号  templateParam：["111512"]只是模板变参，可以是验证码
		// sign：神州医疗
	  return 	sendTencentSMS(tencentSmsVO.getAppId(), phones, templateId, templateParam, tencentSmsVO.getSign());

	}
	
	/**
	 *获取腾讯参数
	 * 
	 * @return
	 */
//	private TencentSmsVO getTencentSmsVO(String appId) {
//		TencentSmsVO tencentSmsVO = new TencentSmsVO();
//		String cacheValue = redisCache.getCacheString(RedisConstants.TENCENT_SMS_PREFIX + appId );
//		if (StringUtils.isNotNullOrEmpty(cacheValue)) {
//			tencentSmsVO = JSON.parseObject(cacheValue, TencentSmsVO.class);
//		} else {
//			AppSet  appSetSearch = new AppSet();
//			appSetSearch.setAppId(Long.valueOf(appId));
//			appSetSearch.setConfigKey(CroCoreConstant.TENCENT_SMS);
//			List<AppSet>  list = appSetMapper.selectAppSetList(appSetSearch);
//			if (list != null && list.size() > 0) {
//				AppSet  appSet  = list.get(0);
//				if(appSet != null ) {
//					redisCache.cacheString(RedisConstants.TENCENT_SMS_PREFIX + appId, appSet.getConfigValue());
//					tencentSmsVO = JSON.parseObject(appSet.getConfigValue(), TencentSmsVO.class);
//				}
//
//			}
//		}
//
//		return tencentSmsVO;
//	}
	
	

	
    /**
     * 
     * @param appId "1400300781" appId ,见《创建应用》小节
     * @param phones {"+8618515574691"} 
     * @param templateId "509184" 模版id,见《创建短信签名和模版》小节
     * @param templateParam 模版参数，从前往后对应的是模版的{1}、{2}等,见《创建短信签名和模版》小节
     * @param  sign 签名内容，不是填签名id,见《创建短信签名和模版》小节
     * @return
     */
	public boolean sendTencentSMS(String appId,String [] phones,String templateId, String [] templateParam,String sign ) {
		SmsClient smsClient = getSmsClientInstance();
	        SendSmsRequest sendSmsRequest = new SendSmsRequest();
	        sendSmsRequest.setSmsSdkAppid(appId);//appId ,见《创建应用》小节
	        sendSmsRequest.setPhoneNumberSet(phones);
	        sendSmsRequest.setTemplateID(templateId);  //模版id,见《创建短信签名和模版》小节
	        sendSmsRequest.setTemplateParamSet(templateParam);
	        sendSmsRequest.setSign(sign); //签名内容，不是填签名id,见《创建短信签名和模版》小节
	        try {
	        	  L.info("调用腾讯请求参数:{}",JSON.toJSONString(sendSmsRequest));
	            SendSmsResponse sendSmsResponse= smsClient.SendSms(sendSmsRequest); //发送短信
	            L.info("调用腾讯发送短信返回结果:{}",JSON.toJSONString(sendSmsResponse));
	            if(sendSmsResponse.getRequestId() !=null && sendSmsResponse.getSendStatusSet()!=null ) {
	            	SendStatus[]  sets = sendSmsResponse.getSendStatusSet();
	            	for (SendStatus sendStatus : sets) {
	            		if("Ok".equalsIgnoreCase(sendStatus.getCode())) {
	            		return true;
	            		}
					}
	            }
	        } catch (TencentCloudSDKException e) {
	            e.printStackTrace();
	            L.error("调用腾讯发送短信异常:{}",e);
	        }
	        return false;

	}
    /**
     * 获取发送短信实例
     * @return
     */
	private  SmsClient getSmsClientInstance() {
		Credential cred = new Credential(croCoreConfig.getTencentSecretId(), croCoreConfig.getTencentSecretKey());
		// 实例化要请求产品(以cvm为例)的client对象
		ClientProfile clientProfile = new ClientProfile();
		clientProfile.setSignMethod(ClientProfile.SIGN_TC3_256);
		return new SmsClient(cred, "ap-beijing");// 第二个ap-chongqing 填产品所在的区
	}
}
