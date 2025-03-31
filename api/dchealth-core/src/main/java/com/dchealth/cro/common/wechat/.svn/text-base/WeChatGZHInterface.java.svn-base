package com.dchealth.cro.common.wechat;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信公众号
 */
public class WeChatGZHInterface {

    /**
     * 公众号消息模板
     * @param access_token
     * @param weChatGZHMessageVO
     */
    public static DchealthCloudResponseEntity<String> sendMessage(String access_token,WeChatGZHMessageVO weChatGZHMessageVO){
        DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
        if(StringUtils.isEmpty(access_token) || weChatGZHMessageVO == null || StringUtils.isEmpty(weChatGZHMessageVO.getTouser())
                || StringUtils.isEmpty(weChatGZHMessageVO.getData())){
            result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
            result.setMsg(CroApiCodeConstant.ERROR_PARAM.getMsg());
            return result;
        }
        String params = JSON.toJSONString(weChatGZHMessageVO);
        String url = WeChatConstants.MESSAGE_TEMPLATE_SEND + "?access_token=" + access_token;
        String json = HttpClientUtil.getInstance().sendHttpPost(url,params);
        if (StringUtils.isNotNullOrEmpty(json)) {
            JSONObject rsJsonObject = JSON.parseObject(json);
            Integer errocode = rsJsonObject.getInteger("errcode");
            if (errocode == 0){
            }else{
                result.setCode(errocode);
                String errmsg = rsJsonObject.getString("errmsg");
                result.setMsg(errmsg);
            }
        }else {
            result.setCode(CroApiCodeConstant.OVER_TIME.getCode());
            result.setMsg(CroApiCodeConstant.OVER_TIME.getMsg());
        }
        return result;
    }


    public static String getUserListPage(String accessToken,String nextOpenid){
        String url = WeChatConstants.USER_GET + "?access_token="+accessToken;
        if (StringUtils.isNotEmpty(nextOpenid)){
            url = url + "&next_openid="+nextOpenid;
        }
        String json = HttpClientUtil.getInstance().sendHttpGet(url);
        return json;
    }

    /**
     * 获取用户信息
     * @param accessToken
     * @param openIds
     * @return
     */
    public static List<WechatUserInfo> getUserInfoList(String accessToken, List<String> openIds){
        List<WechatUserInfo> wechatUserInfos = new ArrayList<>();
        String url = WeChatConstants.USER_INFO_BATCHGET + "?access_token="+accessToken;
        if (openIds.size() > 0){
            if (openIds.size() > 100){
                int count = openIds.size()/100 + (openIds.size() % 100 > 0 ? 1 : 0);
                for (int i = 0;i<count;i++){
                    Map<String,List<Map<String,String>>> params = new HashMap<>();
                    List<Map<String,String>> maps = new ArrayList<>();
                    int start = i * 100;
                    int end = start + 100;
                    if (end > openIds.size()){
                        end = openIds.size();
                    }
                    for (int c = start;c<end;c++){
                        Map<String,String> map = new HashMap<>();
                        map.put("openid",openIds.get(c));
                        maps.add(map);
                    }
                    params.put("user_list",maps);
                    String json = HttpClientUtil.getInstance().sendHttpPost(url,JSON.toJSONString(params));
                    if (StringUtils.isNotEmpty(json)){
                        JSONObject rsJsonObject = JSON.parseObject(json);
                        JSONArray jsonArray = rsJsonObject.getJSONArray("user_info_list");
                        if (!jsonArray.isEmpty()){
                            List<WechatUserInfo> ruserInfos = jsonArray.toJavaList(WechatUserInfo.class);
                            wechatUserInfos.addAll(ruserInfos);
                        }
                    }
                }
            }else{
                Map<String,List<Map<String,String>>> params = new HashMap<>();
                List<Map<String,String>> maps = new ArrayList<>();
                openIds.forEach(item -> {
                    Map<String,String> map = new HashMap<>();
                    map.put("openid",item);
                    maps.add(map);
                });
                params.put("user_list",maps);
                String json = HttpClientUtil.getInstance().sendHttpPost(url,JSON.toJSONString(params));
                if (StringUtils.isNotEmpty(json)){
                    JSONObject rsJsonObject = JSON.parseObject(json);
                    JSONArray jsonArray = rsJsonObject.getJSONArray("user_info_list");
                    if (!jsonArray.isEmpty()){
                        List<WechatUserInfo> ruserInfos = jsonArray.toJavaList(WechatUserInfo.class);
                        wechatUserInfos.addAll(ruserInfos);
                    }
                }
            }
        }

        return wechatUserInfos;
    }
}
