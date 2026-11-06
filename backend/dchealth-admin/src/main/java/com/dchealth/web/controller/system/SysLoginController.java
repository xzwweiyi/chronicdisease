package com.dchealth.web.controller.system;

import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysMenu;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.domain.model.LoginBody;
import com.dchealth.common.utils.SecurityUtils;
import com.dchealth.framework.web.service.SysLoginService;
import com.dchealth.framework.web.service.SysPermissionService;
import com.dchealth.listener.FollowTreeEvent;
import com.dchealth.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@RestController
public class SysLoginController
{
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(SysLoginController.class);

    /**
     * 登录方法
     * 
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getMobile(),loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
//
        return ajax;
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        if (!Objects.isNull(user)) {
            Long userId = user.getUserId();
            applicationContext.publishEvent(new FollowTreeEvent(this,userId, Boolean.FALSE));
        }
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     * 
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        // Debug: log menu count
        int totalMenus = countAllMenus(menus);
        log.info("getRouters userId={}, topLevel={}, totalMenus={}", userId, menus.size(), totalMenus);
        return AjaxResult.success(menuService.buildMenus(menus));
    }

    private int countAllMenus(List<SysMenu> menus) {
        int count = menus.size();
        for (SysMenu m : menus) {
            if (m.getChildren() != null) count += countAllMenus(m.getChildren());
        }
        return count;
    }

}
