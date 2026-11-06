package com.dchealth.research.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.dchealth.common.core.domain.entity.SysDept;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.research.domain.ResearchHospital;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.ResearchRole;
import com.dchealth.research.mapper.*;
import com.dchealth.research.service.IResearchHospitalService;
import com.dchealth.system.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResearchHospitalServiceImpl implements IResearchHospitalService {

    @Autowired
    ResearchHospitalMapper researchHospitalMapper;

    @Autowired
    ResearchInfoMapper researchInfoMapper;

    @Autowired
    ResearchRoleMapper researchRoleMapper;

    @Autowired
    SysDeptMapper sysDeptMapper;

    @Autowired
    ResearchRoleDeptMapper researchRoleDeptMapper;

    @Autowired
    ResearchRoleAuthMapper researchRoleAuthMapper;


    @Override
    public List<ResearchHospital> selectResearchHospital(ResearchHospital researhHospital) {
        return researchHospitalMapper.selectByResearchHospital(researhHospital);
    }

    @Override
    public int insertResearchHospital(ResearchHospital researchHospital) {
        Long researchId = researchHospital.getResearchId();
        Long deptId = researchHospital.getDeptId();
        List<ResearchHospital> researchHospitals = researchHospitalMapper.findResearchHospitalByResearchIdAndDeptId(researchId, deptId);
        if (researchHospitals.size() > 0) {
            throw new ServiceException(String.format("队列参与中心不能重复！"));
        }
        return researchHospitalMapper.insertResearchHospital(researchHospital);
    }

    @Override
    public int updateResearchHospital(ResearchHospital researchHospital) {
        List<ResearchHospital> researchHospitals = researchHospitalMapper.findResearchHospitalByResearchIdAndDeptId(researchHospital.getResearchId(), researchHospital.getDeptId());
        if (researchHospitals.size() > 0) {
            for (ResearchHospital hospital : researchHospitals) {
                if (researchHospital.getId() != hospital.getId()) {
                    throw new ServiceException(String.format("队列参与中心不能重复！"));
                }
            }
        }
        return researchHospitalMapper.updateResearchHospital(researchHospital);
    }

    @Transactional
    @Override
    public int deleteResearchHospitalByIds(List<Long> ids) {
        List<ResearchHospital> researchHospitalByIds = researchHospitalMapper.findResearchHospitalByIds(ids);
        for (ResearchHospital researchHospitalById : researchHospitalByIds) {
            Long researchId = researchHospitalById.getResearchId();//队列id
            Long deptId = researchHospitalById.getDeptId();//医院中心id
            //根据队列查询角色
            List<ResearchRole> researchRoles = researchRoleMapper.findResearchRoleByResearchId(researchId);
            if (researchRoles.size() > 0) {
                //判断队列是本中心还是外院
                ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
                if (researchInfo != null) {
                    String researhType = researchInfo.getResearchType();
                    if ("1".equals(researhType)) {
                        List<SysDept> sysDepts = sysDeptMapper.findDeptListByAncestors(deptId);
                        List<Long> deptIds = sysDepts.stream().map(SysDept::getDeptId).distinct().collect(Collectors.toList());
                        deptIds.add(deptId);
                        for (ResearchRole researchRole : researchRoles) {
                            //删除角色-部门
                            researchRoleDeptMapper.deleteRoleDeptByRoleIdAndDeptIds(researchRole.getRoleId(), deptIds);
                            //删除角色- 部门- 权限
                            researchRoleAuthMapper.deleteRoleDeptAuthByRoleIdAndDeptIds(researchRole.getRoleId(), deptIds);
                        }
                    } else if ("2".equals(researhType)) {
                        for (ResearchRole researchRole : researchRoles) {
                            //删除角色-部门
                            researchRoleDeptMapper.deleteRoleDeptByRoleIdAndDeptId(researchRole.getRoleId(), deptId);
                            //删除角色- 部门- 权限
                            researchRoleAuthMapper.deleteRoleDeptAuthByRoleIdAndDeptId(researchRole.getRoleId(), deptId);
                        }
                    }
                }
            }
        }
        return researchHospitalMapper.deleteResearchHospitalByIds(ids);
    }

    @Override
    public List<JSONObject> findHospitalDict() {
        List<JSONObject> sysDeptVos = new ArrayList<>();
        //查询医院中心
        List<SysDept> sysDepts = sysDeptMapper.findDeptByDeptType("1");
        if (sysDepts.size() > 0) {
            for (SysDept sysDept : sysDepts) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("deptId", sysDept.getDeptId());
                jsonObject.put("deptName", sysDept.getDeptName());
                sysDeptVos.add(jsonObject);
            }
        }
        return sysDeptVos;
    }

    @Override
    public List<JSONObject> findResearchInfoDict() {
        List<JSONObject> researchInfoList = new ArrayList<>();
        List<ResearchInfo> researchInfos = researchInfoMapper.selectResearchInfoList(new ResearchInfo());
        if (researchInfos.size() > 0) {
            for (ResearchInfo researchInfo : researchInfos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("researchId", researchInfo.getId());
                jsonObject.put("researchName", researchInfo.getProjectName());
                researchInfoList.add(jsonObject);
            }
        }
        return researchInfoList;
    }
}
