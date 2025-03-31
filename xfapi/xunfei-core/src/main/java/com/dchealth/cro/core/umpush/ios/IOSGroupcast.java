package com.dchealth.cro.core.umpush.ios;


import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.core.umpush.IOSNotification;

public class IOSGroupcast extends IOSNotification {
	public IOSGroupcast(String appkey,String appMasterSecret) throws Exception {
			setAppMasterSecret(appMasterSecret);
			setPredefinedKeyValue("appkey", appkey);
			this.setPredefinedKeyValue("type", "groupcast");	
	}
	
	public void setFilter(JSONObject filter) throws Exception {
    	setPredefinedKeyValue("filter", filter);
    }
}
