package com.dchealth.web.controller.research;

import com.dchealth.common.constant.UserConstants;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.research.domain.ResearchRole;
import com.dchealth.research.domain.form.ResearchDataPermissionsForm;
import com.dchealth.research.domain.vo.ResearchIdsVo;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import com.dchealth.research.service.IResearchRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 角色信息表
 */
@RestController
@RequestMapping("/researh/role")
public class ResearchRoleController extends BaseController {

    @Autowired
    IResearchRoleService iResearchRoleService;

    /**
     * 查询角色
     */
    @PreAuthorize("@ss.hasPermi('researh:role:query')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody ResearchRole researchRole) {
        startPage();
        List<ResearchRole> list = iResearchRoleService.selectResearchRoleList(researchRole);
        return getDataTable(list);
    }

    /**
     * 新增角色信息
     *
     * @param researchRole
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:role:add')")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody ResearchRole researchRole) {
        if (UserConstants.NOT_UNIQUE.equals(iResearchRoleService.checkRoleNameUnique(researchRole)))
        {
            return AjaxResult.error("新增角色'" + researchRole.getRoleName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(iResearchRoleService.checkRoleKeyUnique(researchRole)))
        {
            return AjaxResult.error("新增角色'" + researchRole.getRoleName() + "'失败，角色权限已存在");
        }
        researchRole.setCreateBy(getUsername());
        researchRole.setCreateTime(new Date());
        return toAjax(iResearchRoleService.insertResearchRole(researchRole));
    }

    /**
     * 修改角色信息
     *
     * @param researchRole
     * @return
     */
    @PreAuthorize("@ss.hasPermi('researh:role:update')")
    @PostMapping("/update")
    public AjaxResult update(@RequestBody ResearchRole researchRole) {
        if (UserConstants.NOT_UNIQUE.equals(iResearchRoleService.checkRoleNameUnique(researchRole)))
        {
            return AjaxResult.error("新增角色'" + researchRole.getRoleName() + "'失败，角色名称已存在");
        }
        else if (UserConstants.NOT_UNIQUE.equals(iResearchRoleService.checkRoleKeyUnique(researchRole)))
        {
            return AjaxResult.error("新增角色'" + researchRole.getRoleName() + "'失败，角色权限已存在");
        }
        researchRole.setUpdateBy(getUsername());
        researchRole.setUpdateTime(new Date());
        return toAjax(iResearchRoleService.updateResearchRole(researchRole));
    }


    /**
     * 根据角色获取数据权限
     * @param roleId
     * @return
     */
    @GetMapping("/dataPermissions/{researchId}/{roleId}")
    public AjaxResult dataPermissions(@PathVariable("researchId")Long researchId, @PathVariable("roleId")Long roleId) {
        return AjaxResult.success(iResearchRoleService.dataPermissions(researchId, roleId));
    }

    /**
     * 保存数据权限
     * @param researchDataPermissionsForm
     * @return
     */
    @PostMapping("/saveDataPermissions")
    public AjaxResult saveDataPermissions(@RequestBody ResearchDataPermissionsForm researchDataPermissionsForm)
    {

        return toAjax(iResearchRoleService.saveDataPermissions(researchDataPermissionsForm));
    }


    /**
     * 为用户分配角色
     */
    @PreAuthorize("@ss.hasPermi('researh:role:update')")
    @PostMapping("/authRole")
    public AjaxResult insertAuthRole(@RequestBody ResearchIdsVo researchIdsVo) {
        Long userId = researchIdsVo.getUserId();
        Long[] roleIds = researchIdsVo.getRoleIds();
        return toAjax(iResearchRoleService.insertUserAuth(userId, roleIds));
    }

    /**
     * 为角色分配部门
     */
    /*@PreAuthorize("@ss.hasPermi('researh:role:update')")
    @PostMapping("/addRoleDept")
    public AjaxResult insertAuthRoledept(@RequestBody ResearchIdsVo researchIdsVo) {
        Long roleId = researchIdsVo.getRoleId();
        Long[] deptIds = researchIdsVo.getDeptIds();
        return toAjax(iResearchRoleService.insertRoledept(roleId, deptIds));
    }*/


    /**
     * 删除角色
     * 先删除菜单、部门，在删除角色
     */
    @PreAuthorize("@ss.hasPermi('researh:role:delete')")
    @PostMapping("/delete")
    public AjaxResult removeRole(@RequestBody ResearchIdsVo researchIdsVo) {
        Long[] roleIds = researchIdsVo.getRoleIds();
        return toAjax(iResearchRoleService.removeRole(roleIds));
    }

    /**
     * 根据角色id查询角色
     */
    @PreAuthorize("@ss.hasPermi('researh:role:query')")
    @PostMapping("/findOneRole")
    public AjaxResult findOneRole(@RequestBody ResearchIdsVo researchIdsVo) {
        Long roleId = researchIdsVo.getRoleId();
        return AjaxResult.success(iResearchRoleService.findOneRole(roleId));
    }

