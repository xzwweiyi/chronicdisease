package com.dchealth.cro.core.umpush;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.config.UmConfig;
import com.dchealth.cro.core.contants.CroCoreConstant;
import com.dchealth.cro.core.domain.AppSet;
import com.dchealth.cro.core.mapper.AppSetMapper;
import com.dchealth.cro.core.umpush.android.AndroidBroadcast;
import com.dchealth.cro.core.umpush.android.AndroidCustomizedcast;
import com.dchealth.cro.core.umpush.android.AndroidUnicast;
import com.dchealth.cro.core.umpush.ios.IOSCustomizedcast;
import com.dchealth.cro.core.umpush.ios.IOSUnicast;


@Component
public class UmpushService {
	private static final Logger L = LoggerFactory.getLogger(UmpushService.class);
	@Autowired
	private PushClient pushClient;
	@Autowired
	private AppSetMapper appSetMapper;

	@Autowired
	private RedisCache redisCache;

	public boolean sendCustomizedcastMessage(Integer appTpye, String accountId,  String content,String extraField)  {
		String title ="系统消息";
		String hint="您有一条新的消息";
		try {
			return sendCustomizedcastMessage(appTpye, accountId, title, content, hint,extraField);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendCustomizedcastMessage(Integer appTpye, String accountId, String title, String content,
											 String hint,String extraField) throws Exception {
		UmengNotification androidCustomizedcast = getAndroidCustomizedcast(appTpye,accountId, title, content, hint,extraField);
		UmengNotification iosCustomizedcast = getIOSCustomizedcast(appTpye, accountId, title, content,extraField);
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

	public IOSCustomizedcast getIOSCustomizedcast(Integer appType, String accountId, String title, String content,
												  String extraField)
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
		iosCustomizedcast.setProductionMode();
		iosCustomizedcast.setAlert(content);
		return iosCustomizedcast;
	}

	public UmConfig getUmConfigInstance(Integer appType) {
		UmConfig liverGallUmConfig = new UmConfig();
		String cacheValue = redisCache.getCacheString(RedisConstants.UMENG_PREFIX + appType);
		if (StringUtils.isNotNullOrEmpty(cacheValue)) {
			liverGallUmConfig = JSON.parseObject(cacheValue, UmConfig.class);
		} else {
			AppSet appSetSearch = new AppSet();
			appSetSearch.setConfigKey(CroCoreConstant.UMENG+"_"+appType);
			appSetSearch.setAppType(appType);
			List<AppSet> list = appSetMapper.selectAppSetList(appSetSearch);
			if (list != null && list.size() > 0) {
				AppSet appSet = list.get(0);
				if (appSet != null && StringUtils.isNotNullOrEmpty(appSet.getConfigValue())) {
					redisCache.cacheString(RedisConstants.UMENG_PREFIX + appType, appSet.getConfigValue());
					liverGallUmConfig = JSON.parseObject(appSet.getConfigValue(), UmConfig.class);
				}
			}
		}
		return liverGallUmConfig;
	}


	//---------------------------------------------------------------------------------

//	public boolean sendCustomizedcastMessage(String tenantId, String accountId, String title, String content,
//			String hint,Integer appType) throws Exception {
//		UmengNotification androidCustomizedcast = getAndroidCustomizedcast(tenantId, accountId, title, content, hint,
//				appType);
//		UmengNotification iosCustomizedcast = getIOSCustomizedcast(tenantId, accountId, title, content,appType);
//		if (androidCustomizedcast == null || iosCustomizedcast == null) {
//			return false;
//		}
//		L.info("appType:{}",appType);
//		boolean androidB = pushClient.send(androidCustomizedcast);
//		boolean iosB = pushClient.send(iosCustomizedcast);
//		if (androidB || iosB) {
//			return true;
//		}
//		return false;
//	}
//
//
//	/**
//	 *
//	 * @param tenantId
//	 * @param accountId
//	 * @param content
//	 * @return
//	 */
//	public boolean sendCustomizedcastMessage(String appId, String accountId,  String content,Integer appType)  {
//		String title = "系统消息";
//		String hint = "您有一条新的消息";
//		try {
//			L.info("11111111111111111111111111111:{}","11111111111111111111111111111");
//			sendCustomizedcastMessage(appId, accountId, title, content, hint, appType);
//			L.info("2222222222222222222222222222222222：{}","2222222222222222222222222222222222");
//			sendCustomizedcastMessage(appId, accountId, title, content, hint, "extraField", appType);
//			L.info("33333333333333333333333333333333333：{}","33333333333333333333333333333333333");
//			return sendCustomizedcastMessage(appId, accountId, content, "extraField", appType);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//
//	/**
//	 * 安卓推送
//	 *
//	 * @param accountId
//	 * @param title
//	 * @param content
//	 * @param hint       您有一条新的消息
//	 * @return
//	 * @throws Exception
//	 */
//	public AndroidCustomizedcast getAndroidCustomizedcast(String tenantId, String accountId, String title,
//			String content, String hint,Integer appType) throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(tenantId,appType);
//		if (liverGallUmConfig == null) {
//			return null;
//		}
//		// 通过Ailias 进行推送
//		AndroidCustomizedcast androidCustomizedcast = new AndroidCustomizedcast(liverGallUmConfig.getUmAndroidAppkey(),
//				liverGallUmConfig.getUmAndroidAppMasterSecret());
//		// 设置别名以及类型
//		androidCustomizedcast.setAlias(accountId, "ACCOUNT_ID");
//		// 设置通知
//		androidCustomizedcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		androidCustomizedcast.setTicker(hint);
//		// 通知内容
//		androidCustomizedcast.setText(content);
//		androidCustomizedcast.setTitle(title);
//		// 点击通知后续行为，默认为打开APP
//		androidCustomizedcast.setAfterOpenAction(AndroidNotification.AfterOpenAction.go_app);
//		androidCustomizedcast.setCustomField("");
//		return androidCustomizedcast;
//	}
//
//
//	public AndroidBroadcast sendAndroidBroadcast(String tenantId,  String title, String content,String hint,
//												 Integer appType) throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(tenantId,appType);
//		if (liverGallUmConfig == null) {
//			return null;
//		}
//		AndroidBroadcast broadcast = new AndroidBroadcast(liverGallUmConfig.getUmIosAppkey(),
//				liverGallUmConfig.getUmIosAppMasterSecret());
//		broadcast.setTicker( hint);
//		broadcast.setTitle(title);
//		broadcast.setText( content);
//		broadcast.goAppAfterOpen();
//		broadcast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		broadcast.setProductionMode();
//		return broadcast;
//	}
//
//	/**
//	 * 苹果推送
//	 *
//	 * @param sysGroupId
//	 * @param title
//	 * @param content
//	 * @return
//	 * @throws Exception
//	 */
//
//	public IOSCustomizedcast getIOSCustomizedcast(String tenantId, String accountId, String title, String content,
//												  Integer appType)
//			throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(tenantId,appType);
//		if (liverGallUmConfig == null) {
//			return null;
//		}
//		// 通过Ailias 进行推送
//		IOSCustomizedcast iosCustomizedcast = new IOSCustomizedcast(liverGallUmConfig.getUmIosAppkey(),
//				liverGallUmConfig.getUmIosAppMasterSecret());
//		// 设置别名以及类型
//		iosCustomizedcast.setAlias(accountId, "ACCOUNT_ID");
//		// 设置通知
////		androidCustomizedcast.setTicker(hint);
//		// 通知内容
//		iosCustomizedcast.setProductionMode();
//		iosCustomizedcast.setAlert(content);
//		return iosCustomizedcast;
//	}
//
//
//	/**
//	 * 发送Android
//	 *
//	 * @param devToken
//	 * @param title
//	 * @param content
//	 * @param hint
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean sendAndroidUnicastMessage(String tenantId, String devToken, String title, String content,
//			String hint,Integer appType) throws Exception {
//		UmengNotification androidUnicast = getAndroidUnicast(tenantId, title, content, hint, devToken,appType);
//		if (androidUnicast == null ) {
//			return false;
//		}
//		return pushClient.send(androidUnicast);
//	}
//
//	/**
//	 * 发送Android
//	 *
//	 * @param devToken
//	 * @param title
//	 * @param content
//	 * @param hint
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean sendIosUnicastMessage(String tenantId, String devToken, String title, String content,Integer appType)
//			throws Exception {
//		UmengNotification iosUnicast = getIOSUnicast(tenantId, devToken, title, content,appType);
//		if (iosUnicast == null ) {
//			return false;
//		}
//		return pushClient.send(iosUnicast);
//	}
//
//	/**
//	 * 安卓
//	 *
//	 * @param title
//	 * @param content
//	 * @param hint
//	 * @param devToken
//	 * @return
//	 * @throws Exception
//	 */
//
//	public AndroidUnicast getAndroidUnicast(String tenantId, String title, String content, String hint,
//											String devToken,Integer appType)
//			throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(tenantId,appType);
//		if (liverGallUmConfig == null) {
//			return null;
//		}
//
//		// 通过Ailias 进行推送
//		AndroidUnicast androidUnicast = new AndroidUnicast(liverGallUmConfig.getUmIosAppkey(),
//				liverGallUmConfig.getUmIosAppMasterSecret());
//		// 设置要推送的终端device_token
//		androidUnicast.setDeviceToken(devToken);
//		// 设置通知
//		androidUnicast.setDisplayType(AndroidNotification.DisplayType.NOTIFICATION);
//		androidUnicast.setTicker(hint);
//		// 通知内容
//		androidUnicast.setText(content);
//		androidUnicast.setTitle(title);
//		// 点击通知后续行为，默认为打开APP
//		androidUnicast.setAfterOpenAction(AndroidNotification.AfterOpenAction.go_app);
//		androidUnicast.setProductionMode();
//		return androidUnicast;
//	}
//
//	/**
//	 * 苹果推送
//	 *
//	 * @param sysGroupId
//	 * @param title
//	 * @param content
//	 * @return
//	 * @throws Exception
//	 */
//
//	public IOSUnicast getIOSUnicast(String tenantId, String devToken, String title, String content,Integer appType) throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(tenantId,appType);
//		if (liverGallUmConfig == null) {
//			return null;
//		}
//
//		// 通过Ailias 进行推送
//		IOSUnicast iosUnicast = new IOSUnicast(liverGallUmConfig.getUmIosAppkey(),
//				liverGallUmConfig.getUmIosAppMasterSecret());
//		iosUnicast.setDeviceToken(devToken);
//		// 设置通知
////		androidCustomizedcast.setTicker(hint);
//		// 通知内容
//		iosUnicast.setProductionMode();
//		iosUnicast.setAlert(content);
//		return iosUnicast;
//	}



}
