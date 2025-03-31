package com.dchealth.datacheck.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.datacheck.domain.SysCheckConfig;
import com.dchealth.datacheck.mapper.SysCheckConfigMapper;
import com.dchealth.datacheck.service.ISysCheckConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据检查配置Service业务层处理
 * 
 * @author lchbu
 * @date 2024-05-14
 */
@Service
public class SysCheckConfigServiceImpl implements ISysCheckConfigService
{
    @Autowired
    private SysCheckConfigMapper sysCheckConfigMapper;

    /**
     * 查询数据检查配置
     * 
     * @param id 数据检查配置主键
     * @return 数据检查配置
     */
    @Override
    public SysCheckConfig selectSysCheckConfigById(Long id)
    {
        return sysCheckConfigMapper.selectSysCheckConfigById(id);
    }

    /**
     * 查询数据检查配置列表
     * 
     * @param sysCheckConfig 数据检查配置
     * @return 数据检查配置
     */
    @Override
    public List<SysCheckConfig> selectSysCheckConfigList(SysCheckConfig sysCheckConfig)
    {
        return sysCheckConfigMapper.selectSysCheckConfigList(sysCheckConfig);
    }

    /**
     * 新增数据检查配置
     * 
     * @param sysCheckConfig 数据检查配置
     * @return 结果
     */
    @Override
    public int insertSysCheckConfig(SysCheckConfig sysCheckConfig)
    {
        sysCheckConfig.setCreateTime(DateUtils.getNowDate());
        return sysCheckConfigMapper.insertSysCheckConfig(sysCheckConfig);
    }

    /**
     * 修改数据检查配置
     * 
     * @param sysCheckConfig 数据检查配置
     * @return 结果
     */
    @Override
    public int updateSysCheckConfig(SysCheckConfig sysCheckConfig)
    {
        return sysCheckConfigMapper.updateSysCheckConfig(sysCheckConfig);
    }

    /**
     * 批量删除数据检查配置
     * 
     * @param ids 需要删除的数据检查配置主键
     * @return 结果
     */
    @Override
    public int deleteSysCheckConfigByIds(Long[] ids)
    {
        return sysCheckConfigMapper.deleteSysCheckConfigByIds(ids);
    }

    /**
     * 删除数据检查配置信息
     * 
     * @param id 数据检查配置主键
     * @return 结果
     */
    @Override
    public int deleteSysCheckConfigById(Long id)
    {
        return sysCheckConfigMapper.deleteSysCheckConfigById(id);
    }
}
