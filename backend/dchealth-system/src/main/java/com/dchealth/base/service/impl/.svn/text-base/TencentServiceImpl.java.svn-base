package com.dchealth.base.service.impl;

import com.alibaba.fastjson2.JSON;
import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.mapper.AccountNoticeMapper;
import com.dchealth.base.domain.AppSet;
import com.dchealth.base.mapper.AppSetMapper;
import com.dchealth.base.service.TencentService;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.core.contants.CroCoreConstant;
import com.dchealth.medical.contants.TencentSmsAdminTemplateEnum;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.SendTencentSmsVO;
import com.dchealth.medical.domain.TencentSmsVO;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.mapper.ResearchInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * TencentService接口实现类
 */
@Service
@Slf4j
public class TencentServiceImpl implements TencentService {

    private static final String APP_ID = "1";

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
            appSetSearch.setAppId(Integer.valueOf(APP_ID));
            appSetSearch.setConfigKey(CroCoreConstant.TENCENT_SMS);
            List<AppSet> list = appSetMapper.selectAppSetList(appSetSearch);
            if (list != null && list.size() > 0) {
                AppSet  appSet  = list.get(0);
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

//    直接发内网不通
//    @Deprecated
//    public Boolean sendSms(PatientInfo patientInfoDO) {
//        log.info("这儿是短信发送, {}", patientInfoDO);
//        SendTencentSmsVO sendTencentSmsVO = getTencentSmsVOConfig();
//        sendTencentSmsVO.setPhones(new String[]{"+86" + patientInfoDO.getMobile()});
//        sendTencentSmsVO.setTemplateParam(new String[]{patientInfoDO.getPatientName()});
//        //发往xfapi中转服务器
//        String sendHttpPost = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"doctorAccount/sendJoinResearchSms",sendTencentSmsVO);
//        log.info("发送http请求结果:{}",sendHttpPost);
//        PatientInfo patientInfoUpdateDO = new PatientInfo();
//        patientInfoUpdateDO.setPatientId(patientInfoDO.getPatientId());
//        Integer smsSendCount = patientInfoDO.getSmsSendCount();
//        if (smsSendCount == null) {
//            smsSendCount = 0;
//        }
//        patientInfoUpdateDO.setSmsSendCount(smsSendCount + 1);
//        patientInfoMapper.updatePatientInfo(patientInfoUpdateDO);
//        return true;
//    }

    @Override
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
        accountNotice.setType("22");
        accountNotice.setCreateTime(new Date());
        accountNotice.setContent("短信通知");
        accountNotice.setTitle("短信通知");
//        accountNotice.setRecordId(0L);
        Map<String, Object> extjson = new HashMap<>();
        extjson.put("type", "22");
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

    public Boolean sendSms(String patientId) {
        PatientInfo patientInfoDO = patientInfoMapper.selectPatientInfoByPatientId(Long.valueOf(patientId));
        return asyncSendSms(patientInfoDO);
    }

    @Override
    public Boolean sendSmsLimitOne(String patientId) {
        PatientInfo patientInfoDO = patientInfoMapper.selectPatientInfoByPatientId(Long.valueOf(patientId));
        if (Objects.isNull(patientInfoDO.getSmsSendCount()) ||patientInfoDO.getSmsSendCount() < 1) {
            PatientInfo patientInfoUpdateDO = new PatientInfo();
            patientInfoUpdateDO.setPatientId(patientInfoDO.getPatientId());
            patientInfoUpdateDO.setSmsSendCount(1);
            patientInfoMapper.updatePatientInfo(patientInfoUpdateDO);
            return asyncSendSms(patientInfoDO);
        }else{
            throw new RuntimeException("短信发送次数已达到上限");
        }
    }

    @Override
    public Boolean sendSmsLimitOne(PatientInfo patientInfoDO) {
        if (Objects.isNull(patientInfoDO.getSmsSendCount()) ||patientInfoDO.getSmsSendCount() < 1) {
            PatientInfo patientInfoUpdateDO = new PatientInfo();
            patientInfoUpdateDO.setPatientId(patientInfoDO.getPatientId());
            patientInfoUpdateDO.setSmsSendCount(1);
            patientInfoMapper.updatePatientInfo(patientInfoUpdateDO);
            return asyncSendSms(patientInfoDO);
        }else{
            throw new RuntimeException("短信发送次数已达到上限");
        }
    }
}
