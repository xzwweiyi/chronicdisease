package com.dchealth.cro.cms.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.form.SendVerfiyCodeForm;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.cms.vo.*;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.core.tencent.TencentSmsSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class DoctorAccountService {
    private static final Logger L = LoggerFactory.getLogger(DoctorAccountService.class);

    @Autowired
    private TencentSmsSevice tencentSmsSevice;

    @Autowired
    private XunfeiService xunfeiService;

    @Autowired
    private CroCoreConfig croCoreConfig;

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;

    public DchealthCloudResponseEntity<CommonResult> sendAccountVerifyCode(SendVerfiyCodeForm sendVerfiyCodeForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("mobile", sendVerfiyCodeForm.getMobile());
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/sms/doctorTencentSmsVO", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        if(httpResult!=null && "1000".equals(code)){
            //成功
            SendTencentSmsVO sendTencentSmsVO = JSONObject.parseObject(jsonObject.getString("data"),SendTencentSmsVO.class);
            boolean isTrue = xunfeiService.sendVerifyCodeWithXunfei(sendTencentSmsVO);

//            Boolean isTrue = tencentSmsSevice.sendTencentSMS(sendTencentSmsVO.getAppId(),sendTencentSmsVO.getPhones(),
//                    sendTencentSmsVO.getTemplateId(),sendTencentSmsVO.getTemplateParam(),sendTencentSmsVO.getTencentSmsVO());
            commonResult.setResult(isTrue);
        }else{
            commonResult.setResult(false);
        }
        result.setData(commonResult);
        return result;
    }

    public DchealthCloudResponseEntity<CommonResult> sendPatientVerifyCode(SendVerfiyCodeForm sendVerfiyCodeForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("mobile", sendVerfiyCodeForm.getMobile());
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/sms/patientTencentSmsVO", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject != null ? jsonObject.getString("code") : null;
        if(httpResult!=null && "1000".equals(code)){
            //成功
            SendTencentSmsVO sendTencentSmsVO = JSONObject.parseObject(jsonObject.getString("data"),SendTencentSmsVO.class);
            boolean isTrue = xunfeiService.sendVerifyCodeWithXunfei(sendTencentSmsVO);
//            Boolean isTrue = tencentSmsSevice.sendTencentSMS(sendTencentSmsVO.getAppId(),sendTencentSmsVO.getPhones(),
//                    sendTencentSmsVO.getTemplateId(),sendTencentSmsVO.getTemplateParam(),sendTencentSmsVO.getTencentSmsVO());
            commonResult.setResult(isTrue);
        }else{
            commonResult.setResult(false);
        }
        result.setData(commonResult);
        return result;
    }


    public DchealthCloudResponseEntity<CommonResult> sendPatientMinVerifyCode(SendVerfiyCodeForm sendVerfiyCodeForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("mobile", sendVerfiyCodeForm.getMobile());
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/sms/patientTencentMinSmsVO", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject.getString("code");
        if(httpResult!=null && "1000".equals(code)){
            //成功
            SendTencentSmsVO sendTencentSmsVO = JSONObject.parseObject(jsonObject.getString("data"),SendTencentSmsVO.class);
            boolean isTrue = xunfeiService.sendVerifyCodeWithXunfei(sendTencentSmsVO);
//            Boolean isTrue = tencentSmsSevice.sendTencentSMS(sendTencentSmsVO.getAppId(),sendTencentSmsVO.getPhones(),
//                    sendTencentSmsVO.getTemplateId(),sendTencentSmsVO.getTemplateParam(),sendTencentSmsVO.getTencentSmsVO());
            commonResult.setResult(isTrue);
        }else{
            commonResult.setResult(false);
        }
        result.setData(commonResult);
        return result;
    }


    /**
     * 邀请入组短信
     *
     * @param b
     * @param sendTencentSmsVO
     * @param researchName
     * @return
     */
    public DchealthCloudResponseEntity<CommonResult> sendPatientJoinResearch(SendTencentSmsVO sendTencentSmsVO, boolean isBind, String researchName) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
//        Boolean isTrue = tencentSmsSevice.sendTencentSMS((String) sendVerfiyCodeForm.get("appid"),
//                new String[]{"+86" + "1381234578"},
//                "2063634",
//                new String[]{"zhangs"},
//                sendTencentSmsVO.getTencentSmsVO());
        boolean isTrue = xunfeiService.sendJoinNotifyWithXunfei(sendTencentSmsVO,isBind,researchName);
//        Boolean isTrue = tencentSmsSevice.sendJoinNotifyWithXunfei(sendTencentSmsVO.getAppId(),sendTencentSmsVO.getPhones(),
//                sendTencentSmsVO.getTemplateId(),sendTencentSmsVO.getTemplateParam(),sendTencentSmsVO.getTencentSmsVO());
        if (isTrue) {
            L.info("{}发送成功", String.join(",",sendTencentSmsVO.getPhones()));
        } else {
            L.info("{}发送失败", String.join(",",sendTencentSmsVO.getPhones()));
        }
        commonResult.setResult(isTrue);
        result.setData(commonResult);
        return result;
    }
}