    /**
     * 分配数据权限--数据权限树形结构
     */
    @PreAuthorize("@ss.hasPermi('researh:role:query')")
    @PostMapping("/findDeptByRoleId")
    public AjaxResult findDeptByRoleId(@RequestBody ResearchIdsVo researchIdsVo) {
        Long roleId = researchIdsVo.getRoleId();
        Long researchId = researchIdsVo.getResearchId();
        return AjaxResult.success(iResearchRoleService.findRoleDeptByRoleId(roleId, researchId));
    }

    /**
     * 修改保存数据权限
     */
    @PreAuthorize("@ss.hasPermi('researh:role:update')")
    @PostMapping("/updateDataScope")
    public AjaxResult updateDataScope(@RequestBody ResearchRole researchRole) {
        iResearchRoleService.updateDataScope(researchRole);
        return AjaxResult.success();
    }

    /**
     * 修改角色状态
     */
    @PreAuthorize("@ss.hasPermi('researh:role:update')")
    @PostMapping("/updateStatus")
    public AjaxResult updateStatus(@RequestBody ResearchRole researchRole) {
        return toAjax(iResearchRoleService.updateStatus(researchRole));
    }

    /**
     * 为角色批量分配用户
     */
    @PreAuthorize("@ss.hasPermi('researh:role:update')")
    @PostMapping("/batchAuthUserByRole")
    public AjaxResult batchAuthUserByRole(@RequestBody ResearchIdsVo researchIdsVo) {
        Long roleId = 0l;
        Long[] userIds = researchIdsVo.getUserIds();
        Long researchId = researchIdsVo.getResearchId();
        return toAjax(iResearchRoleService.batchAuthUserByRole(roleId, userIds,researchId));
    }

    /**
     * 取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:delete')")
    @PostMapping("/cancelAuthRole")
    public AjaxResult cancelAuthRole(@RequestBody ResearchIdsVo researchIdsVo) {
        return toAjax(iResearchRoleService.cancelAuthRole(researchIdsVo));
    }

    /**
     * 批量取消授权用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:delete')")
    @PostMapping("/batchCancelAuthRole")
    public AjaxResult batchCancelAuthRole(@RequestBody ResearchIdsVo researchIdsVo) {
        return toAjax(iResearchRoleService.batchCancelAuthRole(researchIdsVo));
    }


    /**
     * 根据角色id查询数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findRoleAuthByRoleId")
    public AjaxResult findRoleAuthByRoleId(@RequestBody ResearchIdsVo researchIdsVo) {
        return AjaxResult.success(iResearchRoleService.findRoleAuthByRoleId(researchIdsVo));
    }

    /**
     * 根据用户id查询数据权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findRoleAuthByUserId")
    public AjaxResult findRoleAuthByUserId(@RequestBody ResearchIdsVo researchIdsVo) {
        return AjaxResult.success(iResearchRoleService.findRoleAuthByUserId(researchIdsVo));
    }

    /**
     * 根据队列id查询模块集合--模块列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findModulehByResearchId")
    public AjaxResult findModulehByResearchId(@RequestBody ResearchIdsVo researchIdsVo) {
        return AjaxResult.success(iResearchRoleService.findModulehByResearchId(researchIdsVo));
    }

    /**
     * 根据角色id查询模块权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findModuleAuthhByRoleId")
    public AjaxResult findModuleAuthhByRoleId(@RequestBody ResearchIdsVo researchIdsVo) {
        return AjaxResult.success(iResearchRoleService.findModuleAuthhByRoleId(researchIdsVo));
    }

    /**
     * 根据用户id查询模块权限
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findModuleAuthhByUserId")
    public AjaxResult findModuleAuthhByUserId(@RequestBody ResearchIdsVo researchIdsVo) {
        return AjaxResult.success(iResearchRoleService.findModuleAuthhByUserId(researchIdsVo));
    }

    /**
     * 根据角色id查询已分配的用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findAllocatedUserByRoleId")
    public TableDataInfo findAllocatedUserByRoleId(@RequestBody SysUser sysUser) {
        startPage();
        List<SysUser> sysUserList = iResearchRoleService.findAllocatedUserByRoleId(sysUser);
        return getDataTable(sysUserList);
    }


    /**
     * 根据角色id查询未分配的用户
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @PostMapping("/findUnAllocatedUserByRoleId")
    public TableDataInfo findUnAllocatedUserByRoleId(@RequestBody SysUser sysUser) {
        sysUser.setUserId(getUserId());
        startPage();
        List<SysUser> sysUserList = iResearchRoleService.findUnAllocatedUserByRoleId(sysUser);
        return getDataTable(sysUserList);
    }


}
