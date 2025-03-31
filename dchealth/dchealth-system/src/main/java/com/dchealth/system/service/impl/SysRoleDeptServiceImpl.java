package com.dchealth.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.SysRoleDeptMapper;
import com.dchealth.system.service.ISysRoleDeptService;

/**
 * 角色和部门关联Service业务层处理
 * 
 * @author lchbu
 * @date 2023-04-20
 */
@Service
public class SysRoleDeptServiceImpl implements ISysRoleDeptService 
{
    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;


    @Override
    public String getDeptIdsByUserId(Long userId) {
        return sysRoleDeptMapper.getDeptIdsByUserId(userId);
    }
}
