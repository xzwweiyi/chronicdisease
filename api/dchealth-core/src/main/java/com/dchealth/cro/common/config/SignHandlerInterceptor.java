package com.dchealth.cro.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.crypto.SM3Utils;
import com.dchealth.cro.common.crypto.Sm2Utils;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.mapper.SysDeptMapper;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignHandlerInterceptor implements HandlerInterceptor {
    private static final Logger L = LoggerFactory.getLogger(SignHandlerInterceptor.class);
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private SysDeptMapper sysDeptMapper;

    //在业务处理器处理请求之前被调用
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestPath = getRequestPath(request);// 用户访问的资源地址
        // 解析token信息
        try {
            // 权限路径拦截
            response.setContentType("application/json;charset=utf-8");
            response.setHeader("Cache-Control", "no-store");
            response.setCharacterEncoding("utf-8");
            response.addHeader("connection", "keep-alive");
            String deviceToken = request.getHeader("deviceToken") == null ? "" : request.getHeader("deviceToken");// 设备ID
            String version = request.getHeader("version") == null ? "" : request.getHeader("version");// 版本
            String sign = request.getHeader("sign") == null ? "" : request.getHeader("sign");// 签名
            String time = request.getHeader("time") == null ? "" : request.getHeader("time");// 时间
            String channel = request.getHeader("channel") == null ? "" : request.getHeader("channel");// 渠道 1
            String osType = request.getHeader("osType") == null ? "" : request.getHeader("osType");// 版本 1 Android 2 IOS
            String appType = request.getHeader("appType") == null ? "" : request.getHeader("appType");// appType 1 医生端 2患者端
            String openId = request.getHeader("openId") == null ? "" : request.getHeader("openId");// openId
            String rId = request.getHeader("researchId") == null ? null : request.getHeader("researchId");//
            String accountId = request.getHeader("accountId") == null ? null : request.getHeader("accountId");//
            L.info("请求路径{},参数deviceToken:{},时间戳{},版本version:{},渠道channel:{},操作系统osType:{},OpenId{},队列Id:{},签名sign:{}", requestPath, deviceToken, time, version, channel, osType, openId, rId, sign);
//			printRequestParams(request);


            if (isExclude(requestPath)) {
                return true;
            }
            if (isMinProgram(requestPath)) {
                //校验参数不能为空
                if ("".equals(openId) || "".equals(sign)) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                    L.info("请求路径{},OpenId{},签名sign:{} 参数有误", requestPath, openId, sign);
                    return false;
                }
                boolean b = SM3Utils.verify(openId, sign);
                if (!b) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.SYSTEM_SIGN_ERROR.getCode()));
                    L.info("请求路径{},OpenId{},签名sign:{} 签名有误", requestPath, openId, sign);
                    return false;
                }
                request.setAttribute("openId", openId);
                return true;
            }
            if (isTransferProgram(requestPath)) {
                //校验参数不能为空
                if ("".equals(version) || "".equals(sign) || "".equals(time)) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                    L.info("请求路径{},时间戳{},版本version:{},签名sign:{}参数有误", requestPath, time, version, sign);
                    return false;
                }
                // 校验时间
                try {
                    boolean bDateValide = DateUtils.isDateBetweenTime(time, 5 * 12 * 24);
                    if (!bDateValide) {
                        response.getWriter().write(getResponseMsg(CroApiCodeConstant.OVER_TIME.getCode()));
                        return false;
                    }
                } catch (ParseException e) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                    L.info("请求路径{},校验时间{} 参数有误", requestPath, time);
                    return false;
                }
                StringBuffer transSb = new StringBuffer();
                transSb.append("time=").append(time).append("&version=").append(version);
                boolean b = Sm2Utils.verify(Constants.RAS_DEFAULT_PUBLICE_KEY, Hex.toHexString(transSb.toString().getBytes()), sign);
                if (!b) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.SYSTEM_SIGN_ERROR.getCode()));
                    return false;
                }
                return true;
            }

            //校验参数不能为空
            if ("".equals(deviceToken) || "".equals(version) || "".equals(sign) || "".equals(time) || "".equals(channel) || "".equals(osType)) {
                response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                L.info("请求路径{},参数deviceToken:{},时间戳{},版本version:{},渠道channel:{},操作系统osType:{},OpenId{},签名sign:{}参数有误", requestPath, deviceToken, time, version, channel, osType, openId, sign);
                return false;
            }
            //校验渠道值
            if (!("1".equals(channel) || "11".equals(channel))) {
                response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                L.info("请求路径{},channel{} 参数有误", requestPath, channel);
                return false;
            }
            //系统操作系统类型
            if (!("1".equals(osType) || "2".equals(osType) || "3".equals(osType))) {
                response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                L.info("请求路径{},系统操作系统类型{} 参数有误", requestPath, osType);
                return false;
            }
            // 校验时间
            try {
                boolean bDateValide = DateUtils.isDateBetweenTime(time, 5 * 12 * 24);
                if (!bDateValide) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.OVER_TIME.getCode()));
                    return false;
                }
            } catch (ParseException e) {
                response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                L.info("请求路径{},校验时间{} 参数有误", requestPath, time);
                return false;
            }
            //验签处理
            StringBuffer sb = new StringBuffer();
            sb.append("deviceToken=").append(deviceToken).append("&time=").append(time).append("&version=")
                    .append(version).append("&channel=").append(channel).append("&osType=").append(osType);
            String publicKey = Constants.RAS_DEFAULT_PUBLICE_KEY;
            if (isDefault(requestPath)) {
                publicKey = Constants.RAS_DEFAULT_PUBLICE_KEY;
            } else {
                publicKey = redisCache.getCacheString(RedisConstants.TOKEN_PREFIX + channel + ":" + deviceToken);
                if (StringUtils.isBlank(publicKey) || StringUtils.isEmpty(publicKey)) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.USER_NOT_LOGIN.getCode()));
                    return false;
                }
            }
            if (StringUtils.isNotNullOrEmpty(publicKey)) {

                boolean b = Sm2Utils.verify(publicKey, Hex.toHexString(sb.toString().getBytes()), sign);
                if (!b) {
                    response.getWriter().write(getResponseMsg(CroApiCodeConstant.SYSTEM_SIGN_ERROR.getCode()));
                    return false;
                }
            } else {
                response.getWriter().write(getResponseMsg(CroApiCodeConstant.ERROR_PARAM.getCode()));
                L.info("请求路径{},验签处理异常参数有误", requestPath);
                return false;
            }
            //功能路径权限认证
            if ("1".equals(appType) && StringUtils.isNotBlankAndNotEmpty(rId) && StringUtils.isNotBlankAndNotEmpty(accountId)) {
                if (!isExcludeApp(requestPath)) {
                    String appAuthJson = redisCache.getCacheString(RedisConstants.TOKEN_APP_AUTH_PREFIX + channel + ":" + accountId + ":" + Long.valueOf(rId));
                    List<String> authList = null;
                    if (StringUtils.isNotBlankAndNotEmpty(appAuthJson)) {
                        authList = JSON.parseArray(appAuthJson, String.class);
                    } else {
                        authList = sysDeptMapper.selectSourceByResearchIdAndUserId(Long.valueOf(accountId), Long.valueOf(rId));
                        if (authList != null && authList.size() > 0) {
                            redisCache.cacheString(RedisConstants.TOKEN_APP_AUTH_PREFIX + channel + ":" + accountId + ":" + Long.valueOf(rId), JSON.toJSONString(authList), 1, TimeUnit.HOURS);
                        }
                    }


                    if (authList != null && authList.size() > 0) {
                        boolean bAuth = hasAuth(authList, requestPath);
                        if (!bAuth) {
                            response.getWriter().write(getResponseMsg(CroApiCodeConstant.AUTH_NOT_ALLOW.getCode()));
                            L.error("请求路径{},返回结果{}", requestPath, getResponseMsg(CroApiCodeConstant.AUTH_NOT_ALLOW.getCode()));
                            return false;
                        }

                    } else {
                        response.getWriter().write(getResponseMsg(CroApiCodeConstant.AUTH_NOT_ALLOW.getCode()));
                        L.error("请求路径{},返回结果{}", requestPath, getResponseMsg(CroApiCodeConstant.AUTH_NOT_ALLOW.getCode()));
                        return false;
                    }
                }
            }
            request.setAttribute("deviceToken", deviceToken);
            request.setAttribute("channel", channel);
            request.setAttribute("appType", appType);
        } catch (Exception e) {
            L.error("请求异常{}", e);
            response.getWriter().write(getResponseMsg(CroApiCodeConstant.SYSTEM_SIGN_ERROR.getCode()));
            return false;
        }
        // 放行
        return true;

    }


    //postHandle：在业务处理器处理请求执行完成后，生成视图之前执行。后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView （这个博主就基本不怎么用了）；
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //afterCompletion：在DispatcherServlet完全处理完请求后被调用，可用于清理资源等。返回处理（已经渲染了页面）；
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    /**
     * 医生端不需要进行权限认证的地址
     *
     * @param requestPath
     * @return
     */
    public Boolean isExcludeApp(String requestPath) {
        String[] excludeUrls = new String[]{"/research/queueTypeList", "/user/*", "/article/*", "/system/*", "/doctor/*", "/cmsDoctor/*", "/comment/*"};//进行APP权限认证的
        for (String excludeUrl : excludeUrls) {
            if (StringUtils.isNotEmpty(requestPath) && StringUtils.isNotBlank(requestPath)) {
                if (excludeUrl.lastIndexOf("*") != -1) {
                    String exUrls = excludeUrl.substring(0, excludeUrl.lastIndexOf("*") - 1);
                    if (requestPath.startsWith(exUrls)) {
                        return true;
                    }
                } else {
                    if (excludeUrl.equals(requestPath)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 权限认证
     *
     * @param urls
     * @param requestPath
     * @return
     */
    public Boolean hasAuth(List<String> urls, String requestPath) {
        if (StringUtils.isNotEmpty(requestPath) && StringUtils.isNotBlank(requestPath)) {
            for (String url : urls) {
                if (StringUtils.isNotBlankAndNotEmpty(url) && url.equalsIgnoreCase(requestPath)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取国际化内容
     *
     * @return
     */
    private String getResponseMsg(Integer code) {
        JSONObject jobj = new JSONObject();
        jobj.put("code", code);
        jobj.put("msg", CroApiCodeConstant.getMsg(code));
        return jobj.toString();
    }

    /**
     * 获得请求路径
     *
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        requestPath = requestPath.substring(request.getContextPath().length());// 去掉项目路径
        return requestPath;
    }


    /**
     * 判断是否默认
     *
     * @param requestPath
     * @return
     */
    public Boolean isDefault(String requestPath) {
        String[] excludeUrls = new String[]{"/user/*", "/article/*", "/system/*", "/research/*", "/doctor/updatePasswordByOldPassword", "/patientAccount/*", "/doctorAccount/*", "/cmsDoctor/*", "/cmsPatient/*", "/api/*"};//使用默认签名地址
        for (String excludeUrl : excludeUrls) {
            if (StringUtils.isNotEmpty(requestPath) && StringUtils.isNotBlank(requestPath)) {
                if (excludeUrl.lastIndexOf("*") != -1) {
                    String exUrls = excludeUrl.substring(0, excludeUrl.lastIndexOf("*") - 1);
                    if (requestPath.startsWith(exUrls)) {
                        return true;
                    }
                } else {
                    if (excludeUrl.equals(requestPath)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 不需要验签的接口
     *
     * @param requestPath
     * @return
     */

    public Boolean isExclude(String requestPath) {
        String[] excludeUrls = new String[]{"/min_program/detailStandard", "/doctor/testMessagePush", "/transfer/sms/*", "/getIsShow", "/myQueue/*", "/medical/*", "/api/*", "/min_program/getOpenId"};//不需要验签的接口
//		String[] excludeUrls = new String[] {  "/*"};//不需要验签的接口
        for (String excludeUrl : excludeUrls) {
            if (StringUtils.isNotEmpty(requestPath) && StringUtils.isNotBlank(requestPath)) {
                if (excludeUrl.lastIndexOf("*") != -1) {
                    String exUrls = excludeUrl.substring(0, excludeUrl.lastIndexOf("*") - 1);
                    if (requestPath.startsWith(exUrls)) {
                        return true;
                    }
                } else {
                    if (excludeUrl.equals(requestPath)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 小程序验签的接口
     *
     * @param requestPath
     * @return
     */
    public Boolean isMinProgram(String requestPath) {
        String[] excludeUrls = new String[]{"/min_program/*"};//不需要验签的接口
        for (String excludeUrl : excludeUrls) {
            if (StringUtils.isNotEmpty(requestPath) && StringUtils.isNotBlank(requestPath)) {
                if (excludeUrl.lastIndexOf("*") != -1) {
                    String exUrls = excludeUrl.substring(0, excludeUrl.lastIndexOf("*") - 1);
                    if (requestPath.startsWith(exUrls)) {
                        return true;
                    }
                } else {
                    if (excludeUrl.equals(requestPath)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 中转项目验签的接口
     */
    public Boolean isTransferProgram(String requestPath) {
        String[] excludeUrls = new String[]{"/transfer/recordOutbound/*", "/transfer/accountNotice/*"};//走中转服务验签
        for (String excludeUrl : excludeUrls) {
            if (StringUtils.isNotEmpty(requestPath) && StringUtils.isNotBlank(requestPath)) {
                if (excludeUrl.lastIndexOf("*") != -1) {
                    String exUrls = excludeUrl.substring(0, excludeUrl.lastIndexOf("*") - 1);
                    if (requestPath.startsWith(exUrls)) {
                        return true;
                    }
                } else {
                    if (excludeUrl.equals(requestPath)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
