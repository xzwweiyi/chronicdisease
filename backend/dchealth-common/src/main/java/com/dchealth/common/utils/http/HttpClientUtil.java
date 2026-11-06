package com.dchealth.common.utils.http;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
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
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.dchealth.common.utils.StringUtils;



public class HttpClientUtil implements Closeable {
	private static final Logger L = LoggerFactory.getLogger(HttpClientUtil.class);
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
			.setConnectionRequestTimeout(150000).build();

	private static HttpClientUtil instance = null;
	private static String CHARSET_UTF8 = "UTF-8";
	private static String EXCEPTION_CONTEXT = "异常信息";
	
	CloseableHttpClient httpClient = null;
	CloseableHttpResponse response = null;
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
			StringEntity stringEntity = new StringEntity(params, CHARSET_UTF8);
			stringEntity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			L.error(EXCEPTION_CONTEXT, e);
		}
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
		for (Map.Entry<String,String> entry : maps.entrySet()) {
		    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		  }
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, CHARSET_UTF8));
		} catch (Exception e) {
			L.error(EXCEPTION_CONTEXT, e);
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
		for (Map.Entry<String,String> entry : maps.entrySet()) {
		    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		  }
		for (Map.Entry<String,String> entry : header.entrySet()) {
			httpPost.setHeader(entry.getKey(), entry.getValue());
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			L.error(EXCEPTION_CONTEXT, e);
		}
		return sendHttpPost(httpPost);
	}

	public String sendHttpPostWav(String httpUrl, InputStream inputStream,String fileName) throws IOException {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();

		if (inputStream != null) {
			meBuilder.setCharset(Charset.forName("utf-8"));
			meBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 加上此行代码解决返回中文乱码问题
			meBuilder.addBinaryBody("file", inputStream, ContentType.MULTIPART_FORM_DATA,fileName);// 文件流
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
	public String sendHttpPost(String httpUrl, Map<String, String> maps, Map<String, String> header,
			List<File> fileLists, List<String> fileNames) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (Map.Entry<String,String> entry: maps.entrySet()) {
			meBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));
		}
		for (Map.Entry<String,String> entry : header.entrySet()) {
			httpPost.setHeader(entry.getKey(), entry.getValue());
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
		for (Map.Entry<String,String> entry: maps.entrySet()) {
			meBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));
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
		for (Map.Entry<String,String> entry: maps.entrySet()) {
			meBuilder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.TEXT_PLAIN));
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
	 * 发送 post请求（带文件）
	 * 
	 * @param httpUrl   地址
	 * @param maps      参数
	 * @param fileLists 附件
	 * @throws IOException
	 */
	public String sendHttpPost(String httpUrl, MultipartFile file) throws IOException {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();

		if (file != null) {
			meBuilder.setCharset(Charset.forName(CHARSET_UTF8));
			meBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 加上此行代码解决返回中文乱码问题
			meBuilder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());// 文件流
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
	 * @throws IOException
	 */
	public String sendHttpPost(String httpUrl, List<MultipartFile> files) throws IOException {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();

		if (files != null && files.size() > 0) {

			meBuilder.setCharset(Charset.forName(CHARSET_UTF8));
			meBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 加上此行代码解决返回中文乱码问题
			for (MultipartFile multipartFile : files) {
				meBuilder.addBinaryBody("files", multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, multipartFile.getOriginalFilename());// 文件流
			}

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
	private String sendHttpPost(HttpPost httpPost) {
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, CHARSET_UTF8);
		} catch (Exception e) {
			L.error(EXCEPTION_CONTEXT, e);
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
		for (Map.Entry<String,String> entry: headers.entrySet()) {
			httpGet.addHeader(entry.getKey(), entry.getValue());
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
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, CHARSET_UTF8);
		} catch (Exception e) {
			L.error(EXCEPTION_CONTEXT, e);
		} 
		return responseContent;
	}

	/**
	 * 发送Get请求Https
	 * 
	 * @param httpPost
	 * @return
	 */
	private String sendHttpsGet(HttpGet httpGet) {
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
			responseContent = EntityUtils.toString(entity, CHARSET_UTF8);
		} catch (Exception e) {
			L.error(EXCEPTION_CONTEXT, e);
		} 
		return responseContent;
	}

	public String sendHttpPost(String httpUrl, InputStream inputStream) throws IOException {
		HttpPost httpPost = new HttpPost(httpUrl);
		httpPost.addHeader("Content-Type", "application/octet-stream");
		StringBuffer sb = new StringBuffer();
		try {
		    byte [] buffer = new byte[1000];
		    int count = 0;
		    while ((count = inputStream.read(buffer) )> 0) {
		    	  sb.append(new String(buffer, 0, count, CHARSET_UTF8));
		    }
		  } catch (IOException e) { 
			  L.error(EXCEPTION_CONTEXT, e); 
		  }
		
		httpPost.setEntity(new StringEntity(sb.toString(), ContentType.create("image/jpg")));
		return sendHttpPost(httpPost);
	}

	@Override
	public void close() throws IOException {
		if (response != null) {
			response.close();
		}
		if (httpClient != null) {
			httpClient.close();
		}
	}
	
	
	
	/**
	 * 图片涂鸦发送 post请求（带文件）
	 *
	 * @param httpUrl   地址
	 * @param maps      参数
	 * @param fileLists 附件
	 * @throws IOException
	 */

	public String sendHttpPost2(String httpUrl, MultipartFile file) throws IOException {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();

		if (file != null) {
			meBuilder.setCharset(Charset.forName("utf-8"));
			meBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 加上此行代码解决返回中文乱码问题
			if(StringUtils.isNotBlankAndNotEmpty(FilenameUtils.getExtension(file.getOriginalFilename()))) {
				meBuilder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());// 文件流
			}else {
				meBuilder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, "1.jpg");// 文件流
			}
		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost);
	}
	
	

}
