package com.dchealth.common.utils.http;

import cn.hutool.crypto.CryptoException;
import com.alibaba.fastjson.JSON;
import com.dchealth.common.crypto.SM2Util;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Hex;
import com.dchealth.common.constant.Constants;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class XfServiceHttpClientUtil {
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
			.setConnectionRequestTimeout(150000).build();

	private static XfServiceHttpClientUtil instance = null;

	private XfServiceHttpClientUtil() {
	}

	public static XfServiceHttpClientUtil getInstance() {
		if (instance == null) {
			instance = new XfServiceHttpClientUtil();
		}
		return instance;
	}


	public String sendDefaultHttpPost(String httpUrl, Object object) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = getNameValuePairList(object);
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "1.0.0";
		headers.put("version", version);
		headers.put("time", time);
		StringBuffer sb = new StringBuffer();
		sb.append("time=").append(time).append("&version=").append(version);
		try {
			String sign = "";
			try {
				sign = SM2Util.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
			} catch (CryptoException e) {
				e.printStackTrace();
			}
			headers.put("sign", sign);
			for (String key : headers.keySet()) {
				httpPost.setHeader(key, headers.get(key));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	public List<NameValuePair> getNameValuePairList(Object object){
		List<NameValuePair> params = new ArrayList<>();
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field:fields){
			field.setAccessible(true);
			String name = field.getName();
			try {
				Object value = field.get(object);
				if(value!=null){
					params.add(new BasicNameValuePair(name,value.toString()));
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return params;
	}


	public String sendSzDefaultHttpPostJson(String httpUrl, Object object) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "1.0.0";
		headers.put("version", version);
		headers.put("time", time);
		StringBuffer sb = new StringBuffer();
		sb.append("time=").append(time).append("&version=").append(version);
		try {
			String sign = "";
			try {
				sign = SM2Util.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
			} catch (org.bouncycastle.crypto.CryptoException e) {
				e.printStackTrace();
			}
			headers.put("sign", sign);
			for (String key : headers.keySet()) {
				httpPost.setHeader(key, headers.get(key));
			}
			httpPost.setEntity(new StringEntity(JSON.toJSONString(object), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送Post请求
	 *
	 * @param httpPost
	 * @return
	 */
	public String sendHttpPost(HttpPost httpPost) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}

	public static void main(String[] args) throws Exception {
		//test2();
	}




}
