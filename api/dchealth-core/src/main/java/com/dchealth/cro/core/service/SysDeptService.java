package com.dchealth.cro.core.service;

import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.core.domain.SysDept;
import com.dchealth.cro.core.mapper.SysDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门Service业务层处理
 * 
 * @author lchbu
 * @date 2022-09-13
 */
@Service
public class SysDeptService
{
    @Autowired
    private SysDeptMapper sysDeptMapper;

    /**
     * 查询部门
     * 
     * @param deptId 部门主键
     * @return 部门
     */
    public SysDept selectSysDeptByDeptId(Long deptId)
    {
        return sysDeptMapper.selectSysDeptByDeptId(deptId);
    }

    /**
     * 查询部门列表
     * 
     * @param sysDept 部门
     * @return 部门
     */
    public List<SysDept> selectSysDeptList(SysDept sysDept)
    {
        return sysDeptMapper.selectSysDeptList(sysDept);
    }

    /**
     * 新增部门
     * 
     * @param sysDept 部门
     * @return 结果
     */
    public int insertSysDept(SysDept sysDept)
    {
        sysDept.setCreateTime(DateUtils.getNowDate());
        return sysDeptMapper.insertSysDept(sysDept);
    }

    /**
     * 修改部门
     * 
     * @param sysDept 部门
     * @return 结果
     */
    public int updateSysDept(SysDept sysDept)
    {
        sysDept.setUpdateTime(DateUtils.getNowDate());
        return sysDeptMapper.updateSysDept(sysDept);
    }

    /**
     * 批量删除部门
     * 
     * @param deptIds 需要删除的部门主键
     * @return 结果
     */
    public int deleteSysDeptByDeptIds(Long[] deptIds)
    {
        return sysDeptMapper.deleteSysDeptByDeptIds(deptIds);
    }

    /**
     * 删除部门信息
     * 
     * @param deptId 部门主键
     * @return 结果
     */
    public int deleteSysDeptByDeptId(Long deptId)
    {
        return sysDeptMapper.deleteSysDeptByDeptId(deptId);
    }
}
