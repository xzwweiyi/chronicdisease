package com.dchealth.cro.account.service;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.constant.AccountContants;
import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.domain.UserImages;
import com.dchealth.cro.account.domain.UserInfo;
import com.dchealth.cro.account.domain.UserLoginLogs;
import com.dchealth.cro.account.form.ForgetPasswordForm;
import com.dchealth.cro.account.form.UserUpdateOldPasswordForm;
import com.dchealth.cro.account.form.VerfiyCodeForm;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.account.mapper.UserImagesMapper;
import com.dchealth.cro.account.mapper.UserInfoMapper;
import com.dchealth.cro.account.mapper.UserLoginLogsMapper;
import com.dchealth.cro.account.vo.DoctorDetailVO;
import com.dchealth.cro.account.vo.UserCenterVO;
import com.dchealth.cro.account.vo.UserInfoVO;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.cache.RedisCache;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.constant.PatternConstant;
import com.dchealth.cro.common.constant.RedisConstants;
import com.dchealth.cro.common.crypto.SMKeyPair;
import com.dchealth.cro.common.crypto.SecurityUtil;
import com.dchealth.cro.common.crypto.Sm2Utils;
import com.dchealth.cro.common.crypto.Sm4Util;
import com.dchealth.cro.common.enums.AccountType;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.contants.DictConstant;
import com.dchealth.cro.core.domain.SysDept;
import com.dchealth.cro.core.mapper.SysDeptMapper;
import com.dchealth.cro.core.service.SysDictDataService;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class AccountService {
	private static final Logger L = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private SysUserMapper accountMapper;
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private UserInfoMapper accountInfoMapper;
	@Autowired
	private UserImagesMapper accountImagesMapper;

	@Autowired
	private SysDictDataService sysDictDataService;
	@Autowired
	private UserLoginLogsMapper userLoginLogsMapper;
	@Autowired
	private SysDeptMapper sysDeptMapper;

	/**
	 * 
	 * @param accountId
	 * @return
	 */
	public DchealthCloudResponseEntity<UserInfoVO> detail(Long userId) {
		DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
		SysUser accountDB = accountMapper.selectSysUserByUserId(userId);
		if (accountDB == null) {
			result.setCode(CroApiCodeConstant.USER_ERROR.getCode());
			return result;
		}
		UserInfoVO userInfoVO = new UserInfoVO();
		UserInfo accountInfo = accountInfoMapper.selectUserInfoByUserId(userId);
		// 非患者处理
		if (accountInfo == null) {
			result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
			return result;
		}

		result.setData(userInfoVO);
		return result;
	}


	public DchealthCloudResponseEntity<UserCenterVO> index(Long accountId) {
		DchealthCloudResponseEntity<UserCenterVO> result = new DchealthCloudResponseEntity<UserCenterVO>();
		SysUser accountDB = accountMapper.selectSysUserByUserId(accountId);
		if (accountDB == null) {
			result.setCode(CroApiCodeConstant.USER_ERROR.getCode());
			return result;
		}
		UserCenterVO userInfoVO = new UserCenterVO();
		UserInfo accountInfo = accountInfoMapper.selectUserInfoByUserId(accountId);
		if (accountInfo == null) {
			result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
			return result;
		}

		

		result.setData(userInfoVO);
		return result;
	}

	/**
	 * 手机号和密码登录
	 * 
	 * @param aesloginName
	 * @param aespassword
	 * @param appType
	 * @param deviceToken
	 * @param tenantId
	 * @return
	 */
	public DchealthCloudResponseEntity<UserInfoVO> loginBySysUser(String aesloginName, String aespassword,
			String appType, String deviceToken, String tenantId) {
		DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
		String loginName = "";
		String password = "";
		try {
			loginName = Sm4Util.decryptEcb(Constants.SM4_KEY, aesloginName);
			password = Sm4Util.decryptEcb(Constants.SM4_KEY, aespassword);
		} catch (Exception e) {
			L.error("登录账号:{},登录密码:{}", aesloginName, aespassword);
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
		SysUser accountDB = accountMapper.selectSysUserByLoginName(loginName);
		if (accountDB == null) {
			result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
			return result;

		}
		
		if (!SecurityUtil.matchesPassword(password, accountDB.getPassword())) {
			result.setCode(CroApiCodeConstant.USER_ERROR.getCode());
			return result;
		}
		return dealLogin(accountDB, deviceToken, appType,tenantId);
	}

	/**
	 * 手机登录
	 * 
	 * @param aesloginName
	 * @param verifyCode
	 * @param appType
	 * @param deviceToken
	 * @param tenantId
	 * @return
	 */
	public DchealthCloudResponseEntity<UserInfoVO> loginByMobileSms(String aesloginName, String verifyCode,
			String appType, String deviceToken, String tenantId) {
		DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
		String loginName = "";
		try {
			loginName = Sm4Util.decryptEcb(Constants.SM4_KEY, aesloginName);
		} catch (Exception e) {
			L.error("登录账号:{} 验证码：{}", aesloginName, verifyCode);
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
		String cacheCode = redisCache.getCacheString(RedisConstants.PHONE_CODE_PREFIX + tenantId + ":" + loginName);
		if (!StringUtils.isNotNullOrEmpty(cacheCode) || !cacheCode.equals(verifyCode)) {
			result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
			return result;
		}
		SysUser accountDB = accountMapper.selectSysUserByLoginName(loginName);
		if (accountDB == null) {
			result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
			return result;
		}
		return dealLogin(accountDB, deviceToken, appType, tenantId);
	}

	/**
	 * 查询用户信息
	 * 
	 * @param aesMobile
	 * @return
	 */
	@Transactional
	public DchealthCloudResponseEntity<UserInfoVO> findUserInfoByMobile(String aesMobile, String appType,
			String deviceToken, String channel) {
		DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
		String mobile = "";
		try {
			mobile = Sm4Util.decryptEcb(Constants.SM4_KEY,aesMobile); 
		} catch (Exception e) {
			L.error("手机号有误:{},渠道:{}", aesMobile, channel);
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
		if (mobile.matches(PatternConstant.PHONE)) {
			SysUser accountDB = accountMapper.selectSysUserByMobile(mobile);
			UserInfoVO userInfoVO = new UserInfoVO();
			if (accountDB == null) {
				result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
				return result;
			} else {
				// 账号停用处理
				if ("1".equals(accountDB.getStatus())) {
					result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
					return result;
				}
				UserInfo accountInfo = accountInfoMapper.selectUserInfoByUserId(accountDB.getUserId());
				if (accountInfo == null) {
					result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
					return result;
				}
				userInfoVO = getBaseUserInfo(accountDB, accountInfo);
				insertSysUserLoginLogs(accountDB.getUserId());
			}
			SMKeyPair smKeyPair = Sm2Utils.genKeyPair(false);
			userInfoVO.setPrivateKey(smKeyPair.getPriKey());
			L.info("登录生成公私钥:{}", JSON.toJSONString(smKeyPair));
			redisCache.cacheString(RedisConstants.TOKEN_PREFIX + channel + ":" + deviceToken,
					smKeyPair.getPubKey(), 7, TimeUnit.DAYS);
			//获取科室信息
			SysDept sysDept = sysDeptMapper.selectSysDeptByDeptId(accountDB.getDeptId());
			userInfoVO.setDeptName(sysDept.getDeptName());
			result.setData(userInfoVO);
			return result;
		} else {
			result.setCode(CroApiCodeConstant.PHONE_ERROR.getCode());
			return result;
		}

	}

	public DchealthCloudResponseEntity<DoctorDetailVO> detailDoctor(Long doctorId, String positionType,
			String channel) {
		DchealthCloudResponseEntity<DoctorDetailVO> result = new DchealthCloudResponseEntity<DoctorDetailVO>();
		SysUser accountDB = accountMapper.selectSysUserByUserId(doctorId);
		if (accountDB == null) {
			result.setCode(CroApiCodeConstant.DOCTOR_NOT_EXIST.getCode());
			return result;
		}
		UserInfo accountInfo = accountInfoMapper.selectUserInfoByUserId(doctorId);
		if (accountInfo == null ) {
			result.setCode(CroApiCodeConstant.DOCTOR_NOT_EXIST.getCode());
			return result;
		}
		DoctorDetailVO doctorDetailVO = new DoctorDetailVO();
		doctorDetailVO.setDoctorId(doctorId);
		doctorDetailVO
				.setEducation(sysDictDataService.getDictValue(DictConstant.EDUCATION, accountInfo.getEducation()));
		doctorDetailVO.setExpert(accountInfo.getExpert());
		doctorDetailVO.setFigureImage(accountInfo.getFigureImage());
		doctorDetailVO.setPersonalIntro(accountInfo.getPersonalIntro());
		doctorDetailVO.setTitle(sysDictDataService.getDictValue(DictConstant.TITLE, accountInfo.getTitle()));
		doctorDetailVO.setWorkYear(calWorkYear(accountInfo.getWorkYear()));
		result.setData(doctorDetailVO);
		return result;
	}

	

	/**
	 * 校验验证码
	 * 
	 * @param account
	 *            手机号或邮箱
	 * @param verifyCode
	 *            验证码
	 * @return
	 */
	public DchealthCloudResponseEntity<CommonResult> checkVerifyCode(VerfiyCodeForm verfiyCodeForm) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();
		try {
			String cacheCode = redisCache.getCacheString(
					RedisConstants.PHONE_CODE_PREFIX + verfiyCodeForm.getTenantId() + ":" + verfiyCodeForm.getMobile());
			if (StringUtils.isNotNullOrEmpty(cacheCode) && cacheCode.equals(verfiyCodeForm.getVerifyCode())) {
				commonResult.setResult(true);
				result.setData(commonResult);
				return result;
			} else {
				result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
				return result;
			}
		} catch (Exception e) {
			L.error("验证码校验异常请求参数 账号:{},验证码:{}", verfiyCodeForm.getMobile(), verfiyCodeForm.getVerifyCode());
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
	}

	/**
	 * 判断手机号是否存在
	 * 
	 * @param accountName
	 *            账号
	 * @param type
	 *            1 手机 2 邮箱
	 * @return
	 */
	public DchealthCloudResponseEntity<CommonResult> existSysUser(String accountName, String type) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();
		// 从数据库获取
		SysUser account = null;
		if (AccountType.PHONE.getCode().equals(type)) {
			account = accountMapper.selectSysUserByMobile(accountName);
		}
		if (AccountType.EMAIL.getCode().equals(type)) {
			account = accountMapper.selectSysUserByEmail(accountName);
		}

		if (account != null) {
			commonResult.setResult(true);
			result.setData(commonResult);
			return result;
		} else {
			commonResult.setResult(false);
			result.setData(commonResult);
			return result;
		}
	}

	public DchealthCloudResponseEntity<CommonResult> checkSysUser(String phone) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		SysUser account4 = accountMapper.selectSysUserByMobile(phone);
		if (account4 != null) {
			if ("1".equals(account4.getStatus())) {
				result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
				return result;
			}
			return result;
		} else {
			result.setCode(CroApiCodeConstant.PHONE_NOT_EXIST.getCode());
			return result;
		}
	}

	
	/**
	 * 查询用户信息
	 * 
	 * @param accountId
	 * @return
	 */
	public DchealthCloudResponseEntity<UserInfoVO> findUserInfoBySysUserId(Long accountId) {
		DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
		try {
			SysUser account = accountMapper.selectSysUserByUserId(accountId);
			if (account == null) {
				result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
				return result;
			}
			UserInfoVO userInfoVO = new UserInfoVO();
			userInfoVO.setMobile(account.getPhonenumber());
			userInfoVO.setSex(String.valueOf(account.getSex()));
			result.setData(userInfoVO);
			return result;
		} catch (Exception e) {
			L.error("查询用户异常{}", accountId, e);
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
	}

	/**
	 * 根据旧密码进行修改登录密码
	 * 
	 * @param accountId
	 * @param loginPasswd
	 * @param oldLoginPasswd
	 * @return
	 */

	public DchealthCloudResponseEntity<CommonResult> updatePasswordByOldPassword(
			UserUpdateOldPasswordForm userUpdateOldPasswordForm) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();
		try {
			SysUser account = null;

			if (userUpdateOldPasswordForm.getAccountId() != null) {
				account = accountMapper.selectSysUserByUserId(userUpdateOldPasswordForm.getAccountId());
			}
			if (userUpdateOldPasswordForm.getAccountId() == null
					&& StringUtils.isNotNullOrEmpty(userUpdateOldPasswordForm.getPhone())) {
				String loginName = "";
				try {
					loginName =Sm4Util.decryptEcb(Constants.SM4_KEY, userUpdateOldPasswordForm.getPhone());
					account = accountMapper.selectSysUserByMobile(loginName);
				} catch (UnsupportedEncodingException e) {
					result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
					return result;
				}
			}
			if (account == null) {
				result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
				return result;
			}

			if (!StringUtils.isNotNullOrEmpty(userUpdateOldPasswordForm.getOldPassword())
					&& !StringUtils.isNotNullOrEmpty(userUpdateOldPasswordForm.getVerifyCode())) {
				result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
				return result;
			}

			if (StringUtils.isNotEmpty(userUpdateOldPasswordForm.getOldPassword())
					&& StringUtils.isEmpty(userUpdateOldPasswordForm.getVerifyCode())) {
				// 密码不对
				String pwd = SecurityUtil.encryptPassword(userUpdateOldPasswordForm.getPassword());
				if (!pwd.equals(account.getPassword())) {
					result.setCode(CroApiCodeConstant.OLD_PASSWORD_ERROR.getCode());
					return result;
				}
			}
			if (StringUtils.isNotEmpty(userUpdateOldPasswordForm.getVerifyCode())
					&& StringUtils.isEmpty(userUpdateOldPasswordForm.getOldPassword())) {
				if (!StringUtils.isNotNullOrEmpty(account.getPhonenumber())) {
					result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
					return result;
				}
				String cacheCode = redisCache.getCacheString(
						RedisConstants.PHONE_CODE_PREFIX + account.getPhonenumber());
				if (!StringUtils.isNotNullOrEmpty(cacheCode)
						|| !cacheCode.equals(userUpdateOldPasswordForm.getVerifyCode())) {
					result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
					return result;
				}
			}

			// 生成加密后的登陆密码
			String pwd = SecurityUtil.encryptPassword(userUpdateOldPasswordForm.getPassword());
			account.setPassword(pwd);
			int rs = accountMapper.updateSysUser(account);
			if (rs > 0) {
				commonResult.setResult(true);
				result.setData(commonResult);
				return result;
			} else {
				commonResult.setResult(false);
				result.setData(commonResult);
				return result;
			}
		} catch (Exception e) {
			L.info("根据旧密码修改密码异常", e.getMessage());
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
	}

	/**
	 * 根据手机号或邮箱进行登录密码修改
	 * 
	 * @param userSysUserPasswordForm
	 * @return
	 */
	public DchealthCloudResponseEntity<CommonResult> updatePasswordBySysUser(ForgetPasswordForm forgetPasswordForm) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();

		try {
			// 判断账号是否存在
			SysUser account = null;
			if (forgetPasswordForm.getAccount().matches(PatternConstant.PHONE)) {
				account = accountMapper.selectSysUserByMobile(forgetPasswordForm.getAccount());
			}
			if (forgetPasswordForm.getAccount().matches(PatternConstant.EMAIL)) {
				account = accountMapper.selectSysUserByEmail(forgetPasswordForm.getAccount());
			}

			if (account == null) {
				result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
				return result;
			}
			String pwd = SecurityUtil.encryptPassword(forgetPasswordForm.getPassword());
			// 生成加密后的登陆密码
			account.setPassword(pwd);
			int rs = accountMapper.updateSysUser(account);
			if (rs > 0) {
				commonResult.setResult(true);
				result.setData(commonResult);
				return result;
			} else {
				commonResult.setResult(false);
				result.setData(commonResult);
				return result;
			}
		} catch (Exception e) {
			L.info("忘记密码修改异常", e.getMessage());
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
	}

	/**
	 * 校验验证码
	 * 
	 * @param account
	 *            邮箱或手机号
	 * @param code
	 * @return
	 */
	public DchealthCloudResponseEntity<CommonResult> verifyPhone(String account, String code) {
		DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
		CommonResult commonResult = new CommonResult();
		try {
			String cacheCode = redisCache.getCacheString(RedisConstants.PHONE_CODE_PREFIX + account);
			if (StringUtils.isNotNullOrEmpty(cacheCode) && cacheCode.equals(code)) {
				commonResult.setResult(true);
				result.setData(commonResult);
				return result;
			} else {
				result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
				return result;
			}
		} catch (Exception e) {
			L.info("校验验证码异常", e.getMessage());
			result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
			return result;
		}
	}

	/**
	 * 设置用户字典信息
	 * 
	 * @param userInfoVO
	 * @param tenantId
	 * @return
	 */
	private UserInfoVO setDictDatas(UserInfoVO userInfoVO) {
		userInfoVO.setEducationDict(sysDictDataService.getDictVO(DictConstant.EDUCATION));
		userInfoVO.setSexDict(sysDictDataService.getDictVO(DictConstant.SYS_USER_SEX));
		userInfoVO.setTitleDict(sysDictDataService.getDictVO(DictConstant.TITLE));
		return userInfoVO;
	}

	private UserInfoVO getBaseUserInfo(SysUser accountDB, UserInfo accountInfo) {
		UserInfoVO userInfoVO = new UserInfoVO();
		userInfoVO.setMobile(accountDB.getPhonenumber());
		userInfoVO.setSex(String.valueOf(accountDB.getSex()));
		userInfoVO.setUserHead(accountDB.getAvatar());
		userInfoVO.setNickName(accountDB.getNickName());
		userInfoVO.setEmail(accountDB.getEmail());
		userInfoVO.setAccountId(accountDB.getUserId());
		userInfoVO.setUserName(accountDB.getUserName());
		userInfoVO.setTitle(accountInfo.getTitle());
		userInfoVO.setWorkYear(accountInfo.getWorkYear());
		userInfoVO.setCalWorkYear(calWorkYear(accountInfo.getWorkYear()));
		userInfoVO.setExpert(accountInfo.getExpert());
		userInfoVO.setPersonalIntro(accountInfo.getPersonalIntro());
		userInfoVO.setFigureImage(accountInfo.getFigureImage());
		userInfoVO.setHonorImages(getSysUserImages(AccountContants.DOCTOR_IMAGE_HONOR, accountDB.getUserId()));
		userInfoVO.setEducation(accountInfo.getEducation());
		userInfoVO.setAuthStatus(String.valueOf(accountInfo.getAuthStatus()));
		userInfoVO = setDictDatas(userInfoVO);
		return userInfoVO;
	}



	
	/**
	 * 获取用户图片信息
	 * 
	 * @param type
	 *            1 荣誉图片
	 * @param accountId
	 *            账号ID
	 * @return
	 */
	private List<String> getSysUserImages(String type, Long accountId) {
		List<String> result = new ArrayList<String>();
		UserImages searcehSysUserImages = new UserImages();
		searcehSysUserImages.setType(type);
		searcehSysUserImages.setUserId(accountId);
		List<UserImages> list = accountImagesMapper.selectUserImagesList(searcehSysUserImages);
		for (UserImages accountImages : list) {
			result.add(accountImages.getImageUrl());
		}
		return result;
	}

	private String calWorkYear(String workYear) {
		if (StringUtils.isNotNullOrEmpty(workYear) && NumberUtils.isCreatable(workYear)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(DateUtils.getNowDate());
			int year = calendar.get(Calendar.YEAR);
			int result = year - NumberUtils.toInt(workYear);
			if (result >= 0 && result < 100) {
				return String.valueOf(result);
			}
		}
		return "-";
	}

	/**
	 * 更新用户登录日志
	 * 
	 * @param accountId
	 */
	private void insertSysUserLoginLogs(Long accountId) {
		SysUser accountInfo = new SysUser();
		accountInfo.setLoginDate(DateUtils.getNowDate());
		accountInfo.setUserId(accountId);
		accountMapper.updateSysUser(accountInfo);
		insertSysUserLogs(accountId, "用户登录");
	}
	
	// 插入日志信息
	private int insertSysUserLogs(Long accountId, String eventName) {
		UserLoginLogs accountLogs = new UserLoginLogs();
		accountLogs.setUserId(accountId);
		accountLogs.setCreateTime(DateUtils.getNowDate());
		accountLogs.setEventName(eventName);
		return userLoginLogsMapper.insertUserLoginLogs(accountLogs);
	}


	/**
	 * 统一处理用户登录
	 * 
	 * @param accountDB
	 * @param deviceToken
	 * @return
	 */
	private DchealthCloudResponseEntity<UserInfoVO> dealLogin(SysUser accountDB, String deviceToken, String appType,String channel) {
		DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
		UserInfoVO userInfoVO = new UserInfoVO();
		// 账号停用处理
		if ("1".equals(accountDB.getStatus())) {
			result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
			return result;
		}
		UserInfo accountInfo = accountInfoMapper.selectUserInfoByUserId(accountDB.getUserId());
			if (accountInfo == null ) {
				result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
				return result;
			}
			userInfoVO = getBaseUserInfo(accountDB, accountInfo);
			insertSysUserLoginLogs(accountDB.getUserId());
			SMKeyPair smKeyPair = Sm2Utils.genKeyPair(false);
			userInfoVO.setPrivateKey(smKeyPair.getPriKey());
			L.info("登录生成公私钥:{}", JSON.toJSONString(smKeyPair));
			redisCache.cacheString(RedisConstants.TOKEN_PREFIX + channel + ":" + deviceToken,
					smKeyPair.getPubKey(), 7, TimeUnit.DAYS);
			//获取科室信息
		SysDept sysDept = sysDeptMapper.selectSysDeptByDeptId(accountDB.getDeptId());
		userInfoVO.setDeptName(sysDept.getDeptName());
		result.setData(userInfoVO);
		return result;
	}


}
