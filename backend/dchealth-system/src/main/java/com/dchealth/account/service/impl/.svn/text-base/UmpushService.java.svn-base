package com.dchealth.account.service.impl;

import com.alibaba.fastjson.JSON;
import com.dchealth.base.domain.AppSet;
import com.dchealth.base.mapper.AppSetMapper;
import com.dchealth.common.constant.RedisConstants;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.umpush.AndroidNotification;
import com.dchealth.common.umpush.PushClient;
import com.dchealth.common.umpush.UmConfig;
import com.dchealth.common.umpush.UmengNotification;
import com.dchealth.common.umpush.android.AndroidCustomizedcast;
import com.dchealth.common.umpush.ios.IOSCustomizedcast;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.core.contants.CroCoreConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UmpushService {

	@Autowired
	private PushClient pushClient;
	@Autowired
	private AppSetMapper appSetMapper;

	@Autowired
	private RedisCache redisCache;

	public boolean sendCustomizedcastMessage(Integer appType, String accountId,  String content,String extraField)  {
		String title ="系统消息";
		String hint="您有一条新的消息";
		try {
			return sendCustomizedcastMessage(appType, accountId, title, content, hint,extraField);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return false;
	}

	public boolean sendCustomizedcastMessage(Integer appType, String accountId, String title, String content,
											 String hint,String extraField) throws Exception {
		UmengNotification androidCustomizedcast = getAndroidCustomizedcast(appType, accountId, title, content, hint,extraField);
		UmengNotification iosCustomizedcast = getIOSCustomizedcast(appType, accountId, title, content,extraField);
		if (androidCustomizedcast == null || iosCustomizedcast == null) {
			return false;
		}
		boolean androidB = pushClient.send(androidCustomizedcast);
		boolean iosB = pushClient.send(iosCustomizedcast);
		if (androidB || iosB) {
			return true;
		}
		return false;
	}

	public AndroidCustomizedcast getAndroidCustomizedcast(Integer appType, String accountId, String title,
														  String content, String hint,String extraField) throws Exception {
		UmConfig liverGallUmConfig = getUmConfigInstance(appType);
		if (liverGallUmConfig == null) {
			return null;
		}
		// 通过Ailias 进行推送
		AndroidCustomizedcast androidCustomizedcast = new AndroidCustomizedcast(liverGallUmConfig.getUmAndroidAppkey(),
				liverGallUmConfig.getUmAndroidAppMasterSecret(),true,"com.dhctech.cro.push.PushActivity");
		// 设置别名以及类型
		androidCustomizedcast.setAlias(accountId, "ACCOUNT_ID");
		// 设置通知
		androidCustomizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
		androidCustomizedcast.setTicker(hint);
		// 通知内容
		androidCustomizedcast.setText(content);
		androidCustomizedcast.setTitle(title);
		androidCustomizedcast.setExtraField("json", extraField);
		// 点击通知后续行为，默认为打开APP
		androidCustomizedcast.setAfterOpenAction(AndroidNotification.AfterOpenAction.go_app);
		androidCustomizedcast.goActivityAfterOpen("com.dhctech.cro.push.PushActivity");
		androidCustomizedcast.setCustomField("");
		return androidCustomizedcast;
	}

	public IOSCustomizedcast getIOSCustomizedcast(Integer appType, String accountId, String title, String content,String extraField)
			throws Exception {
		UmConfig liverGallUmConfig = getUmConfigInstance(appType);
		if (liverGallUmConfig == null) {
			return null;
		}
		// 通过Ailias 进行推送
		IOSCustomizedcast iosCustomizedcast = new IOSCustomizedcast(liverGallUmConfig.getUmIosAppkey(),
				liverGallUmConfig.getUmIosAppMasterSecret());
		// 设置别名以及类型
		iosCustomizedcast.setAlias(accountId, "ACCOUNT_ID");
		iosCustomizedcast.setCustomizedField("json",extraField );
		// 设置通知
//		androidCustomizedcast.setTicker(hint);
		// 通知内容
//		iosCustomizedcast.setProductionMode();
//		iosCustomizedcast.setTestMode();
		iosCustomizedcast.setProductionMode();
		iosCustomizedcast.setAlert(content);
		iosCustomizedcast.setContentAvailable(1);
		return iosCustomizedcast;
	}

	private UmConfig getUmConfigInstance(Integer appType) {
		UmConfig liverGallUmConfig = new UmConfig();
		String cacheValue = redisCache.getCacheObject(RedisConstants.UMENG_PREFIX + appType);
//		log.info("--------------------------------推送获取缓存cacheValue参数:--------------------------------- {}", cacheValue);
		if (StringUtils.isNotBlankAndNotEmpty(cacheValue)) {
			liverGallUmConfig = JSON.parseObject(cacheValue, UmConfig.class);
		} else {
			AppSet appSetSearch = new AppSet();
			appSetSearch.setConfigKey(CroCoreConstant.UMENG+"_"+appType);
			appSetSearch.setAppId(appType);
			List<AppSet> list = appSetMapper.selectAppSetList(appSetSearch);
			if (list != null && list.size() > 0) {
				AppSet appSet = list.get(0);
				if (appSet != null && StringUtils.isNotBlankAndNotEmpty(appSet.getConfigValue())) {
					redisCache.setCacheObject(RedisConstants.UMENG_PREFIX + appType, appSet.getConfigValue());
					liverGallUmConfig = JSON.parseObject(appSet.getConfigValue(), UmConfig.class);
				}
			}
		}
//		log.info("--------------------------------推送获取缓存liverGallUmConfig:--------------------------------- {}", liverGallUmConfig.toString());
		return liverGallUmConfig;
	}




}
