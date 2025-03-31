package com.dchealth.cro.transfer.service;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.utils.VerifyCodeGenerator;
import com.dchealth.cro.core.tencent.SendTencentSmsVO;
import com.dchealth.cro.core.tencent.TencentSmsSevice;
import com.dchealth.cro.core.tencent.TencentSmsTemplateEnum;
import com.dchealth.cro.core.tencent.TencentSmsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class TransferService {
    private static final Logger L = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TencentSmsSevice tencentSmsSevice;


    public DchealthCloudResponseEntity<SendTencentSmsVO> getDoctorTencentSmsVO(SendVerfiyCodeForm sendVerfiyCodeForm) {
        DchealthCloudResponseEntity<SendTencentSmsVO> result = new DchealthCloudResponseEntity<SendTencentSmsVO>();
        String verifyCode = VerifyCodeGenerator.gen();
        redisCache.cacheString(RedisConstants.PHONE_CODE_PREFIX + sendVerfiyCodeForm.getAppId() + ":" + sendVerfiyCodeForm.getMobile(), verifyCode, 5,
                TimeUnit.MINUTES);
        SendTencentSmsVO sendTencentSmsVO = new SendTencentSmsVO(sendVerfiyCodeForm.getAppId(),new String[] { "+86" + sendVerfiyCodeForm.getMobile() },
                TencentSmsTemplateEnum.LOGIN_CODE.getTemplateId(),new String[] { verifyCode ,"5"});
        TencentSmsVO tencentSmsVO = tencentSmsSevice.getTencentSmsVO(sendVerfiyCodeForm.getAppId());
        L.info("医生端tencentSmsVO:{}", JSON.toJSONString(tencentSmsVO));
        sendTencentSmsVO.setTencentSmsVO(tencentSmsVO);
        result.setData(sendTencentSmsVO);
        return result;
    }


    public DchealthCloudResponseEntity<SendTencentSmsVO> getPatientTencentSmsVO(SendVerfiyCodeForm sendVerfiyCodeForm) {
        DchealthCloudResponseEntity<SendTencentSmsVO> result = new DchealthCloudResponseEntity<SendTencentSmsVO>();
        String verifyCode = VerifyCodeGenerator.gen();
        redisCache.cacheString(RedisConstants.PHONE_CODE_PREFIX + sendVerfiyCodeForm.getAppId() + ":" + sendVerfiyCodeForm.getMobile(), verifyCode, 5,
                TimeUnit.MINUTES);
        SendTencentSmsVO sendTencentSmsVO = new SendTencentSmsVO(sendVerfiyCodeForm.getAppId(),new String[] { "+86" + sendVerfiyCodeForm.getMobile() },
                TencentSmsTemplateEnum.LOGIN_CODE.getTemplateId(),new String[] { verifyCode ,"5"});
        TencentSmsVO tencentSmsVO = tencentSmsSevice.getTencentSmsVO(sendVerfiyCodeForm.getAppId());
        L.info("患者端tencentSmsVO:{}", JSON.toJSONString(tencentSmsVO));
        sendTencentSmsVO.setTencentSmsVO(tencentSmsVO);
        result.setData(sendTencentSmsVO);
        return result;
    }


}
