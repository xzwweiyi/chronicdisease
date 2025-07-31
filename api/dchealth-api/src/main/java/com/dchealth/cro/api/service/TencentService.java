package com.dchealth.cro.api.service;


import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.AccountNotice;
import com.dchealth.cro.account.mapper.AccountNoticeMapper;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.contants.CroCoreConstant;
import com.dchealth.cro.core.domain.AppSet;
import com.dchealth.cro.core.mapper.AppSetMapper;
import com.dchealth.cro.core.tencent.SendTencentSmsVO;
import com.dchealth.cro.core.tencent.TencentSmsVO;
import com.dchealth.cro.enums.TencentSmsAdminTemplateEnum;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/28 18:26
 * @Version: 1.0
 */
@Slf4j
@Component
public class TencentService {

    private static final String APP_ID = "2";

    @Autowired
    private RedisCache redisCache;

    @Value("${xunfei.requestUrl}")
    private String baseUrl ;

    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private AppSetMapper appSetMapper;
    @Autowired
    private AccountNoticeMapper accountNoticeMapper;
    @Autowired
    private ResearchInfoMapper researchInfoMapper;
    private SendTencentSmsVO getTencentSmsVOConfig() {

        TencentSmsVO tencentSmsVO = new TencentSmsVO();
        String cacheValue = redisCache.getCacheString(CroCoreConstant.TENCENT_SMS_PREFIX + APP_ID );
        if (StringUtils.isNotEmpty(cacheValue)) {
            tencentSmsVO = JSON.parseObject(cacheValue, TencentSmsVO.class);
        } else {
            AppSet  appSetSearch = new AppSet();
            appSetSearch.setAppId(Long.valueOf(APP_ID));
            appSetSearch.setConfigKey(CroCoreConstant.TENCENT_SMS);
            List<AppSet> list = appSetMapper.selectAppSetList(appSetSearch);
            if (list != null && list.size() > 0) {
                AppSet appSet  = list.get(0);
                if(appSet != null ) {
                    redisCache.cacheString(CroCoreConstant.TENCENT_SMS_PREFIX + APP_ID, appSet.getConfigValue(),3, TimeUnit.DAYS);
                    tencentSmsVO = JSON.parseObject(appSet.getConfigValue(), TencentSmsVO.class);
                }

            }
        }
        SendTencentSmsVO sendTencentSmsVO = new SendTencentSmsVO();
        sendTencentSmsVO.setAppId(APP_ID);
        sendTencentSmsVO.setTemplateId(TencentSmsAdminTemplateEnum.JOIN_RESEARCH.getTemplateId());
        sendTencentSmsVO.setTencentSmsVO(tencentSmsVO);
//      new SendTencentSmsVO(APP_ID,new String[] { "+86" + info.getMobile() },
//                TencentSmsAdminTemplateEnum.JOIN_RESEARCH.getTemplateId(),new String[] { info.getPatientName()});
        return sendTencentSmsVO;
    }

    public Boolean asyncSendSms(PatientInfo patientInfoDO) {
        log.info("这儿是短信发送, {}", patientInfoDO);
        //先发送, 再处理缓存等相关优化
        SendTencentSmsVO sendTencentSmsVO = getTencentSmsVOConfig();
        if (StringUtils.isEmpty(patientInfoDO.getPatientMobile())) {
            throw new ServiceException("患者手机号为空");
        }
        sendTencentSmsVO.setPhones(new String[]{"+86" + patientInfoDO.getPatientMobile()});
        sendTencentSmsVO.setTemplateParam(new String[]{patientInfoDO.getPatientName()});
        //发往xfapi中转服务器
        AccountNotice accountNotice = new AccountNotice();
        accountNotice.setAccountId(patientInfoDO.getAccountId());
        accountNotice.setType("23");  // 预问诊自动入组
        accountNotice.setCreateTime(new Date());
        accountNotice.setContent("短信通知");
        accountNotice.setTitle("短信通知");
//        accountNotice.setRecordId(0L);
        Map<String, Object> extjson = new HashMap<>();
        extjson.put("type", "23");
        extjson.put("patientId", String.valueOf(patientInfoDO.getPatientId()));
        if (StringUtils.isNotEmpty(patientInfoDO.getProjectName())) {
            extjson.put("researchName", patientInfoDO.getProjectName());
        }else {
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(patientInfoDO.getResearchId());
            extjson.put("researchName", researchInfo.getProjectName());
        }
//        extjson.put("recordId", 0);
        extjson.put("sendTencentSmsVO", sendTencentSmsVO);
        accountNotice.setExtendJson(com.alibaba.fastjson.JSON.toJSONString(extjson));
        accountNoticeMapper.insertAccountNotice(accountNotice);
        log.info("短信插入库={}", accountNotice);
        return true;
    }
}
