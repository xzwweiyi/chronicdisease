//package com.dchealth.cro.aspectj;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.dchealth.cro.common.utils.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.MethodParameter;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//@Order(0)
//@ControllerAdvice
//public class ImageResponseBody implements ResponseBodyAdvice {
//
//    @Value("${image.interceptPath}")
//    private String interceptPath;
//    @Value("${image.interceptIp}")
//    private String interceptIp;
//    @Value("${image.oldDomain}")
//    private String oldDomain;
//    @Value("${image.newDomain}")
//    private String newDomain;
//
//    private Logger logger = LoggerFactory.getLogger(ImageResponseBody.class);
//
//    @Override
//    public boolean supports(MethodParameter methodParameter, Class aClass) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String requestHost = request.getServerName(); // 获取请求的域名
//        logger.info("request:{}", request);
//
//        logger.info("requestHost:{}", requestHost);
//        // 检查域名是否匹配配置
//        for (String domainPattern : interceptIp.split(",")) {
//            if (matchDomain(requestHost, domainPattern.trim())) {
//
//                logger.info("requestHost-------------------------------------->:{}", requestHost);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean matchDomain(String requestHost, String pattern) {
//        pattern = pattern.replace(".", "\\.").replace("*", ".*");
//        return Pattern.matches(pattern, requestHost);
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String requestURI = request.getRequestURI();
//        logger.info("小程序beforeBodyWrite requestURI:{}", requestURI);
//        List<String> matcherList = getMatcherList(interceptPath);
//        logger.info("小程序beforeBodyWrite o:{}", o);
//        for (String matcherStr : matcherList) {
//            logger.info("matcherStr: {}", matcherStr);
//            Pattern pattern = Pattern.compile(matcherStr);
//            Matcher matcher = pattern.matcher(requestURI);
//            if (matcher.matches()) {
//                String jsonString = JSON.toJSONString(o);
//                logger.info("jsonString ============== >{}", jsonString);
////                if (jsonString.contains(oldDomain)){
////                    logger.info("jsonString contains oldDomain");
////                    String replace = jsonString.replace(oldDomain, newDomain);
//                if (jsonString.contains("http://172.100.45.33")) {
//                    logger.info("jsonString contains oldDomain");
//                    String replace = jsonString.replace("http://172.100.45.33", "https://td.dchealthclinical.com");
//
//                    logger.info("replace {}", replace);
//                    JSONObject json = null;
//                    try {
//                        json = JSONObject.parseObject(replace);
//                        logger.info("json {}", json);
//                        return json;
//                    } catch (Exception ex) {
//                        json = (JSONObject) JSON.toJSON(replace);
//                        return json;
//                    } finally {
//                        if (json == null) {
//                            return o;
//                        }
//                    }
//                }
//
//            }
//
//        }
//        return o;
//    }
//
//    public static List<String> getMatcherList(String matcher) {
//        List<String> list = new ArrayList<>();
//        String[] split = matcher.split(",");
//        for (int i = 0; i < split.length; i++) {
//            String s = split[i];
//            StringBuffer sb = new StringBuffer();
//            sb.append("^");
//            for (char j = 0; j < s.length(); j++) {
//                char c = s.charAt(j);
//                if (c != '*') {
//                    sb.append(c);
//                } else {
//                    j++;
//                    sb.append("[a-zA-Z0-9/_]*");
//                }
//            }
////            sb.append("$");
//            list.add(sb.toString());
//        }
//        return list;
//    }
//
//    public static String extractIPAddress(String input) {
//        String regex = "\\b(?:\\d{1,3}\\.*){3}\\b";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(input);
//
//        if (matcher.find()) {
//            String group = matcher.group();
//            if (!group.substring(group.length() - 1).equals(".")) {
//                group = group + ".";
//            }
//            return group;
//        }
//        return "";
//    }
//
//
//}