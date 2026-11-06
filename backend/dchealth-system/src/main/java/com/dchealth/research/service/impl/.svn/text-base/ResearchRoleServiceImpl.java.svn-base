package com.dchealth.research.service.impl;

import com.dchealth.common.constant.UserConstants;
import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.common.core.domain.entity.SysUser;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.research.domain.*;
import com.dchealth.research.domain.form.ResearchDataPermissionsForm;
import com.dchealth.research.domain.vo.*;
import com.dchealth.research.mapper.*;
import com.dchealth.research.service.IResearchRoleService;
import com.dchealth.system.domain.SysPost;
import com.dchealth.system.domain.SysUserPost;
import com.dchealth.system.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ResearchRoleServiceImpl implements IResearchRoleService {

    @Autowired
    ResearchRoleMapper researchRoleMapper;

    @Autowired
    ResearchUserRoleMapper researchUserRoleMapper;

    @Autowired
    ResearchRoleDeptMapper researchRoleDeptMapper;

    @Autowired
    ResearchRoleSourceMapper researchRoleSourceMapper;

    @Autowired
    SysDeptMapper sysDeptMapper;

    @Autowired
    ResearchInfoMapper researchInfoMapper;

    @Autowired
    ResearchHospitalMapper researchHospitalMapper;

    @Autowired
    ResearchRoleAuthMapper researchRoleAuthMapper;

    @Autowired
    ResearchModuleMapper researchModuleMapper;

    @Autowired
    ResearchRoleModuleMapper researchRoleModuleMapper;

    @Autowired
    ResearchRoleModuleAuthMapper researchRoleModuleAuthMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysDictDataMapper sysDictDataMapper;
    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;
    @Autowired
    private SysPostMapper sysPostMapper;
    @Autowired
    private SysUserPostMapper sysUserPostMapper;

    @Override
    @Transactional
    public int insertResearchRole(ResearchRole researchRole) {
        researchRoleMapper.insertResearchRole(researchRole);
        return insertRoleSources(researchRole);
    }

    /**
     * 新增角色app资源信息
     *
     * @param researchRole 角色对象
     */
    public int insertRoleSources(ResearchRole researchRole)
    {
        if (researchRole.getSourceIds() == null || researchRole.getSourceIds().length == 0){
            return 1;
        }
        int rows = 1;
        // 新增用户与角色管理
        List<ResearchRoleSource> list = new ArrayList<ResearchRoleSource>();
        for (Long sourceId : researchRole.getSourceIds())
        {
            ResearchRoleSource rm = new ResearchRoleSource();
            rm.setRoleId(researchRole.getRoleId());
            rm.setSourceId(sourceId);
            list.add(rm);
        }
        if (list.size() > 0)
        {
            rows = researchRoleSourceMapper.batchRoleSource(list);
        }
        return rows;
    }

    @Override
    public int updateResearchRole(ResearchRole researchRole) {
        researchRoleMapper.updateResearchRole(researchRole);
        researchRoleSourceMapper.deleteRoleSource(researchRole.getRoleId());
        return insertRoleSources(researchRole);
    }

    @Override
    public ResearchDataPermissionsVO dataPermissions(Long researchId, Long roleId) {
        ResearchDataPermissionsVO dataPermissions = new ResearchDataPermissionsVO();
        ResearchRole researchRole = researchRoleMapper.findResearchRoleByRoleId(roleId);
        if (researchRole != null){
            dataPermissions.setRoleId(roleId);
            dataPermissions.setRoleName(researchRole.getRoleName());
        }
        //全部随访节点
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);
        //已配置的随访节点
        List<ResearchRoleModule> researchRoleModules = researchRoleModuleMapper.findRoleModuleByRoleId(roleId);
        List<ResearchFollowVO> researchFollowVOS = followNodes(patientRecordTemplates, researchRoleModules);
        dataPermissions.setResearchFollowVOS(researchFollowVOS);
        List<SysDept> sysDepts = sysDeptMapper.selectDeptList(new SysDept());
        List<ResearchRoleDept> researchRoleDepts = researchRoleDeptMapper.findRoleDeptByRoleId(roleId);
        List<ResearchRoleAuth> researchRoleAuths = researchRoleAuthMapper.findRoleAuthByRoleId(roleId);
        List<ResearchDeptVO> deptVOS = tree(0l,deptAndOper(roleId, sysDepts, researchRoleDepts, researchRoleAuths));
        dataPermissions.setDeptVOS(deptVOS);
        return dataPermissions;
    }

    @Override
    @Transactional
    public int saveDataPermissions(ResearchDataPermissionsForm researchDataPermissionsForm) {
        ResearchRole researchRole = new ResearchRole();
        researchRole.setRoleId(researchDataPermissionsForm.getRoleId());
        researchRole.setDataScope(researchDataPermissionsForm.getDataScope());
        researchRoleMapper.updateResearchRole(researchRole);
        //删除随访节点勾选
        researchRoleModuleMapper.deleteRoleModuleByRoleId(researchDataPermissionsForm.getRoleId());
        //删除科室勾选
        researchRoleDeptMapper.deleteRoledept(researchDataPermissionsForm.getRoleId());
        //删除科室操作权限勾选
        researchRoleAuthMapper.deleteRoleDeptAuTh(researchDataPermissionsForm.getRoleId());
        //保存随访节点勾选
        if (researchDataPermissionsForm.getFollowIds() != null && !researchDataPermissionsForm.getFollowIds().isEmpty()){
            List<ResearchRoleModule> researchRoleModules = researchDataPermissionsForm.getFollowIds()
                    .stream()
                    .map(x->new ResearchRoleModule(researchDataPermissionsForm.getRoleId(),x))
                    .collect(Collectors.toList());
            researchRoleModuleMapper.batchRoleModule(researchRoleModules);
        }
        //保存科室勾选
        if (researchDataPermissionsForm.getDeptAndOperForms() != null && !researchDataPermissionsForm.getDeptAndOperForms().isEmpty()){
            List<ResearchRoleDept> researchRoleDepts = researchDataPermissionsForm.getDeptAndOperForms()
                    .stream()
                    .map(x->new ResearchRoleDept(researchDataPermissionsForm.getRoleId(),x.getDeptId()))
                    .collect(Collectors.toList());
            researchRoleDeptMapper.batchRoleDept(researchRoleDepts);
        }

        //保存科室操作权限勾选
        if (researchDataPermissionsForm.getDeptAndOperForms() != null && !researchDataPermissionsForm.getDeptAndOperForms().isEmpty()){
            List<ResearchRoleAuth> researchRoleAuths = new ArrayList<>();
            researchDataPermissionsForm.getDeptAndOperForms()
                .stream()
                .filter(x->x.getOpers() != null && !x.getOpers().isEmpty())
                .forEach(x->researchRoleAuths.addAll(x.getOpers()
                        .stream()
                        .map(y->new ResearchRoleAuth(researchDataPermissionsForm.getRoleId(),x.getDeptId(),y))
                        .collect(Collectors.toList())));


            if (researchRoleAuths != null && !researchRoleAuths.isEmpty()){
                researchRoleAuthMapper.batchRoleAuth(researchRoleAuths);
            }
        }
        return 1;
    }

    /**
     * 数据结构
     * @param parentId
     * @param researchDeptVOS
     * @return
     */
    private List<ResearchDeptVO> tree(long parentId, List<ResearchDeptVO> researchDeptVOS){
        if (researchDeptVOS == null || researchDeptVOS.isEmpty()){
            return null;
        }
        List<ResearchDeptVO> list = researchDeptVOS.stream().filter(x->x.getParentDeptId().longValue() == parentId).collect(Collectors.toList());

        if (list != null && !list.isEmpty()){
            list.forEach(x->x.setChildren(tree(x.getDeptId().longValue(), researchDeptVOS)));
        }
        return list;
    }


    /**
     * 数据权限部门与操作权限
     * @param roleId
     * @param sysDepts
     * @param researchRoleDepts
     * @param researchRoleAuths
     * @return
     */
    private List<ResearchDeptVO> deptAndOper(Long roleId, List<SysDept> sysDepts, List<ResearchRoleDept> researchRoleDepts, List<ResearchRoleAuth> researchRoleAuths){
        if (sysDepts == null || sysDepts.isEmpty()){
            return null;
        }
        List<ResearchDeptVO> researchDeptVOS = new ArrayList<>();
        //角色部门勾选
        Map<Long,Long> researchRoleDeptsMap = null;
        if (researchRoleDepts != null && !researchRoleDepts.isEmpty()){
            researchRoleDeptsMap = researchRoleDepts.stream().collect(Collectors.toMap(ResearchRoleDept :: getDeptId,ResearchRoleDept::getRoleId));
        }

        //角色部门操作权限
        Map<String,List<ResearchRoleAuth>> researchRoleAuthsMap = null;
        if (researchRoleAuths != null && !researchRoleAuths.isEmpty()){
            researchRoleAuthsMap = researchRoleAuths.stream().collect(Collectors.groupingBy(p->p.getRoleId().longValue()+"_"+p.getDeptId()));
        }
        for (SysDept sysDept : sysDepts) {
            ResearchDeptVO researchDeptVO = new ResearchDeptVO();
            researchDeptVO.setDeptId(sysDept.getDeptId());
            researchDeptVO.setParentDeptId(sysDept.getParentId());
            researchDeptVO.setDeptName(sysDept.getDeptName());
            researchDeptVO.setCheck(false);
            //勾选部门
            if (researchRoleDeptsMap != null && researchRoleDeptsMap.containsKey(sysDept.getDeptId())){
                researchDeptVO.setCheck(true);
            }
            //设置操作权限
            if (researchRoleAuthsMap != null && researchRoleAuthsMap.containsKey(roleId.longValue()+"_"+sysDept.getDeptId().longValue())){
                researchDeptVO.setOpers(researchRoleAuthsMap.get(roleId.longValue()+"_"+sysDept.getDeptId().longValue())
                        .stream()
                        .map(x->x.getAuthType())
                        .collect(Collectors.toList()));
            }
            researchDeptVOS.add(researchDeptVO);
        }
        return researchDeptVOS;

    }


    /**
     * 获取队列随访节点
     * @param patientRecordTemplates
     * @param researchRoleModules
     * @return
     */
    private List<ResearchFollowVO> followNodes(List<PatientRecordTemplate> patientRecordTemplates, List<ResearchRoleModule> researchRoleModules){
        if (patientRecordTemplates == null && patientRecordTemplates.isEmpty()){
            return null;
        }
        List<ResearchFollowVO> list = new ArrayList<>();
        patientRecordTemplates.forEach(x->{
            ResearchFollowVO researchFollowVO = new ResearchFollowVO();
            researchFollowVO.setFollowId(x.getFollowId());
            researchFollowVO.setCheck(false);
            researchFollowVO.setFollowName(x.getFollowName());
            if (researchRoleModules != null && !researchRoleModules.isEmpty()){
                for (ResearchRoleModule researchRoleModule : researchRoleModules) {
                    if (x.getFollowId().longValue() == researchRoleModule.getFollowId().longValue()){
                        researchFollowVO.setCheck(true);
                    }
                }
            }
            list.add(researchFollowVO);
        });
        
        return list;
    }


    @Override
    public List<ResearchRole> selectResearchRoleList(ResearchRole researchRole) {
        return researchRoleMapper.selectResearchRoleList(researchRole);
    }

    @Override
    @Transactional
    public int insertUserAuth(Long userId, Long[] roleIds) {
        //先删除该用户角色
        researchUserRoleMapper.deleteUserAuth(userId);
        //再新增该用户角色
        return this.insertResearchUserRole(userId, roleIds);

    }

    private int insertResearchUserRole(Long userId, Long[] roleIds) {
        List<ResearchUserRole> researchUserRoles = new ArrayList<>();
        if (roleIds.length > 0) {
            for (Long roleId : roleIds) {
                ResearchUserRole researchUserRole = new ResearchUserRole();
                researchUserRole.setUserId(userId);
                researchUserRole.setRoleId(roleId);
                researchUserRoles.add(researchUserRole);
            }
        }
        return researchUserRoleMapper.batchUserRole(researchUserRoles);
    }

   /* @Override
    @Transactional
    public int insertRoledept(Long roleId, Long[] deptIds) {
        //先删除该角色下的部门
        researchRoleDeptMapper.deleteRoledept(roleId);
        //再新增该角色下的部门
        return this.insertResearchRoleDept(roleId, deptIds);
    }*/


    private void insertResearchRoleDept(Long roleId, List<Long> deptIds) {

        List<ResearchRoleDept> researchRoleDepts = new ArrayList<>();
        if (deptIds.size() > 0) {
            for (Long deptId : deptIds) {
                ResearchRoleDept researchRoleDept = new ResearchRoleDept();
                researchRoleDept.setRoleId(roleId);
                researchRoleDept.setDeptId(deptId);
                researchRoleDepts.add(researchRoleDept);
            }
            researchRoleDeptMapper.batchRoleDept(researchRoleDepts);
        }
    }

    @Override
    @Transactional
    public int removeRole(Long[] roleIds) {
        if (roleIds.length > 0) {
            for (Long roleId : roleIds) {
                int count = researchUserRoleMapper.findUserRoleNumberByRoleId(roleId);
                ResearchRole researchRole = researchRoleMapper.findResearchRoleByRoleId(roleId);
                if (count > 0) {
                    throw new ServiceException(String.format("%1$s已分配,不能删除", researchRole.getRoleName()));
                }
            }
        }
        // 删除角色与菜单关联
        researchRoleSourceMapper.deleteRoleSourceByRoleIds(roleIds);
        // 删除角色与部门关联
        researchRoleDeptMapper.deleteRoleDeptByRoleIds(roleIds);
        //删除角色-部门-权限
        researchRoleAuthMapper.deleteRoleDeptAuThByRoleIds(roleIds);
        //删除角色-模块
        researchRoleModuleMapper.deleteRoleModuleByRoleIds(roleIds);
        //删除角色-模块-权限
//        researchRoleModuleAuthMapper.deleteRoleModuleAuthByRoleIds(roleIds);
        return researchRoleMapper.deleteRoleByIds(roleIds);
    }

    @Override
    public ResearchRole findOneRole(Long roleId) {
        return researchRoleMapper.findResearchRoleByRoleId(roleId);
    }

    @Override
    @Transactional
    public void updateDataScope(ResearchRole researchRole) {
        // 修改角色信息
        researchRoleMapper.updateResearchRole(researchRole);

        // 删除角色与部门关联
        researchRoleDeptMapper.deleteRoledept(researchRole.getRoleId());

        //删除部门权限关联
        researchRoleAuthMapper.deleteRoleDeptAuTh(researchRole.getRoleId());

        //删除角色与模块关联
        researchRoleModuleMapper.deleteRoleModuleByRoleId(researchRole.getRoleId());

        //删除角色与模块权限关联
        researchRoleModuleAuthMapper.deleteRoleModuleAuthByRoleId(researchRole.getRoleId());

        //部门
        List<RoleDeptVo> roleDepts = researchRole.getRoleDepts();
        List<Long> deptIds = new ArrayList<>();
        if (roleDepts != null && roleDepts.size() > 0) {
            deptIds = roleDepts.stream().map(RoleDeptVo::getDeptId).distinct().collect(Collectors.toList());
        }
        //模块
        List<RoleModuleVo> roleModuleVos = researchRole.getRoleModuleVos();
        if (roleModuleVos != null && roleModuleVos.size() > 0) {
            List<String> moduleIds = roleModuleVos.stream().map(RoleModuleVo::getModuleId).distinct().collect(Collectors.toList());
            List<ResearchRoleModule> researchRoleModules = new ArrayList<>();
            if (moduleIds.size() > 0) {
                for (String moduleId : moduleIds) {
                    ResearchRoleModule researchRoleModule = new ResearchRoleModule();
                    researchRoleModule.setRoleId(researchRole.getRoleId());
//                    researchRoleModule.setModuleId(moduleId);
                    researchRoleModules.add(researchRoleModule);
                }

                researchRoleModuleMapper.batchRoleModule(researchRoleModules);
            }


            List<ResearchRoleModuleAuth> moduleAuths = new ArrayList<>();
            //新增部门权限
            for (RoleModuleVo moduleVo : roleModuleVos) {
                List<String> authType = moduleVo.getAuthType();
                if (authType.size() > 0) {
                    for (String auth : authType) {
                        ResearchRoleModuleAuth researchRoleModuleAuth = new ResearchRoleModuleAuth();
                        researchRoleModuleAuth.setRoleId(researchRole.getRoleId());
                        researchRoleModuleAuth.setModuleId(moduleVo.getModuleId());
                        researchRoleModuleAuth.setAuthType(auth);
                        moduleAuths.add(researchRoleModuleAuth);
                    }
                }
            }
            if (moduleAuths.size() > 0) {
                researchRoleModuleAuthMapper.batchRoleModuleAuth(moduleAuths);
            }
        }

        //新增和角色部门权限
        List<ResearchRoleAuth> auths = new ArrayList<>();
        if (roleDepts != null && roleDepts.size() > 0) {
            for (RoleDeptVo deptVo : roleDepts) {
                List<String> authType = deptVo.getAuthType();
                if (authType.size() > 0) {
                    for (String auth : authType) {
                        ResearchRoleAuth researchRoleAuth = new ResearchRoleAuth();
                        researchRoleAuth.setRoleId(researchRole.getRoleId());
                        researchRoleAuth.setDeptId(deptVo.getDeptId());
                        researchRoleAuth.setAuthType(auth);
                        auths.add(researchRoleAuth);
                    }
                }
            }
        }
        if (auths.size() > 0) {
            researchRoleAuthMapper.batchRoleAuth(auths);
        }

        // 新增和角色部门信息（数据权限）
        this.insertResearchRoleDept(researchRole.getRoleId(), deptIds);
    }

    @Override
    public int updateStatus(ResearchRole researchRole) {
        return researchRoleMapper.updateResearchRole(researchRole);
    }

    @Override
    public int batchAuthUserByRole(Long roleId, Long[] userIds,Long researchId) {
        List<ResearchUserRole> list = new ArrayList<>();
        for (Long userId : userIds) {
            ResearchUserRole ur = new ResearchUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            ur.setResearchId(researchId);
            list.add(ur);
        }
        return researchUserRoleMapper.batchUserRole(list);
    }

    @Override
    public int cancelAuthRole(ResearchIdsVo researchIdsVo) {
        return researchUserRoleMapper.cancelAuthRole(researchIdsVo.getUserId(), researchIdsVo.getResearchId());
    }

    @Override
    public int batchCancelAuthRole(ResearchIdsVo researchIdsVo) {
        return researchUserRoleMapper.batchCancelAuthRole(researchIdsVo.getResearchId(), researchIdsVo.getUserIds());
    }

    @Override
    public List<DeptTreeVo> findRoleDeptByRoleId(Long roleId, Long researchId) {
        //查询队列
        ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
        String researhType = researchInfo.getResearchType();
        //根据队列查询队列参与中心
        ResearchHospital researchHospital = new ResearchHospital();
        researchHospital.setResearchId(researchId);
        List<ResearchHospital> researhHospitals = researchHospitalMapper.selectByResearchHospital(researchHospital);
        List<DeptTreeVo> deptTreeVos = new ArrayList<>();
        // 本院展示到部门
        if (researhHospitals.size() > 0) {
            deptTreeVos = getDeptTreeVo(researhHospitals, researhType);
        }
        return deptTreeVos;
    }

    private List<DeptTreeVo> getDeptTreeVo(List<ResearchHospital> researhHospitals, String researhType) {
        //List<SysDictData> dictDataList = sysDictDataMapper.selectDictDataByType("dept_oper_type");
       /* List<String> dictValues = new ArrayList<>();
        if (dictDataList.size()>0){
            dictValues = dictDataList.stream().map(SysDictData::getDictValue).distinct().collect(Collectors.toList());
        }*/
        List<DeptTreeVo> deptTreeVos = new ArrayList<>();
        for (ResearchHospital hospital : researhHospitals) {
            if ("1".equals(researhType)) {
                List<SysDept> sysDepts = sysDeptMapper.findDeptListByAncestors(hospital.getDeptId());
                DeptTreeVo deptTreeVo = new DeptTreeVo();
                deptTreeVo.setDeptId(hospital.getDeptId());
                deptTreeVo.setDeptName(hospital.getDeptName());
                deptTreeVo.setDeptTreeVos(getDeptTree(hospital.getDeptId(), sysDepts));
                deptTreeVo.setAuthType(new ArrayList<>());
                deptTreeVos.add(deptTreeVo);

            } else if ("2".equals(researhType)) {
                DeptTreeVo deptTreeVo = new DeptTreeVo();
                deptTreeVo.setDeptId(hospital.getDeptId());
                deptTreeVo.setDeptName(hospital.getDeptName());
                deptTreeVo.setAuthType(new ArrayList<>());
                deptTreeVos.add(deptTreeVo);
            }
        }
        return deptTreeVos;
    }

    private List<DeptTreeVo> getDeptTree(Long deptId, List<SysDept> sysDepts) {
        List<DeptTreeVo> childrenList = new ArrayList<>();
        for (SysDept sysDept : sysDepts) {
            Long parentId = sysDept.getParentId();
            if (deptId.equals(parentId)) {
                DeptTreeVo treeVO = new DeptTreeVo();
                treeVO.setDeptId(sysDept.getDeptId());
                treeVO.setDeptName(sysDept.getDeptName());
                treeVO.setParentId(sysDept.getParentId());
                treeVO.setAuthType(new ArrayList<>());
                treeVO.setDeptTreeVos(getDeptTree(sysDept.getDeptId(), sysDepts));
                childrenList.add(treeVO);
            }
        }
        return childrenList;
    }

    @Override
    public List<RoleDeptVo> findRoleAuthByRoleId(ResearchIdsVo researchIdsVo) {
        List<RoleDeptVo> roleDeptVos = new ArrayList<>();
        Long roleId = researchIdsVo.getRoleId();
        //根据角色id查询部门
        List<ResearchRoleDept> researchRoleDepts = researchRoleDeptMapper.findRoleDeptByRoleId(roleId);
        if (researchRoleDepts.size() > 0) {
            for (ResearchRoleDept researchRoleDept : researchRoleDepts) {
                RoleDeptVo roleDeptVo = new RoleDeptVo();
                Long deptId = researchRoleDept.getDeptId();
                roleDeptVo.setDeptId(deptId);
                List<ResearchRoleAuth> authList = researchRoleAuthMapper.findRoleAuthByRoleIdAndDeptId(roleId, deptId);
                if (authList.size() > 0) {
                    List<String> authTypes = authList.stream().map(ResearchRoleAuth::getAuthType).distinct().collect(Collectors.toList());
                    roleDeptVo.setAuthType(authTypes);
                }
                roleDeptVos.add(roleDeptVo);
            }
        }
        return roleDeptVos;
    }

    @Override
    public List<RoleDeptVo> findRoleAuthByUserId(ResearchIdsVo researchIdsVo) {
        List<RoleDeptVo> roleDeptVos = new ArrayList<>();
        Long userId = researchIdsVo.getUserId();
        //查询该用户所有角色
        List<ResearchUserRole> researchUserRoles = researchUserRoleMapper.findUserRoleByUserId(userId);
        if (researchUserRoles.size() > 0) {
            List<Long> roleIds = researchUserRoles.stream().map(ResearchUserRole::getRoleId).distinct().collect(Collectors.toList());
            //根据角色查询部门权限
            List<ResearchRoleDept> researchRoleDepts = researchRoleDeptMapper.findRoleDeptByRoleIds(roleIds);
            if (researchRoleDepts.size() > 0) {
                List<Long> deptIds = researchRoleDepts.stream().map(ResearchRoleDept::getDeptId).distinct().collect(Collectors.toList());
                List<ResearchRoleAuth> researchRoleAuths = new ArrayList<>();
                for (ResearchRoleDept researchRoleDept : researchRoleDepts) {
                    Long roleId = researchRoleDept.getRoleId();
                    Long deptId = researchRoleDept.getDeptId();
                    List<ResearchRoleAuth> authList = researchRoleAuthMapper.findRoleAuthByRoleIdAndDeptId(roleId, deptId);
                    if (authList.size()>0){
                        for (ResearchRoleAuth researchRoleAuth : authList) {
                             researchRoleAuths.add(researchRoleAuth);
                        }
                    }
                }
                if (researchRoleAuths.size() > 0) {
                    Map<Long, List<ResearchRoleAuth>> collect = researchRoleAuths.stream().collect(Collectors.groupingBy(ResearchRoleAuth::getDeptId));
                    for (Long deptId : deptIds) {
                        RoleDeptVo roleDeptVo = new RoleDeptVo();
                        roleDeptVo.setDeptId(deptId);
                        List<ResearchRoleAuth> authList = collect.get(deptId);
                        if (authList!= null && authList.size()>0){
                            List<String> authTypes = authList.stream().map(ResearchRoleAuth::getAuthType).distinct().collect(Collectors.toList());
                            roleDeptVo.setAuthType(authTypes);
                            roleDeptVos.add(roleDeptVo);
                        }
                    }
                }
            }
        }
        return roleDeptVos;
    }

    @Override
    public List<ResearchModule> findModulehByResearchId(ResearchIdsVo researchIdsVo) {
       /* List<ResearchModule> researchModules = researchModuleMapper.findModulehByResearchId(researchIdsVo.getResearchId());

        List<SysDictData>  dictDataList = sysDictDataMapper.selectDictDataByType("research_module");

        if (researchModules.size()>0){
            for (ResearchModule module : researchModules) {
                String moduleId = module.getModuleId();
                for (SysDictData sysDictData : dictDataList) {
                    if (moduleId.equals(sysDictData.getDictValue())){
                        module.setModuleName(sysDictData.getDictLabel());
                    }
                }
            }
        }
        return researchModules;*/
       return new ArrayList<>();
    }

    @Override
    public List<RoleModuleVo> findModuleAuthhByRoleId(ResearchIdsVo researchIdsVo) {
        List<RoleModuleVo> roleModuleVos = new ArrayList<>();
        Long roleId = researchIdsVo.getRoleId();
        List<ResearchRoleModule> researchRoleModules = researchRoleModuleMapper.findRoleModuleByRoleId(roleId);
        if (researchRoleModules.size() > 0) {
            for (ResearchRoleModule researchRoleModule : researchRoleModules) {
                RoleModuleVo roleModuleVo = new RoleModuleVo();
                /*String moduleId = researchRoleModule.getModuleId();
                roleModuleVo.setModuleId(moduleId);
                List<ResearchRoleModuleAuth> authList = researchRoleModuleAuthMapper.findRoleModuleAuthByRoleIdAndModuleId(roleId, moduleId);
                if (authList.size() > 0) {
                    List<String> authTypes = authList.stream().map(ResearchRoleModuleAuth::getAuthType).distinct().collect(Collectors.toList());
                    roleModuleVo.setAuthType(authTypes);
                }*/
                roleModuleVos.add(roleModuleVo);
            }
        }
        return roleModuleVos;
    }

    @Override
    public List<RoleModuleVo> findModuleAuthhByUserId(ResearchIdsVo researchIdsVo) {
        List<RoleModuleVo> roleModuleVos = new ArrayList<>();
        //根据用户查询所有角色
        List<ResearchUserRole> researchUserRoles = researchUserRoleMapper.findUserRoleByUserId(researchIdsVo.getUserId());
        if (researchUserRoles.size() > 0) {
            List<ResearchRoleModuleAuth> researchRoleModuleAuthList = new ArrayList<>();
            List<Long> roleIds = researchUserRoles.stream().map(ResearchUserRole::getRoleId).distinct().collect(Collectors.toList());
            //根据角色查询所有 角色-模块
            List<ResearchRoleModule> researchRoleModules = researchRoleModuleMapper.findRoleModuleByRoleIds(roleIds);
            if (researchRoleModules.size() > 0) {
                /*List<String> moduleIds = researchRoleModules.stream().map(ResearchRoleModule::getModuleId).distinct().collect(Collectors.toList());
                for (ResearchRoleModule researchRoleModule : researchRoleModules) {
                    List<ResearchRoleModuleAuth> researchRoleModuleAuths = researchRoleModuleAuthMapper.findRoleModuleAuthByRoleIdAndModuleId(researchRoleModule.getRoleId(), researchRoleModule.getModuleId());
                    if (researchRoleModuleAuths.size() > 0) {
                        for (ResearchRoleModuleAuth researchRoleModuleAuth : researchRoleModuleAuths) {
                            researchRoleModuleAuthList.add(researchRoleModuleAuth);
                        }
                    }
                }
                if (researchRoleModuleAuthList.size() > 0) {
                    Map<String, List<ResearchRoleModuleAuth>> collect = researchRoleModuleAuthList.stream().collect(Collectors.groupingBy(ResearchRoleModuleAuth::getModuleId));
                    for (String moduleId : moduleIds) {
                        List<ResearchRoleModuleAuth> researchRoleModuleAuths = collect.get(moduleId);
                        RoleModuleVo roleModuleVo = new RoleModuleVo();
                        roleModuleVo.setModuleId(moduleId);
                        if (researchRoleModuleAuths!= null && researchRoleModuleAuths.size()>0){
                            List<String> authTypes = researchRoleModuleAuths.stream().map(ResearchRoleModuleAuth::getAuthType).distinct().collect(Collectors.toList());
                            roleModuleVo.setAuthType(authTypes);
                            roleModuleVos.add(roleModuleVo);
                        }
                    }
                }*/
            }
        }
        return roleModuleVos;
    }

    @Override
    public List<SysUser> findAllocatedUserByRoleId(SysUser sysUser) {
        List<SysUser>  sysUserList = sysUserMapper.findAllocatedUserByRoleId(sysUser);
        List<SysPost> sysPostList = sysPostMapper.selectPostAll();
        Map<Long,SysPost> sysPostMap =  sysPostList.stream().collect(Collectors.toMap(SysPost::getPostId, Function.identity(), (key1, key2) -> key2));

        List<SysDept> sysDeptList = sysDeptMapper.selectDeptList(new SysDept());
        Map<Long,SysDept> sysDeptMap = sysDeptList.stream().collect(Collectors.toMap(SysDept::getDeptId, Function.identity(), (key1, key2) -> key2));

        for(SysUser s:sysUserList){
            SysUserPost sysUserPost = sysUserPostMapper.selectPostByUserId(s.getUserId());
            if(sysUserPost!=null){
                if(sysPostMap.containsKey(sysUserPost.getPostId())){
                    SysPost sp = sysPostMap.get(sysUserPost.getPostId());
                    s.setPostName(sp.getPostName());
                }
            }
            if(sysDeptMap.containsKey(s.getDeptId())){
                SysDept sd = sysDeptMap.get(s.getDeptId());
                if(sd!=null){
                    s.setDeptNames(sd.getDeptName());
                }
            }
        }
        return sysUserList;
    }

    @Override
    public List<SysUser> findUnAllocatedUserByRoleId(SysUser sysUser) {
        if(sysUser.getDeptId()==null){
            sysUser.setDeptId(100l);
        }
        List<SysUser> sysUserList = sysUserMapper.findUnAllocatedUserByRoleId(sysUser);

        List<SysPost> sysPostList = sysPostMapper.selectPostAll();
        Map<Long,SysPost> sysPostMap =  sysPostList.stream().collect(Collectors.toMap(SysPost::getPostId, Function.identity(), (key1, key2) -> key2));

        List<SysDept> sysDeptList = sysDeptMapper.selectDeptList(new SysDept());
        Map<Long,SysDept> sysDeptMap = sysDeptList.stream().collect(Collectors.toMap(SysDept::getDeptId, Function.identity(), (key1, key2) -> key2));

        for(SysUser s:sysUserList){
            SysUserPost sysUserPost = sysUserPostMapper.selectPostByUserId(s.getUserId());
            if(sysUserPost!=null){
                if(sysPostMap.containsKey(sysUserPost.getPostId())){
                    SysPost sp = sysPostMap.get(sysUserPost.getPostId());
                    s.setPostName(sp.getPostName());
                }
            }
            if(sysDeptMap.containsKey(s.getDeptId())){
                SysDept sd = sysDeptMap.get(s.getDeptId());
                if(sd!=null){
                    s.setDeptNames(sd.getDeptName());
                }
            }
        }
        return sysUserList;
    }

    @Override
    public String checkRoleNameUnique(ResearchRole researchRole) {
        Long roleId = StringUtils.isNull(researchRole.getRoleId()) ? -1L : researchRole.getRoleId();
        ResearchRole info = researchRoleMapper.checkRoleNameUnique(researchRole.getResearchId(), researchRole.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    @Override
    public String checkRoleKeyUnique(ResearchRole researchRole) {
        Long roleId = StringUtils.isNull(researchRole.getRoleId()) ? -1L : researchRole.getRoleId();
        ResearchRole info = researchRoleMapper.checkRoleKeyUnique(researchRole.getResearchId(), researchRole.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

}
