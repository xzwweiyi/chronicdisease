package com.dchealth.warn.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.warn.mapper.WarnConfigMapper;
import com.dchealth.warn.domain.WarnConfig;
import com.dchealth.warn.service.IWarnConfigService;

/**
 * 预警信息Service业务层处理
 * 
 * @author lchbu
 * @date 2022-08-30
 */
@Service
public class WarnConfigServiceImpl implements IWarnConfigService 
{
    @Autowired
    private WarnConfigMapper warnConfigMapper;

    /**
     * 查询预警信息
     * 
     * @param id 预警信息主键
     * @return 预警信息
     */
    @Override
    public WarnConfig selectWarnConfigById(Long id)
    {
        return warnConfigMapper.selectWarnConfigById(id);
    }

    /**
     * 查询预警信息列表
     * 
     * @param warnConfig 预警信息
     * @return 预警信息
     */
    @Override
    public List<WarnConfig> selectWarnConfigList(WarnConfig warnConfig)
    {
        return warnConfigMapper.selectWarnConfigList(warnConfig);
    }

    /**
     * 新增预警信息
     * 
     * @param warnConfig 预警信息
     * @return 结果
     */
    @Override
    public int insertWarnConfig(WarnConfig warnConfig)
    {
        warnConfig.setCreateTime(DateUtils.getNowDate());
        return warnConfigMapper.insertWarnConfig(warnConfig);
    }

    /**
     * 修改预警信息
     * 
     * @param warnConfig 预警信息
     * @return 结果
     */
    @Override
    public int updateWarnConfig(WarnConfig warnConfig)
    {
        warnConfig.setUpdateTime(DateUtils.getNowDate());
        return warnConfigMapper.updateWarnConfig(warnConfig);
    }

    /**
     * 批量删除预警信息
     * 
     * @param ids 需要删除的预警信息主键
     * @return 结果
     */
    @Override
    public int deleteWarnConfigByIds(Long[] ids)
    {
        return warnConfigMapper.deleteWarnConfigByIds(ids);
    }

    /**
     * 删除预警信息信息
     * 
     * @param id 预警信息主键
     * @return 结果
     */
    @Override
    public int deleteWarnConfigById(Long id)
    {
        return warnConfigMapper.deleteWarnConfigById(id);
    }
}
