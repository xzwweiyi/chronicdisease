package com.dchealth.framework.web.service;

import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.core.redis.RedisCache;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.exception.user.CaptchaException;
import com.dchealth.common.exception.user.CaptchaExpireException;
import com.dchealth.common.exception.user.UserPasswordNotMatchException;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.MessageUtils;
import com.dchealth.common.utils.ServletUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.ip.IpUtils;
import com.dchealth.framework.manager.AsyncManager;
import com.dchealth.framework.manager.factory.AsyncFactory;
import com.dchealth.system.service.ISysConfigService;
import com.dchealth.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String mobile, String username, String password, String code, String uuid) {
        // 用户验证
        Authentication authentication = null;
        boolean captchaOnOff = configService.selectCaptchaOnOff();
        if (StringUtils.isEmpty(mobile)) {
            // 验证码开关
            if (captchaOnOff) {
            validateCaptcha(username, code, uuid);
            }
            try {
                // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
                authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            } catch (Exception e) {
                if (e instanceof BadCredentialsException) {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                    throw new UserPasswordNotMatchException();
                } else {
                    AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                    throw new ServiceException(e.getMessage());
                }
            }
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            recordLoginInfo(loginUser.getUserId());
            // 生成token
            return tokenService.createToken(loginUser);
        } else {
            // 验证码开关
            if (captchaOnOff) {
                validateCaptchaOfMobile(mobile, code, uuid);
            }
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(mobile, password));
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            recordLoginInfo(loginUser.getUserId());
            // 生成token
            return tokenService.createToken(loginUser);
        }
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    public void validateCaptchaOfMobile(String mobile, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
