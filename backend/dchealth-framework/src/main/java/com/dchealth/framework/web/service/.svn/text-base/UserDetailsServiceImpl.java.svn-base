package com.dchealth.framework.web.service;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginUser;
import com.dchealth.common.enums.UserStatus;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.length() == 11 && isNumeric(username)) {
            SysUser user = userService.selectUserByMobile(username);
            if (StringUtils.isNull(user)) {
                throw new ServiceException("登录用户：" + username + " 不存在");
            } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
                throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
            } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                throw new ServiceException("对不起，您的账号：" + username + " 已停用");
            }
            return createLoginUser(user);
        } else {
            SysUser user = userService.selectUserByUserName(username);
            if (StringUtils.isNull(user)) {
                log.info("登录用户：{} 不存在.", username);
                throw new ServiceException("登录用户：" + username + " 不存在");
            } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
                log.info("登录用户：{} 已被删除.", username);
                throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
            } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
                log.info("登录用户：{} 已被停用.", username);
                throw new ServiceException("对不起，您的账号：" + username + " 已停用");
            }
            return createLoginUser(user);
        }
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
