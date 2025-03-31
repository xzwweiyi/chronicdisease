package com.dchealth.cro.cms.utils;

import com.alibaba.fastjson.JSON;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.crypto.Sm2Utils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

public class TdApiHttpClientUtil {

	private static final Logger L = LoggerFactory.getLogger(TdApiHttpClientUtil.class);
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
			.setConnectionRequestTimeout(150000).build();
//		private String BASE_URL="http://localhost:7918/tdapi/";
//	private String BASE_URL="http://test.giantcro.com/tdapi/";
	private static TdApiHttpClientUtil instance = null;

	private TdApiHttpClientUtil() {
	}

	public static TdApiHttpClientUtil getInstance() {
		if (instance == null) {
			instance = new TdApiHttpClientUtil();
		}
		return instance;
	}


	public String sendSzDefaultHttpPost(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
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
				sign = Sm2Utils.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
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
				sign = Sm2Utils.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
			} catch (CryptoException e) {
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

	public String sendFileDefaultHttpPost(String httpUrl, Map<String, String> maps,String fileName ,File file ) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		ContentType strContent=ContentType.create("text/plain", Charset.forName("UTF-8"));
		for (String key : maps.keySet()) {
			meBuilder.addTextBody(key,maps.get(key),strContent);
		}
		FileBody fileBody = new FileBody(file);
		meBuilder.addPart(fileName, fileBody);

		HttpEntity reqEntity = meBuilder.build();
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "1.0.0";
		headers.put("version", version);
		headers.put("time", time);
		StringBuffer sb = new StringBuffer();
		sb.append("time=").append(time).append("&version=").append(version);
		String sign = "";
		try {
			sign = Sm2Utils.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
		} catch (CryptoException e) {
			e.printStackTrace();
		}
		headers.put("sign",sign);
		for (String key : headers.keySet()) {
			httpPost.setHeader(key, headers.get(key));
		}
		httpPost.setEntity(reqEntity);
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


}
