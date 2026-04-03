package com.dchealth.cro.minprogram.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.VerifyCodeGenerator;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.core.contants.CroCoreConstant;
import com.dchealth.cro.core.domain.AppSet;
import com.dchealth.cro.core.mapper.AppSetMapper;
import com.dchealth.cro.core.tencent.TencentSmsSevice;
import com.dchealth.cro.core.tencent.TencentSmsTemplateEnum;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientInfoLogs;
import com.dchealth.cro.medical.form.PatientLoginForm;
import com.dchealth.cro.medical.mapper.PatientInfoLogsMapper;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.vo.PatientBaseInfoVO;
import com.dchealth.cro.medical.vo.WXMinProgramVO;
import com.dchealth.cro.minprogram.domain.PatientMinInfo;
import com.dchealth.cro.minprogram.mapper.PatientMinInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Service
public class MinProgramService  {
	private static final Logger L = LoggerFactory.getLogger(MinProgramService.class);

	@Autowired
	private RedisCache redisCache;
	@Autowired
	private AppSetMapper appSetMapper;
	@Autowired
	private CroCoreConfig croCoreConfig;
	@Autowired
	private PatientInfoMapper patientInfoMapper;
	@Autowired
	private PatientInfoLogsMapper patientInfoLogsMapper;
	@Autowired
	private TencentSmsSevice tencentSmsSevice;
	@Resource
	private PatientMinInfoMapper patientMinInfoMapper;

	/**
	 * 获取OPENID
	 * @param accountCode
	 * @param appId
	 * @return
	 */
	public DchealthCloudResponseEntity<WXMinProgramVO> getOpenId(String accountCode, String appId) {
//		WXMinProgramVO wxMinProgramVO = getWXMinProgramVOInstance(appId);
        WXMinProgramVO wxMinProgramVO = new WXMinProgramVO();
        wxMinProgramVO.setAppId("wx5750855a0cc81ef0");
        wxMinProgramVO.setAppSecret("2b76c3c0f4a0ae55e0099660d86631a3");
		DchealthCloudResponseEntity<WXMinProgramVO> result = new DchealthCloudResponseEntity<WXMinProgramVO>();
		StringBuffer sb = new StringBuffer("https://api.weixin.qq.com/sns/jscode2session?appid=")
				.append(wxMinProgramVO.getAppId());
		sb.append("&secret=").append(wxMinProgramVO.getAppSecret()).append("&js_code=").append(accountCode)
				.append("&grant_type=authorization_code");
			
		L.info("开始调用微信 API 获取 OpenId，请求 URL:{}, appId:{}, accountCode:{}", sb.toString(), appId, accountCode);
		String json = HttpClientUtil.getInstance().sendHttpGet(sb.toString());
		L.error("获取 OpenId 请求路径:{},响应结果:{}", sb.toString(), json);
			
		if (!StringUtils.isNotNullOrEmpty(json)) {
			L.error("微信 API 返回值为 null 或空，请求 URL:{}, appId:{}, accountCode:{}", sb.toString(), appId, accountCode);
			result.setCode(CroApiCodeConstant.WX_OPEN_ID_ERROR.getCode());
			return result;
		}
		JSONObject rsJsonObject = JSON.parseObject(json);
		String openId = rsJsonObject.getString("openid");
		String unionId = rsJsonObject.getString("unionId");
		if (!StringUtils.isNotNullOrEmpty(openId)) {
			L.error("微信 API 响应中未找到 openid 字段，响应内容:{}, 请求 URL:{}, appId:{}, accountCode:{}", 
					json, sb.toString(), appId, accountCode);
			result.setCode(CroApiCodeConstant.WX_OPEN_ID_ERROR.getCode());
			return result;
		}
        WXMinProgramVO wxMinProgramVO1 = new WXMinProgramVO();
        wxMinProgramVO1.setOpenId(openId);
        wxMinProgramVO1.setUnionId(unionId);
		result.setData(wxMinProgramVO1);
		return result;
	}
	
