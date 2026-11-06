package com.dchealth.datacheck.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.datacheck.domain.SysDataCheck;
import com.dchealth.datacheck.mapper.SysDataCheckMapper;
import com.dchealth.datacheck.service.ISysDataCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 数据检查Service业务层处理
 * 
 * @author lchbu
 * @date 2024-05-14
 */
@Service
public class SysDataCheckServiceImpl implements ISysDataCheckService
{
    @Autowired
    private SysDataCheckMapper sysDataCheckMapper;

    /**
     * 查询数据检查
     * 
     * @param id 数据检查主键
     * @return 数据检查
     */
    @Override
    public SysDataCheck selectSysDataCheckById(Long id)
    {
        return sysDataCheckMapper.selectSysDataCheckById(id);
    }

    /**
     * 查询数据检查列表
     * 
     * @param sysDataCheck 数据检查
     * @return 数据检查
     */
    @Override
    public List<SysDataCheck> selectSysDataCheckList(SysDataCheck sysDataCheck)
    {
        return sysDataCheckMapper.selectSysDataCheckList(sysDataCheck);
    }

    /**
     * 新增数据检查
     * 
     * @param sysDataCheck 数据检查
     * @return 结果
     */
    @Override
    public int insertSysDataCheck(SysDataCheck sysDataCheck)
    {
        sysDataCheck.setCreateTime(DateUtils.getNowDate());
        return sysDataCheckMapper.insertSysDataCheck(sysDataCheck);
    }

    /**
     * 修改数据检查
     * 
     * @param sysDataCheck 数据检查
     * @return 结果
     */
    @Override
    public int updateSysDataCheck(SysDataCheck sysDataCheck)
    {
        return sysDataCheckMapper.updateSysDataCheck(sysDataCheck);
    }

    /**
     * 批量删除数据检查
     * 
     * @param ids 需要删除的数据检查主键
     * @return 结果
     */
    @Override
    public int deleteSysDataCheckByIds(Long[] ids)
    {
        return sysDataCheckMapper.deleteSysDataCheckByIds(ids);
    }

    /**
     * 删除数据检查信息
     * 
     * @param id 数据检查主键
     * @return 结果
     */
    @Override
    public int deleteSysDataCheckById(Long id)
    {
        return sysDataCheckMapper.deleteSysDataCheckById(id);
    }
}
