//import com.alibaba.fastjson.JSON;
//import com.dchealth.cro.ApiApplication;
//import com.dchealth.cro.common.constant.Constants;
//import com.dchealth.cro.common.crypto.Sm2Utils;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.bouncycastle.crypto.CryptoException;
//import org.bouncycastle.util.encoders.Hex;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * @Author: wangpf
// * @Description: TODO
// * @Date: 2025/7/25 11:47
// * @Version: 1.0
// */
////@SpringBootTest(classes = ApiApplication.class)
//public class YuApiTest {
//
//    @Test
//    public void test() {
//
//        String httpUrl = "http://127.0.0.1:7918/tdapi/api/bind";
//        Map<String, String> maps = new HashMap<>();
//        maps.put("card", "610111197403033032");
//        maps.put("personId", "10001685");
//        maps.put("mobile", "15300000000");
//        String result = sendSzDefaultHttpPost(httpUrl, maps);
//        System.out.println(result);
//
//    }
//
//    public String sendSzDefaultHttpPost(String httpUrl, Map<String, String> maps) {
//        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
//        // 创建参数队列
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        for (String key : maps.keySet()) {
//            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
//        }
//        Map<String, String> headers = new HashMap<String, String>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = sdf.format(new Date());
//        String version = "1.0.0";
//        headers.put("version", version);
//        headers.put("time", time);
//        StringBuffer sb = new StringBuffer();
//        sb.append("time=").append(time).append("&version=").append(version);
//        try {
//            String sign = "";
//            try {
//                sign = Sm2Utils.sign(Constants.RAS_DEFAULT_PRIVATE_KEY, Hex.toHexString(sb.toString().getBytes()));
//            } catch (CryptoException e) {
//                e.printStackTrace();
//            }
//            headers.put("sign", sign);
//            for (String key : headers.keySet()) {
//                httpPost.setHeader(key, headers.get(key));
//            }
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println(JSON.toJSONString(httpPost));
//        return sendHttpPost(httpPost);
//    }
//
//    private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(150000).setConnectTimeout(150000)
//            .setConnectionRequestTimeout(150000).build();
//
//    public String sendHttpPost(HttpPost httpPost) {
//        CloseableHttpClient httpClient = null;
//        CloseableHttpResponse response = null;
//        HttpEntity entity = null;
//        String responseContent = null;
//        try {
//            // 创建默认的httpClient实例.
//            httpClient = HttpClients.createDefault();
//            httpPost.setConfig(requestConfig);
//            // 执行请求
//            response = httpClient.execute(httpPost);
//            entity = response.getEntity();
//            responseContent = EntityUtils.toString(entity, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                // 关闭连接,释放资源
//                if (response != null) {
//                    response.close();
//                }
//                if (httpClient != null) {
//                    httpClient.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return responseContent;
//    }
//}