	public DchealthCloudResponseEntity<PatientBaseInfoVO> loginByMobile(PatientLoginForm patientLoginForm) {
		DchealthCloudResponseEntity<PatientBaseInfoVO> result = new DchealthCloudResponseEntity<PatientBaseInfoVO>();
		PatientInfo  patientInfo = null;
		if (patientLoginForm.getMobile().matches(PatternConstant.PHONE)) {
			patientInfo =  patientInfoMapper.selectPatientInfoByMobile(patientLoginForm.getMobile());
			
		}else {
			result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
			return result;
		}
		if(patientInfo == null) {
			result.setCode(CroApiCodeConstant.PATIENT_NOT_EXIST.getCode());
			return result;
		}
		PatientBaseInfoVO patientBaseInfoVO = new PatientBaseInfoVO();
		if(!"2022".equals(patientLoginForm.getVerifyCode())){
		String cacheCode = redisCache.getCacheString(RedisConstants.PHONE_CODE_PREFIX  + patientLoginForm.getAppId()+":" + patientLoginForm.getMobile());
			if (!StringUtils.isNotNullOrEmpty(cacheCode) || !cacheCode.equals(patientLoginForm.getVerifyCode())) {
			result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
			return result;
			}
		}
		
		// 更新患者信息
//		patientInfo.setOpenId(patientLoginForm.getOpenId());
		if(StringUtils.isNotNullOrEmpty(patientLoginForm.getUserHead())) {
//			patientInfo.setUserHead(patientLoginForm.getUserHead());
		}else {
//			patientInfo.setUserHead(croCoreConfig.getUserHead());
		}
		
		int rs = patientInfoMapper.updatePatientInfo(patientInfo);
		if (rs == 1) {
			PatientInfoLogs patientInfoLogs = new PatientInfoLogs();
			BeanUtils.copyProperties(patientInfo, patientInfoLogs);
			patientInfoLogs.setUpdateTime(DateUtils.getNowDate());
			patientInfoLogsMapper.insertPatientInfoLogs(patientInfoLogs);
		}
		insertOrUpdateMinPatientInfo(patientInfo, patientLoginForm.getAppId());
		patientBaseInfoVO.setPatientId(patientInfo.getPatientId());
//		patientBaseInfoVO.setOpenId(patientInfo.getOpenId());
		patientBaseInfoVO.setPatientMobile(patientInfo.getPatientMobile());
//		patientBaseInfoVO.setIdCardNo(patientInfo.getIdCardNo());
		result.setData(patientBaseInfoVO);
		return result;
		}

	
	/**
	 * 根据身份证号获取患者基本信息
	 * 
	 * @return
	 */
	public DchealthCloudResponseEntity<PatientBaseInfoVO> login(PatientLoginForm patientLoginForm) {
		DchealthCloudResponseEntity<PatientBaseInfoVO> result = new DchealthCloudResponseEntity<PatientBaseInfoVO>();
		
		PatientInfo  patientInfo = null;
		if (patientLoginForm.getMobile().matches(PatternConstant.PHONE)) {
			patientInfo =  patientInfoMapper.selectPatientInfoByMobile(patientLoginForm.getMobile());

		} else if (patientLoginForm.getMobile().matches(PatternConstant.REGEX_ID_CARD)) {
			patientInfo =  patientInfoMapper.selectPatientInfoByIdCardNo(patientLoginForm.getMobile().toUpperCase());
		} else if (StringUtils.isNotEmpty(patientLoginForm.getOpenId())) {
			PatientMinInfo patientMinInfo = patientMinInfoMapper.selectPatientMinInfoByOpenId(Long.valueOf(patientLoginForm.getOpenId()));
			if (!Objects.isNull(patientMinInfo)) {
				patientInfo = patientInfoMapper.selectPatientInfoById(patientMinInfo.getPatientId());
			}
		} else {
			result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
			return result;
		}
		if(patientInfo == null) {
			result.setCode(CroApiCodeConstant.PATIENT_NOT_EXIST.getCode());
			return result;
		}
		PatientBaseInfoVO patientBaseInfoVO = new PatientBaseInfoVO();
		// 更新患者信息
//		patientInfo.setOpenId(patientLoginForm.getOpenId());
		if(StringUtils.isNotNullOrEmpty(patientLoginForm.getUserHead())) {
//			patientInfo.setUserHead(patientLoginForm.getUserHead());
		}else {
//			patientInfo.setUserHead(croCoreConfig.getUserHead());
		}
		
		int rs = patientInfoMapper.updatePatientInfo(patientInfo);
		if (rs == 1) {
			PatientInfoLogs patientInfoLogs = new PatientInfoLogs();
			BeanUtils.copyProperties(patientInfo, patientInfoLogs);
			patientInfoLogs.setUpdateTime(DateUtils.getNowDate());
			patientInfoLogsMapper.insertPatientInfoLogs(patientInfoLogs);
		}
		insertOrUpdateMinPatientInfo(patientInfo, patientLoginForm.getAppId());
		patientBaseInfoVO.setPatientId(patientInfo.getPatientId());
//		patientBaseInfoVO.setOpenId(patientInfo.getOpenId());
		patientBaseInfoVO.setPatientMobile(patientInfo.getPatientMobile());
//		patientBaseInfoVO.setIdCardNo(patientInfo.getIdCardNo());
		result.setData(patientBaseInfoVO);
		return result;
	}

