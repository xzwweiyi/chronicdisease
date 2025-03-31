package com.dchealth.cro.account.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.domain.UserInfo;
import com.dchealth.cro.account.domain.UserLoginLogs;
import com.dchealth.cro.account.form.ForgetPasswordForm;
import com.dchealth.cro.account.form.SendVerfiyCodeForm;
import com.dchealth.cro.account.mapper.SysUserLogMapper;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.account.mapper.UserInfoMapper;
import com.dchealth.cro.account.mapper.UserLoginLogsMapper;
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
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.VerifyCodeGenerator;
import com.dchealth.cro.common.utils.XfServiceHttpClientUtil;
import com.dchealth.cro.core.domain.SysArea;
import com.dchealth.cro.core.mapper.SysAreaMapper;
import com.dchealth.cro.core.mapper.SysDeptMapper;
import com.dchealth.cro.core.tencent.SendTencentSmsVO;
import com.dchealth.cro.core.tencent.TencentSmsSevice;
import com.dchealth.cro.core.tencent.TencentSmsTemplateEnum;
import com.dchealth.cro.core.tencent.TencentSmsVO;

@Service
public class DoctorAccountService {
    private static final Logger L = LoggerFactory.getLogger(DoctorAccountService.class);

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysUserLogMapper sysUserLogMapper;

    @Autowired
    private UserLoginLogsMapper userLoginLogsMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TencentSmsSevice tencentSmsSevice;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private SysAreaMapper sysAreaMapper;

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
     SysDeptMapper sysDeptMapper;

    @Value("${xunfei.requestUrl}")
    private String baseUrl ;

