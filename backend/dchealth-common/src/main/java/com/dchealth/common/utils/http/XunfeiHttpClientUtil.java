package com.dchealth.common.utils.http;

import com.alibaba.fastjson2.JSON;
import com.dchealth.common.constant.Constants;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.*;

public class XunfeiHttpClientUtil {
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
			.setConnectionRequestTimeout(150000).build();
	private static XunfeiHttpClientUtil instance = null;

	private String BASE_URL = "https://www.xfzyzl.com";

	private XunfeiHttpClientUtil() {
	}

	public static XunfeiHttpClientUtil getInstance() {
		if (instance == null) {
			instance = new XunfeiHttpClientUtil();
		}
		return instance;
	}


	public String sendDefaultHttpPost(String httpUrl, Object object) {

		//当前时间戳
		Long timesTamp = System.currentTimeMillis();
		StringBuilder sb = new StringBuilder();
		//将appId+timesTamp数据拼接成字符串
		sb.append(Constants.XUNFEI_APP_ID).append(timesTamp);
		//加上秘钥secrekey
		sb.append(Constants.XUNFEI_SECRET_KEY);
		//sign计算方法：sign=MD5AsHex(str+secretKey)
		String sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
		String url = BASE_URL+httpUrl+"?appId="+Constants.XUNFEI_APP_ID+"&timestamp="+timesTamp+"&sign="+sign;

		HttpPost httpPost = new HttpPost(url);// 创建httpPost
//		System.out.println("url:"+url);
//		System.out.println("sign:"+sign);
//		System.out.println("参数:"+JSON.toJSONString(object));
		httpPost.setHeader("Content-Type","application/json; charset=utf-8");
		// 创建参数队列
		httpPost.setEntity(new StringEntity(JSON.toJSONString(object), "UTF-8"));
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