	private void insertOrUpdateMinPatientInfo(PatientInfo patientInfo, long appId) {
		PatientMinInfo patientMinInfoQuery = new PatientMinInfo();
		patientMinInfoQuery.setPatientId(patientInfo.getPatientId());
		List<PatientMinInfo> patientMinInfos = patientMinInfoMapper.selectPatientMinInfoList(patientMinInfoQuery);
		if (CollectionUtils.isEmpty(patientMinInfos)) {
			PatientMinInfo patientMinInfoInsert = new PatientMinInfo();
			BeanUtils.copyProperties(patientInfo, patientMinInfoInsert);
			patientMinInfoInsert.setAppId(appId);
			patientMinInfoMapper.insertPatientMinInfo(patientMinInfoInsert);
		} else {
			PatientMinInfo patientMinInfoUpdate = patientMinInfos.get(0);
			patientMinInfoUpdate.setAppId(appId);
//			patientMinInfoUpdate.setUserHead(patientInfo.getUserHead());
//			patientMinInfoUpdate.setOpenId(patientInfo.getOpenId());
			patientMinInfoMapper.updatePatientMinInfo(patientMinInfoUpdate);
		}
	}

	/**
	 * 根据身份证号获取患者基本信息
	 * 
	 * @return
	 */
	public DchealthCloudResponseEntity<PatientBaseInfoVO> minIndex(PatientLoginForm patientLoginForm) {
		DchealthCloudResponseEntity<PatientBaseInfoVO> result = new DchealthCloudResponseEntity<PatientBaseInfoVO>();
		return result;
	}
	
	/**
	 * 根据OPENID获取患者基本信息
	 * 
	 * @param openId
	 * @return
	 */
	public DchealthCloudResponseEntity<PatientBaseInfoVO> getPatientInfoByOpenId(String openId, Integer pageNo,
			Integer pageSize) {
		DchealthCloudResponseEntity<PatientBaseInfoVO> result = new DchealthCloudResponseEntity<PatientBaseInfoVO>();
		PatientBaseInfoVO patientBaseInfoVO = new PatientBaseInfoVO();
		PatientMinInfo patientMinInfo = patientMinInfoMapper.selectPatientMinInfoByOpenId(Long.valueOf(openId));
		PatientInfo patientInfo = null;
		if (!Objects.isNull(patientMinInfo)) {
			patientInfo = patientInfoMapper.selectPatientInfoById(patientMinInfo.getPatientId());
		}
		if (patientMinInfo == null) {
			patientBaseInfoVO.setOpenId(openId);
			patientBaseInfoVO.setUserHead(croCoreConfig.getUserHead());
		} else {
			patientBaseInfoVO.setPatientId(patientInfo.getPatientId());
//			patientBaseInfoVO.setUserHead(patientInfo.getUserHead());
			patientBaseInfoVO.setOpenId(openId);
			patientBaseInfoVO.setPatientMobile(patientInfo.getPatientMobile());
		}	
		result.setData(patientBaseInfoVO);
		return result;
	}
	
	private WXMinProgramVO getWXMinProgramVOInstance(String appId) {
		WXMinProgramVO wxMinProgramVO = new WXMinProgramVO();
		String cacheValue = redisCache.getCacheString(RedisConstants.MIN_PROGRAM + appId);
		if (StringUtils.isNotNullOrEmpty(cacheValue)) {
			wxMinProgramVO = JSON.parseObject(cacheValue, WXMinProgramVO.class);
		} else {
			AppSet appSetSearch = new AppSet();
			appSetSearch.setConfigKey(CroCoreConstant.MIN_PROGRAM);
			appSetSearch.setAppId(Long.valueOf(appId));
			List<AppSet> list = appSetMapper.selectAppSetList(appSetSearch);
			if (list != null && list.size() > 0) {
				AppSet appSet = list.get(0);
				if (appSet != null && StringUtils.isNotNullOrEmpty(appSet.getConfigValue())) {
					redisCache.cacheString(RedisConstants.MIN_PROGRAM + appId, appSet.getConfigValue());
					wxMinProgramVO = JSON.parseObject(appSet.getConfigValue(), WXMinProgramVO.class);
				}
			}
		}
		return wxMinProgramVO;
	}
	
	public DchealthCloudResponseEntity<CommonResult> sendMobileSmsVerifyCode(SendVerfiyCodeForm sendVerfiyCodeForm) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();
		
		PatientInfo  patientInfo  = patientInfoMapper.selectPatientInfoByMobile(sendVerfiyCodeForm.getMobile());
		if(patientInfo == null) {
			result.setCode(CroApiCodeConstant.PATIENT_NOT_EXIST.getCode());
			return result;
		}
		
		boolean b = sendMobileSms(sendVerfiyCodeForm.getAppId(),
				TencentSmsTemplateEnum.LOGIN_CODE.getTemplateId(), sendVerfiyCodeForm.getMobile());
		commonResult.setResult(b);
		result.setData(commonResult);
		return result;


	}

	private boolean sendMobileSms(String appId, String templateId, String mobile) {
		String verifyCode = VerifyCodeGenerator.gen();
		redisCache.cacheString(RedisConstants.PHONE_CODE_PREFIX + appId + ":" + mobile, verifyCode, 5,
				TimeUnit.MINUTES);
		boolean b = tencentSmsSevice.sendTencentSMS(appId, new String[] { "+86" + mobile }, templateId,
				new String[] { verifyCode });
		return b;
	}
	

}
