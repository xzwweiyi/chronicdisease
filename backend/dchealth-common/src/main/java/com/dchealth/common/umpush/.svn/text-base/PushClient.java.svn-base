package com.dchealth.common.umpush;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
@Component
public class PushClient {
private static final Logger log = LoggerFactory.getLogger(PushClient.class);

// The user agent
private final String USER_AGENT = "Mozilla/5.0";

// This object is used for sending the post request to Umeng
private  CloseableHttpClient client = HttpClients.createDefault();

// The host
private static final String host = "http://msg.umeng.com";

// The upload path
private static final String uploadPath = "/upload";

// The post path
private static final String postPath = "/api/send";

public boolean send(UmengNotification msg) throws Exception {
	String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
	msg.setPredefinedKeyValue("timestamp", timestamp);
    String postBody = msg.getPostBody();
    String url = host + postPath;
   return  send(postBody, msg.getAppMasterSecret(),url);
   
 
}
public boolean send(String postBody,String appMasterSecret,String url) throws Exception {
	String sign = DigestUtils.md5Hex(("POST" + url + postBody + appMasterSecret).getBytes("utf8"));
	url = url + "?sign=" + sign;
	HttpPost post = new HttpPost(url);
	post.setHeader("User-Agent", USER_AGENT);
	StringEntity se = new StringEntity(postBody, "UTF-8");
	post.setEntity(se);
	HttpResponse response = client.execute(post);
	int status = response.getStatusLine().getStatusCode();
	log.error("Response Code : " + status);
	BufferedReader rd  = null;
	InputStreamReader   inputStream = null;
	try {
		inputStream = new InputStreamReader(response.getEntity().getContent());
		rd = new BufferedReader(inputStream);
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		log.info("--------------------------------友盟推送返回结果--------------------------- {}", result.toString());
		log.error(result.toString());
		if (status == 200) {
			log.error("Notification sent successfully.");
		} else {
			log.error("Failed to send the notification!");
		}
		return true;
	} catch (Exception e) {
	}finally {
		if(rd != null) {
			rd.close();
		}
		if(inputStream != null) {
			inputStream.close();
		}
	}
	
	return false;
	
}

public boolean uploadContents(String appkey,String appMasterSecret,String contents) throws Exception {
	
	JSONObject uploadJson = new JSONObject();
	uploadJson.put("appkey", appkey);
	String timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
	uploadJson.put("timestamp", timestamp);
	uploadJson.put("content", contents);
	String postBody = uploadJson.toString();
	String url = host + uploadPath;
	return send(postBody, appMasterSecret,url);
	
	
}

}