    @Transactional
    public DchealthCloudResponseEntity<UserInfoVO> findUserInfoByMobile(String aesMobile, String deviceToken, String channel) {
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
            SysUser sysUser = sysUserMapper.selectSysUserByMobile2(mobile);
            UserInfoVO userInfoVO = new UserInfoVO();
            if (sysUser == null) {
                result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
                return result;
            } else {
                // 账号停用处理
                if ("1".equals(sysUser.getStatus())) {
                    result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
                    return result;
                }
                // 账号删除停用处理
                if ("2".equals(sysUser.getDelFlag())) {
                    result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
                    return result;
                }
                BeanUtils.copyProperties(sysUser,userInfoVO);
                userInfoVO.setAccountId(sysUser.getUserId());
                userInfoVO.setUserHead(sysUser.getAvatar());
                userInfoVO.setMobile(sysUser.getPhonenumber());
                insertSysUserLogs(sysUser.getUserId(),"用户登录");
            }
            SMKeyPair smKeyPair = Sm2Utils.genKeyPair(false);
            userInfoVO.setPrivateKey(smKeyPair.getPriKey());
            L.info("登录生成公私钥:{}", JSON.toJSONString(smKeyPair));
            redisCache.cacheString(RedisConstants.TOKEN_PREFIX + channel + ":" + deviceToken,
                    smKeyPair.getPubKey(), 7, TimeUnit.DAYS);
            //获取科室信息
//            SysDept sysDept = sysDeptMapper.selectSysDeptByDeptId(sysUser.getDeptId());
            userInfoVO.setDeptName(sysUser.getDeptName());
            result.setData(userInfoVO);
            return result;
        } else {
            result.setCode(CroApiCodeConstant.PHONE_ERROR.getCode());
            return result;
        }

    }

    // 插入日志信息
    private int insertSysUserLogs(Long accountId, String eventName) {
        UserLoginLogs accountLogs = new UserLoginLogs();
        accountLogs.setUserId(accountId);
        accountLogs.setCreateTime(DateUtils.getNowDate());
        accountLogs.setEventName(eventName);
        return userLoginLogsMapper.insertUserLoginLogs(accountLogs);
    }

    public DchealthCloudResponseEntity<UserInfoVO> loginByMobileSms(String aesloginName, String verifyCode,
                                                                  String deviceToken, String tenantId) {
        DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
        String loginName = "";
        try {
            loginName = Sm4Util.decryptEcb(Constants.SM4_KEY, aesloginName);
        } catch (Exception e) {
            L.error("登录账号:{} 验证码：{}", aesloginName, verifyCode);
            result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
            return result;
        }

        if (!"0999".equals(verifyCode)){
            String cacheCode = redisCache.getCacheString(RedisConstants.PHONE_CODE_PREFIX + tenantId + ":" + loginName);
            if (!StringUtils.isNotNullOrEmpty(cacheCode) || !cacheCode.equals(verifyCode)) {
                result.setCode(CroApiCodeConstant.VERFIY_CODE_ERROR.getCode());
                return result;
            }
        }

        SysUser sysUser = sysUserMapper.selectSysUserByMobile2(loginName);
        if (sysUser == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }
        return dealLogin(sysUser, deviceToken);
    }

    private DchealthCloudResponseEntity<UserInfoVO> dealLogin(SysUser sysUser, String deviceToken) {
        DchealthCloudResponseEntity<UserInfoVO> result = new DchealthCloudResponseEntity<UserInfoVO>();
        UserInfoVO userInfoVO = new UserInfoVO();
        // 账号停用处理
        if ("1".equals(sysUser.getStatus())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }

        // 账号删除停用处理
        if ("2".equals(sysUser.getDelFlag())) {
            result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
            return result;
        }

        BeanUtils.copyProperties(sysUser,userInfoVO);
        UserInfo userInfo = userInfoMapper.selectUserInfoByUserId(sysUser.getUserId());
        if (userInfo != null){
            userInfoVO = getBaseUserInfo(userInfo, userInfoVO);
        }
        //获取科室信息
//        SysDept sysDept = sysDeptMapper.selectSysDeptByDeptId(sysUser.getDeptId());
        userInfoVO.setDeptName(sysUser.getDeptName());
        userInfoVO.setAccountId(sysUser.getUserId());
        userInfoVO.setUserHead(sysUser.getAvatar());
        userInfoVO.setMobile(sysUser.getPhonenumber());
        insertSysUserLogs(sysUser.getUserId(),"用户登录");

        SMKeyPair smKeyPair = Sm2Utils.genKeyPair(false);
        userInfoVO.setPrivateKey(smKeyPair.getPriKey());
        L.info("登录生成公私钥:{}", JSON.toJSONString(smKeyPair));
        redisCache.cacheString(RedisConstants.TOKEN_PREFIX + "1:" + deviceToken,
                smKeyPair.getPubKey(), 7, TimeUnit.DAYS);
        result.setData(userInfoVO);
        return result;
    }

    private UserInfoVO getBaseUserInfo(UserInfo userInfo, UserInfoVO userInfoVO) {
        userInfoVO.setAuthContext(userInfo.getAuthContext());
        userInfoVO.setBirthday(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,userInfo.getBirthday()));
        userInfoVO.setAuthStatus(userInfo.getAuthStatus());
        userInfoVO.setExpert(userInfo.getExpert());
        userInfoVO.setEducation(userInfo.getEducation());
        userInfoVO.setFigureImage(userInfo.getFigureImage());
        userInfoVO.setDetailAddress(userInfo.getDetailAddress());
        userInfoVO.setTitle(userInfo.getTitle());

        if(userInfo.getProvinceId() != null) {
            SysArea sysArea = sysAreaMapper.selectSysAreaById(userInfo.getProvinceId());
            if(sysArea != null ) {
                userInfoVO.setProvinceName(sysArea.getAreaName());
            }
        }
        if(userInfo.getCityId() != null) {
            SysArea sysArea = sysAreaMapper.selectSysAreaById(userInfo.getCityId());
            if(sysArea != null ) {
                userInfoVO.setCityName(sysArea.getAreaName());
            }
        }
        if(userInfo.getAreaId() != null) {
            SysArea sysArea = sysAreaMapper.selectSysAreaById(userInfo.getAreaId());
            if(sysArea != null ) {
                userInfoVO.setAreaName(sysArea.getAreaName());
            }
        }

        return userInfoVO;
    }

    public DchealthCloudResponseEntity<CommonResult> sendMobileSmsVerifyCode(SendVerfiyCodeForm sendVerfiyCodeForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();

//        if ("1".equals(sendVerfiyCodeForm.getBussinessType())) {
//            SysUser sysUser = sysUserMapper.selectSysUserByMobile(sendVerfiyCodeForm.getMobile());
//            if (sysUser != null) {
//                result.setCode(CroApiCodeConstant.PHONE_ALREADY.getCode());
//                return result;
//            } else {
//                boolean b = sendMobileSms(sendVerfiyCodeForm.getAppId(),
//                        TencentSmsTemplateEnum.REGISTER_CODE.getTemplateId(), sendVerfiyCodeForm.getMobile());
//                commonResult.setResult(b);
//                result.setData(commonResult);
//                return result;
//            }
//        }
//        if ("2".equals(sendVerfiyCodeForm.getBussinessType())) {
            boolean b = sendMobileSms(sendVerfiyCodeForm.getAppId(),
                    TencentSmsTemplateEnum.LOGIN_CODE.getTemplateId(), sendVerfiyCodeForm.getMobile());
            commonResult.setResult(b);
            result.setData(commonResult);
            return result;
//        }
//        if ("3".equals(sendVerfiyCodeForm.getBussinessType())) {
//            SysUser sysUser = sysUserMapper.selectSysUserByMobile(sendVerfiyCodeForm.getMobile());
//            if (sysUser != null) {
//                if ("1".equals(sysUser.getStatus())) {
//                    result.setCode(CroApiCodeConstant.BLOCK_UP_ERROR.getCode());
//                    return result;
//                }
//                boolean b = sendMobileSms(sendVerfiyCodeForm.getAppId(),
//                        TencentSmsTemplateEnum.FORGOT_PASSWORD.getTemplateId(), sendVerfiyCodeForm.getMobile());
//                commonResult.setResult(b);
//                result.setData(commonResult);
//                return result;
//            } else {
//                result.setCode(CroApiCodeConstant.PHONE_NOT_EXIST.getCode());
//                return result;
//            }
//        }
//        if ("4".equals(sendVerfiyCodeForm.getBussinessType())) {
//            boolean b = sendMobileSms(sendVerfiyCodeForm.getAppId(),
//                    TencentSmsTemplateEnum.FORGOT_PASSWORD.getTemplateId(), sendVerfiyCodeForm.getMobile());
//            commonResult.setResult(b);
//            result.setData(commonResult);
//            return result;
//        }

//        result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
//        return result;

    }


    /**
     * 废弃
     */
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

    public DchealthCloudResponseEntity<UserInfoVO> loginBySysUser(String aesloginName, String aespassword,
                                                                  String appType, String deviceToken) {
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
        SysUser sysUser = sysUserMapper.selectSysUserByMobile2(loginName);
        if (sysUser == null) {
            result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
            return result;
        }

        if (!SecurityUtil.matchesPassword(password, sysUser.getPassword())) {
            result.setCode(CroApiCodeConstant.USER_ERROR.getCode());
            return result;
        }
        return dealLogin(sysUser, deviceToken);
    }

    /**
     * 根据手机号或邮箱进行登录密码修改
     * @return
     */
    public DchealthCloudResponseEntity<CommonResult> updatePasswordBySysUser(ForgetPasswordForm forgetPasswordForm) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CommonResult commonResult = new CommonResult();

        String loginName = "";
        String password = "";
        try {
            loginName = Sm4Util.decryptEcb(Constants.SM4_KEY, forgetPasswordForm.getAccount());
            password = Sm4Util.decryptEcb(Constants.SM4_KEY, forgetPasswordForm.getPassword());
        } catch (Exception e) {
            L.error("登录账号:{},登录密码:{}", forgetPasswordForm.getAccount(), forgetPasswordForm.getPassword());
            result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
            return result;
        }

        try {
            // 判断账号是否存在
            SysUser sysUser = null;
            if (loginName.matches(PatternConstant.PHONE)) {
                sysUser = sysUserMapper.selectSysUserByMobile(loginName);
            }

            if (sysUser == null) {
                result.setCode(CroApiCodeConstant.USER_NOT_EXIST.getCode());
                return result;
            }
            String pwd = SecurityUtil.encryptPassword(password);
            // 生成加密后的登陆密码
            sysUser.setPassword(pwd);
            int rs = sysUserMapper.updateSysUser(sysUser);
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

}
