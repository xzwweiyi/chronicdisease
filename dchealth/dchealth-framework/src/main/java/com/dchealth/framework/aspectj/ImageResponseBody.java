package com.dchealth.framework.aspectj;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.coyote.http11.Constants.a;

@ControllerAdvice
public class ImageResponseBody implements ResponseBodyAdvice {

    @Value("${image.interceptPath}")
    private String interceptPath;
    @Value("${image.interceptIp}")
    private String interceptIp;
    @Value("${image.oldDomain}")
    private String oldDomain;
    @Value("${image.newDomain}")
    private String newDomain;

    private Logger logger = LoggerFactory.getLogger(ImageResponseBody.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestUrl = request.getRequestURL().toString();
        String requestIp = extractIPAddress(requestUrl);
        String interceptIpStr = extractIPAddress(interceptIp);
        logger.info("requestIp:{};interceptIp:{}",requestIp,interceptIpStr);
        if (interceptIpStr.equals(requestIp)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String requestURI = request.getRequestURI();
        logger.info("requestURI:{}",requestURI);
        List<String> matcherList = getMatcherList(interceptPath);
        for (String matcherStr : matcherList){
            Pattern pattern = Pattern.compile(matcherStr);
            Matcher matcher = pattern.matcher(requestURI);
            if (matcher.matches()){
                String jsonString = JSON.toJSONString(o);
                if (jsonString.contains(oldDomain)){
                    String replace = jsonString.replace(oldDomain, newDomain);
                    JSONObject json = null;
                    try {
                        json = JSONObject.parseObject(replace);
                        return json;
                    }catch (Exception ex){
                        json = (JSONObject) JSON.toJSON(replace);
                        return json;
                    }finally {
                        if (json ==null){
                            return o;
                        }
                    }
                }

            }

        }
        return o;
    }

    public static List<String> getMatcherList(String matcher) {
        List<String> list = new ArrayList<>();
        String[] split = matcher.split(",");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            StringBuffer sb = new StringBuffer();
            sb.append("^");
            for (char j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if (c != '*') {
                    sb.append(c);
                } else {
                    j++;
                    sb.append("[a-zA-Z0-9/_]*");
                }
            }
//            sb.append("$");
            list.add(sb.toString());
        }
        return list;
    }
    public static String extractIPAddress(String input) {
        String regex = "\\b(?:\\d{1,3}\\.*){3}\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String group = matcher.group();
            if (!group.substring(group.length()-1).equals(".")){
                group = group+".";
            }
            return group;
        }
        return "";
    }
}