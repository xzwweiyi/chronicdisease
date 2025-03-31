package com.dchealth.research.service;

import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.research.domain.ResearchModule;
import com.dchealth.research.domain.ResearchRole;
import com.dchealth.research.domain.form.ResearchDataPermissionsForm;
import com.dchealth.research.domain.vo.*;

import java.util.List;

public interface IResearchRoleService {

    int insertResearchRole(ResearchRole researchRole);

    int updateResearchRole(ResearchRole researchRole);

    /**
     * 队列角色数据权限
     * @param researchId
     * @param roleId
     * @return ResearchDataPermissions
     */
    public ResearchDataPermissionsVO dataPermissions(Long researchId, Long roleId);

    /**
     * 保存数据权限
     * @param researchDataPermissionsForm
     * @return
     */
    public int saveDataPermissions(ResearchDataPermissionsForm researchDataPermissionsForm);


    List<ResearchRole> selectResearchRoleList(ResearchRole researchRole);

    int insertUserAuth(Long userId, Long[] roleIds);

    //int insertRoledept(Long roleId, Long[] deptIds);

    int removeRole(Long[] roleIds);

    ResearchRole findOneRole(Long roleId);

    void updateDataScope(ResearchRole researchRole);

    int updateStatus(ResearchRole researchRole);

    int batchAuthUserByRole(Long roleId, Long[] userIds,Long researchId);

    int cancelAuthRole(ResearchIdsVo researchIdsVo);

    int batchCancelAuthRole(ResearchIdsVo researchIdsVo);

    List<DeptTreeVo> findRoleDeptByRoleId(Long roleId,Long researchId);

    List<RoleDeptVo> findRoleAuthByRoleId(ResearchIdsVo researchIdsVo);

    List<RoleDeptVo> findRoleAuthByUserId(ResearchIdsVo researchIdsVo);

    List<ResearchModule> findModulehByResearchId(ResearchIdsVo researchIdsVo);

    List<RoleModuleVo> findModuleAuthhByRoleId(ResearchIdsVo researchIdsVo);

    List<RoleModuleVo> findModuleAuthhByUserId(ResearchIdsVo researchIdsVo);

    List<SysUser> findAllocatedUserByRoleId(SysUser sysUser);

    List<SysUser> findUnAllocatedUserByRoleId(SysUser sysUser);


    /**
     * 校验角色名称是否唯一
     *
     * @param researchRole 角色信息
     * @return 结果
     */
    public String checkRoleNameUnique(ResearchRole researchRole);

    /**
     * 校验角色权限是否唯一
     *
     * @param researchRole 角色信息
     * @return 结果
     */
    public String checkRoleKeyUnique(ResearchRole researchRole);
}
