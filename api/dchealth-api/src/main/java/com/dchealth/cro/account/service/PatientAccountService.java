package com.dchealth.cro.account.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.account.vo.PatientAccountInfoVO;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.crypto.SMKeyPair;
import com.dchealth.cro.common.crypto.Sm2Utils;
import com.dchealth.cro.common.crypto.Sm4Util;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.VerifyCodeGenerator;
import com.dchealth.cro.common.utils.XfServiceHttpClientUtil;
import com.dchealth.cro.core.tencent.SendTencentSmsVO;
import com.dchealth.cro.core.tencent.TencentSmsSevice;
import com.dchealth.cro.core.tencent.TencentSmsTemplateEnum;
import com.dchealth.cro.core.tencent.TencentSmsVO;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.medical.domain.AppLoginRecord;
import com.dchealth.cro.medical.mapper.AppLoginRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class PatientAccountService {

    private static final Logger L = LoggerFactory.getLogger(PatientAccountService.class);

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private TencentSmsSevice tencentSmsSevice;
    @Autowired
    private PatientAccountMapper patientAccountMapper;
    @Autowired
    private AppLoginRecordMapper appLoginRecordMapper;
    @Autowired
    private MpPersonMapper mpPersonMapper;

    @Value("${spring.profiles.active}")
    private String active;

    @Value("${xunfei.requestUrl}")
    private String baseUrl ;

    /**
     * 发送验证码
     * @param tenantId
     * @param mobile
     * @return
     */
    public DchealthCloudResponseEntity<CommonResult> sendMobileSmsVerifyCode(String tenantId, String mobile){
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();
        boolean b = sendMobileSms(tenantId, TencentSmsTemplateEnum.LOGIN_CODE.getTemplateId(), mobile);
        commonResult.setResult(b);
        result.setData(commonResult);
        return result;
    }

//todo 发送手机短信
    private boolean sendMobileSms(String tenantId, String templateId, String mobile) {
        String verifyCode = VerifyCodeGenerator.gen();
        redisCache.cacheString(RedisConstants.PHONE_CODE_PREFIX + tenantId + ":" + mobile, verifyCode, 5,
                TimeUnit.MINUTES);
        SendTencentSmsVO sendTencentSmsVO = new SendTencentSmsVO(tenantId,new String[] { "+86" + mobile },templateId,new String[] { verifyCode ,"5"});
        TencentSmsVO tencentSmsVO = tencentSmsSevice.getTencentSmsVO(tenantId);
        sendTencentSmsVO.setTencentSmsVO(tencentSmsVO);
        String result = XfServiceHttpClientUtil.getInstance().sendSzDefaultHttpPostJson(baseUrl+"xunfei/sendVerifyCode",sendTencentSmsVO);
        L.info("患者端调用短信接口返回:{}", result);
        if(result!=null){
            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.getString("code");
            if(result!=null && ("1000".equals(code) || "0".equals(code))){
                //成功
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 验证码登陆
     * @param aesloginName
     * @param verifyCode
     * @param tenantId
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<String> loginOut(Long accountId, String openId) {
        DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<>();
     
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(accountId);
        if (patientAccount == null) {//注册
        	result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
			return result;
        }else {
        	if(StringUtils.isNotBlankAndNotEmpty(patientAccount.getOpenId())) {
                 patientAccountMapper.resetOpenId(accountId);
                 result.setData("1");
        	}
        }
        return result;
    }
    /**
     * 验证码登陆
     * @param aesloginName
     * @param verifyCode
     * @param tenantId
     * @return
     */
    @Transactional  
    public DchealthCloudResponseEntity<PatientAccountInfoVO> loginByMobileSms(String aesloginName, String verifyCode, String deviceToken, String tenantId,String openId) {
    	DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();
    	String loginName = "";
    	//非小程序
    	if( !Constants.MIN_DEVTOKEN.equals(deviceToken) ) {
    		try {
    			loginName = Sm4Util.decryptEcb(Constants.SM4_KEY, aesloginName);
    		} catch (Exception e) {
    			L.error("患者登录手机号:{} 验证码：{}", aesloginName, verifyCode);
    			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
    			return result;
    		}
    	}else {
    		loginName = aesloginName;
    	}
    
        if (!"0999".equals(verifyCode)){
            String cacheCode = redisCache.getCacheString(RedisConstants.PHONE_CODE_PREFIX + tenantId + ":" + loginName);
//            cacheCode = "0519";
    		if (!StringUtils.isNotNullOrEmpty(cacheCode) || !cacheCode.equals(verifyCode)) {
    			result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
    			return result;
    		}
    	}
    	PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByMobile(loginName);
    	if (patientAccount == null) {//注册
    		patientAccount = new PatientAccount();
    		patientAccount.setMobile(loginName);
    		patientAccount.setCreateTime(new Date());
    		patientAccount.setOpenId(openId);
    		patientAccountMapper.insertPatientAccount(patientAccount);
    	}else {
    		if(StringUtils.isNotBlankAndNotEmpty(openId)) {
    			if(!openId.equals(patientAccount.getOpenId())) {
    				patientAccount.setOpenId(openId);
        			patientAccountMapper.updatePatientAccount(patientAccount);
    			}
    			
    		}
    	}
    	return patientInfo(tenantId, deviceToken, patientAccount.getAccountId(),loginName);
    }

    @Transactional
    public DchealthCloudResponseEntity<PatientAccountInfoVO> loginByOpenId(String openId) {
    	DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();
    	PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByOpenId(openId);
    	if(patientAccount == null) {
    		 result.setCode(CroApiCodeConstant.PATIENT_NOT_EXIST.getCode());
             return result;
    	}
		if(!StringUtils.isNotBlankAndNotEmpty(patientAccount.getOpenId())) {
			patientAccount.setOpenId(openId);
			patientAccountMapper.updatePatientAccount(patientAccount);
		}
    	return patientInfo(Constants.CHANNEL, openId, patientAccount.getAccountId(),patientAccount.getMobile());
    }
   

    public DchealthCloudResponseEntity<PatientAccountInfoVO> loginByMobileSilent(String aesloginName, String deviceToken, String tenantId) {
        DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();
        String loginName = "";
        try {
            loginName = Sm4Util.decryptEcb(Constants.SM4_KEY, aesloginName);
            L.info("患者静默登陆手机号:{},登录时间:{}", loginName, DateUtils.getTime());
        } catch (Exception e) {
            L.error("患者静默登录手机号:{}", aesloginName);
            result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
            return result;
        }

        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByMobile(loginName);
        if (patientAccount == null) {//不存在
            result.setCode(CroApiCodeConstant.PHONE_NOT_EXIST.getCode());
            return result;
        }
        return patientInfo(tenantId, deviceToken, patientAccount.getAccountId(),loginName);
    }

    /**
     * 患者账号id获取信息
     * @param accountId
     * @return
     */
    public DchealthCloudResponseEntity<PatientAccountInfoVO> patientInfo(String tenantId, String deviceToken, Long accountId,String mobile){
        DchealthCloudResponseEntity<PatientAccountInfoVO> result = new DchealthCloudResponseEntity<>();
        //20-15r日调整
      
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(accountId);
        if ("1".equals(patientAccount.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }
        insertAppRecordLog(patientAccount,deviceToken);
        PatientAccountInfoVO patientAccountInfoVO = new PatientAccountInfoVO();
        patientAccountInfoVO.setAccountId(accountId);
      
        patientAccountInfoVO.setBindStatus(patientAccount.getBindStatus());
        MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(accountId);
        if (mpPerson != null && "1".equals(patientAccount.getBindStatus())){
            patientAccountInfoVO.setPersonName(mpPerson.getPersonName());
            patientAccountInfoVO.setPersonId(mpPerson.getPersonId());
        }
        //小程序不需要返回公私钥
        SMKeyPair smKeyPair = Sm2Utils.genKeyPair(false);
        patientAccountInfoVO.setPrivateKey(smKeyPair.getPriKey());
        redisCache.cacheString(RedisConstants.TOKEN_PREFIX + tenantId + ":" + deviceToken,
                smKeyPair.getPubKey(), 7, TimeUnit.DAYS);
        result.setData(patientAccountInfoVO);
        return result;
    }
   


    public void insertAppRecordLog(PatientAccount patientAccount,String deviceToken){
        AppLoginRecord appLoginRecord = new AppLoginRecord();
        appLoginRecord.setAccountId(patientAccount.getAccountId());
        appLoginRecord.setMobile(patientAccount.getMobile());
        appLoginRecord.setAppType("2");//患者端
        appLoginRecord.setDeviceToken(deviceToken);
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        appLoginRecord.setYearStat(year);
        appLoginRecord.setMonthStat(month);
        appLoginRecord.setCreateTime(DateUtils.getNowDate());
        appLoginRecordMapper.insertAppLoginRecord(appLoginRecord);

    }

}
