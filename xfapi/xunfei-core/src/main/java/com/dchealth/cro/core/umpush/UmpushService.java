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
import com.dchealth.cro.core.config.LiverGallUmConfig;
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

	public boolean sendCustomizedcastMessage(LiverGallUmConfig liverGallUmConfig, String accountId,  String content,String extraField)  {
		String title ="系统消息";
		String hint="您有一条新的消息";
		try {
			return sendCustomizedcastMessage(liverGallUmConfig, accountId, title, content, hint,extraField);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean sendCustomizedcastMessage(LiverGallUmConfig liverGallUmConfig, String accountId, String title, String content,
											 String hint,String extraField) throws Exception {
		UmengNotification androidCustomizedcast = getAndroidCustomizedcast(liverGallUmConfig,accountId, title, content, hint,extraField);
		UmengNotification iosCustomizedcast = getIOSCustomizedcast(liverGallUmConfig, accountId, title, content,extraField);
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

	public AndroidCustomizedcast getAndroidCustomizedcast(LiverGallUmConfig liverGallUmConfig, String accountId, String title,
														  String content, String hint,String extraField) throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(appType);
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

	public IOSCustomizedcast getIOSCustomizedcast(LiverGallUmConfig liverGallUmConfig, String accountId, String title, String content,
												  String extraField)
			throws Exception {
//		LiverGallUmConfig liverGallUmConfig = getLiverGallUmConfigInstance(appType);
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





}
