package com.dchealth.datacheck.mapper;

import com.dchealth.datacheck.domain.SysCheckConfig;

import java.util.List;

/**
 * 数据检查配置Mapper接口
 * 
 * @author lchbu
 * @date 2024-05-14
 */
public interface SysCheckConfigMapper 
{
    /**
     * 查询数据检查配置
     * 
     * @param id 数据检查配置主键
     * @return 数据检查配置
     */
    public SysCheckConfig selectSysCheckConfigById(Long id);

    /**
     * 查询数据检查配置列表
     * 
     * @param sysCheckConfig 数据检查配置
     * @return 数据检查配置集合
     */
    public List<SysCheckConfig> selectSysCheckConfigList(SysCheckConfig sysCheckConfig);

    /**
     * 新增数据检查配置
     * 
     * @param sysCheckConfig 数据检查配置
     * @return 结果
     */
    public int insertSysCheckConfig(SysCheckConfig sysCheckConfig);

    /**
     * 修改数据检查配置
     * 
     * @param sysCheckConfig 数据检查配置
     * @return 结果
     */
    public int updateSysCheckConfig(SysCheckConfig sysCheckConfig);

    /**
     * 删除数据检查配置
     * 
     * @param id 数据检查配置主键
     * @return 结果
     */
    public int deleteSysCheckConfigById(Long id);

    /**
     * 批量删除数据检查配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCheckConfigByIds(Long[] ids);
}
