package com.dchealth.datacheck.service;

import com.dchealth.datacheck.domain.SysDataCheck;

import java.util.List;

/**
 * 数据检查Service接口
 * 
 * @author lchbu
 * @date 2024-05-14
 */
public interface ISysDataCheckService 
{
    /**
     * 查询数据检查
     * 
     * @param id 数据检查主键
     * @return 数据检查
     */
    public SysDataCheck selectSysDataCheckById(Long id);

    /**
     * 查询数据检查列表
     * 
     * @param sysDataCheck 数据检查
     * @return 数据检查集合
     */
    public List<SysDataCheck> selectSysDataCheckList(SysDataCheck sysDataCheck);

    /**
     * 新增数据检查
     * 
     * @param sysDataCheck 数据检查
     * @return 结果
     */
    public int insertSysDataCheck(SysDataCheck sysDataCheck);

    /**
     * 修改数据检查
     * 
     * @param sysDataCheck 数据检查
     * @return 结果
     */
    public int updateSysDataCheck(SysDataCheck sysDataCheck);

    /**
     * 批量删除数据检查
     * 
     * @param ids 需要删除的数据检查主键集合
     * @return 结果
     */
    public int deleteSysDataCheckByIds(Long[] ids);

    /**
     * 删除数据检查信息
     * 
     * @param id 数据检查主键
     * @return 结果
     */
    public int deleteSysDataCheckById(Long id);
}
