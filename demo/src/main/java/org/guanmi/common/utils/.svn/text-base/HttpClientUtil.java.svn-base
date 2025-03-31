package org.guanmi.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.util.encoders.Hex;
import org.guanmi.common.crypto.SM2Util;

import com.alibaba.fastjson.JSON;

public class HttpClientUtil {
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
			.setConnectionRequestTimeout(150000).build();
//	private String BASE_URL="http://localhost:7918/tdapi/";
	private String BASE_URL="http://test.giantcro.com/tdapi/";
	private static HttpClientUtil instance = null;
	private static final String channel ="1";
	private static final String deviceToken="test";

	private HttpClientUtil() {
	}

	public static HttpClientUtil getInstance() {
		if (instance == null) {
			instance = new HttpClientUtil();
		}
		return instance;
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl 地址
	 */
	public String sendHttpPost(String httpUrl) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		return sendHttpPost(httpPost);
	}
	
	

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl 地址
	 * @param params  参数(格式:key1=value1&key2=value2)
	 */
	public String sendHttpPost(String httpUrl, String params) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, "UTF-8");
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求（带文件）
	 * 
	 * @param httpUrl   地址
	 * @param maps      参数
	 * @param fileLists 附件
	 */
	public String sendHttpPost(String httpUrl, Map<String, Object> maps, File assistant_image_data, File image_data) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (String key : maps.keySet()) {
			meBuilder.addPart(key, new StringBody(String.valueOf(maps.get(key)), ContentType.TEXT_PLAIN));
		}
		if (assistant_image_data != null) {
			FileBody fileBody = new FileBody(assistant_image_data);
			meBuilder.addPart("assistant_image_data", fileBody);
		}
		if (image_data != null) {
			FileBody fileBody = new FileBody(image_data);
			meBuilder.addPart("image_data", fileBody);
		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl 地址
	 * @param maps    参数
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, String.valueOf(maps.get(key))));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl 地址
	 * @param maps    参数
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, String header) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		httpPost.setHeader("Authorization", "Basic c2t5d29ydGg6c2t5d29ydGg=");
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl 地址
	 * @param maps    参数
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, Map<String, String> header) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		for (String key : header.keySet()) {
			httpPost.setHeader(key, header.get(key));
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	
	public String sendSzTestHttpPost(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "v1.0.0";
		String osType="1";//1:Android 2 IOS
		headers.put("deviceToken", deviceToken);
		headers.put("version", version);
		headers.put("time", time);
		headers.put("channel", channel);
		headers.put("osType", osType);
		StringBuffer sb = new StringBuffer();
		sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
				.append(version).append("&channel=").append(channel).append("&osType=").append(osType);
		try {
			  String sign = "";
		         try {
		             sign = SM2Util.sign(Constants.RSA_TEST_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
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
	public String sendSzMinHttpPost(String httpUrl, Map<String, String> maps,String openId) {
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		Map<String, String> headers = new HashMap<String, String>();
		String sign = null;
		try {
			sign = HmacSHA256Util.hmacSHA256(Constants.SHA256_KEY, openId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		headers.put("sign", sign);
		headers.put("openId", openId);
		for (String key : headers.keySet()) {
			httpPost.setHeader(key, headers.get(key));
		}
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendHttpPost(httpPost);
	}
	
	
	public String sendSzDefaultHttpPost(String httpUrl, Map<String, String> maps) {
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "v1.0.0";
		String osType="1";//1:Android 2 IOS
		headers.put("deviceToken", deviceToken);
		headers.put("version", version);
		headers.put("time", time);
		headers.put("channel", channel);
		headers.put("osType", osType);
		headers.put("appType", "1");
		StringBuffer sb = new StringBuffer();
		sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
				.append(version).append("&channel=").append(channel).append("&osType=").append(osType);
		System.out.println("sb:"+sb.toString());

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
	
	public static void main(String[] args) throws Exception {
		//test2();
	}
	
	
	
	public String sendSzDefaultHttpPost(String httpUrl, Map<String, String> maps,List<File> fileLists) {
		
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		ContentType strContent=ContentType.create("text/plain",Charset.forName("UTF-8"));
		for (String key : maps.keySet()) {
			meBuilder.addTextBody(key,maps.get(key),strContent);
//			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		if(fileLists !=null && fileLists.size() >0) {
			for (File file : fileLists) {
				FileBody fileBody = new FileBody(file);
				meBuilder.addPart("files", fileBody);
			}
		}
		
		HttpEntity reqEntity = meBuilder.build();
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "v1.0.0";
		String osType="1";//1:Android 2 IOS
		headers.put("deviceToken", deviceToken);
		headers.put("version", version);
		headers.put("time", time);
		headers.put("channel", channel);
		headers.put("osType", osType);
		StringBuffer sb = new StringBuffer();
		sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
				.append(version).append("&channel=").append(channel).append("&osType=").append(osType);
		
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
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}
	public String sendSzTestHttpPost(String httpUrl, Map<String, String> maps,String fileName,List<File> fileLists) {
		
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		ContentType strContent=ContentType.create("text/plain",Charset.forName("UTF-8"));
		for (String key : maps.keySet()) {
			meBuilder.addTextBody(key,maps.get(key),strContent);
//			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		if(fileLists !=null && fileLists.size() >0) {
			for (File file : fileLists) {
				FileBody fileBody = new FileBody(file);
				meBuilder.addPart(fileName, fileBody);
			}
		}
		
		HttpEntity reqEntity = meBuilder.build();
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "v1.0.0";
		String osType="1";//1:Android 2 IOS
		headers.put("deviceToken", deviceToken);
		headers.put("version", version);
		headers.put("time", time);
		headers.put("channel", channel);
		headers.put("osType", osType);
		StringBuffer sb = new StringBuffer();
		sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
		.append(version).append("&channel=").append(channel).append("&osType=").append(osType);
		 String sign = "";
         try {
             sign = SM2Util.sign(Constants.RSA_TEST_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
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
	public String sendSzDefaultHttpPost(String httpUrl, Map<String, String> maps,String fileName ,File file ) {
		
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		ContentType strContent=ContentType.create("text/plain",Charset.forName("UTF-8"));
		for (String key : maps.keySet()) {
			meBuilder.addTextBody(key,maps.get(key),strContent);
//			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		FileBody fileBody = new FileBody(file);
		meBuilder.addPart(fileName, fileBody);
		
		HttpEntity reqEntity = meBuilder.build();
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "v1.0.0";
		String osType="1";//1:Android 2 IOS
		headers.put("deviceToken", deviceToken);
		headers.put("version", version);
		headers.put("time", time);
		headers.put("channel", channel);
		headers.put("osType", osType);
		StringBuffer sb = new StringBuffer();
		sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
		.append(version).append("&channel=").append(channel).append("&osType=").append(osType);
		
		 String sign = "";
         try {
             sign = SM2Util.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
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
	public String sendSzTestHttpPost(String httpUrl, Map<String, String> maps,String fileName ,File file ) {
		
		HttpPost httpPost = new HttpPost(BASE_URL+httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		ContentType strContent=ContentType.create("text/plain",Charset.forName("UTF-8"));
		for (String key : maps.keySet()) {
			meBuilder.addTextBody(key,maps.get(key),strContent);
//			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		FileBody fileBody = new FileBody(file);
		meBuilder.addPart(fileName, fileBody);
		
		HttpEntity reqEntity = meBuilder.build();
		Map<String, String> headers = new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time =sdf.format(new Date());
		String version = "v1.0.0";
		String osType="1";//1:Android 2 IOS
		headers.put("deviceToken", deviceToken);
		headers.put("version", version);
		headers.put("time", time);
		headers.put("channel", channel);
		headers.put("osType", osType);
		StringBuffer sb = new StringBuffer();
		sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
		.append(version).append("&channel=").append(channel).append("&osType=").append(osType);
		 String sign = "";
         try {
             sign = SM2Util.sign(Constants.RSA_TEST_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
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
	 * 发送 post请求
	 * 
	 * @param httpUrl 地址
	 * @param maps    参数
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, Map<String, String> header,
			List<File> fileLists, List<String> fileNames) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		ContentType strContent=ContentType.create("text/plain",Charset.forName("UTF-8"));
		for (String key : maps.keySet()) {
			meBuilder.addTextBody(key,maps.get(key),strContent);
		}
		for (String key : header.keySet()) {
			httpPost.setHeader(key, header.get(key));
		}
		if (fileLists != null && fileLists.size() > 0) {

			for (int i = 0; i < fileLists.size(); i++) {
				FileBody fileBody = new FileBody(fileLists.get(i));
				meBuilder.addPart(fileNames.get(i), fileBody);
			}

		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求（带文件）
	 * 
	 * @param httpUrl   地址
	 * @param maps      参数
	 * @param fileLists 附件
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (String key : maps.keySet()) {
			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		for (File file : fileLists) {
			FileBody fileBody = new FileBody(file);
			meBuilder.addPart("files", fileBody);
		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}

	/**
	 * 发送 post请求（带文件）
	 * 
	 * @param httpUrl   地址
	 * @param maps      参数
	 * @param fileLists 附件
	 */
	public String sendHttpPost(String httpUrl, Map<String, String> maps, File file) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (String key : maps.keySet()) {
			meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
		}
		if (file != null) {
			FileBody fileBody = new FileBody(file);
			meBuilder.addPart("file", fileBody);
		}
		HttpEntity reqEntity = meBuilder.build();
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

	/**
	 * 发送 get请求
	 * 
	 * @param httpUrl
	 */
	public String sendHttpGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpGet(httpGet);
	}

	/**
	 * 发送 get请求
	 * 
	 * @param httpUrl
	 */
	public String sendHttpGet(String httpUrl, Map<String, String> headers) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		for (String key : headers.keySet()) {
			// addFormDataPart用来传入字符串参数
			httpGet.addHeader(key, headers.get(key));
		}

		return sendHttpGet(httpGet);
	}

	/**
	 * 发送 get请求Https
	 * 
	 * @param httpUrl
	 */
	public String sendHttpsGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpsGet(httpGet);
	}

	/**
	 * 发送Get请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private String sendHttpGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
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
	public  String postBody(String url, String json) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {

			post.setEntity(new StringEntity(json, "UTF-8"));
			response = httpClient.execute(post); // 发送请求并接收返回数据
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity(); // 获取response的body部分
				result = EntityUtils.toString(entity); // 读取reponse的body部分并转化成字符串
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}
	

	/**
	 * 发送Get请求Https
	 * 
	 * @param httpPost
	 * @return
	 */
	private String sendHttpsGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader
					.load(new URL(httpGet.getURI().toString()));
			DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
			httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
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

	/**
	 * 请求Post Body
	 * 
	 * @param url
	 * @param headerMap
	 * @param contentMap
	 * @return
	 */
	public String postBody(String url, Map<String, String> headerMap, Map<String, String> contentMap) {
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		List<NameValuePair> content = new ArrayList<NameValuePair>();
		Iterator iterator = contentMap.entrySet().iterator(); // 将content生成entity
		while (iterator.hasNext()) {
			Entry<String, String> elem = (Entry<String, String>) iterator.next();
			content.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
		}
		CloseableHttpResponse response = null;
		try {
			Iterator headerIterator = headerMap.entrySet().iterator(); // 循环增加header
			while (headerIterator.hasNext()) {
				Entry<String, String> elem = (Entry<String, String>) headerIterator.next();
				post.addHeader(elem.getKey(), elem.getValue());
			}
			if (content.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content, "UTF-8");
				
				post.setEntity(new StringEntity(JSON.toJSONString(contentMap), "UTF-8"));
			}
			response = httpClient.execute(post); // 发送请求并接收返回数据
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity(); // 获取response的body部分
				result = EntityUtils.toString(entity); // 读取reponse的body部分并转化成字符串
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return null;
	}
	
	/**
	 * POST表单数据请求
	 *
	 * @param requestPath
	 *            请求相对路径
	 * @param jsondata
	 *            参数data值
	 * @return
	 */
	public static void postAliData(String requestPath, 	List<NameValuePair> formparams ) {
		// 创建默认的httpClient实例.
		HttpClient httpclient = new DefaultHttpClient();
		// 创建HTTPPOST
		HttpPost httppost = new HttpPost(requestPath);

		UrlEncodedFormEntity uefEntity;
		try {
			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
			httppost.setEntity(uefEntity);
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String results = EntityUtils.toString(entity, "UTF-8");
				System.out.println("encode-result:" + results);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpclient.getConnectionManager().shutdown();
		}
	}	
}
