package com.dchealth.warn.service;

import java.util.List;
import com.dchealth.warn.domain.WarnConfig;

/**
 * 预警信息Service接口
 * 
 * @author lchbu
 * @date 2022-08-30
 */
public interface IWarnConfigService 
{
    /**
     * 查询预警信息
     * 
     * @param id 预警信息主键
     * @return 预警信息
     */
    public WarnConfig selectWarnConfigById(Long id);

    /**
     * 查询预警信息列表
     * 
     * @param warnConfig 预警信息
     * @return 预警信息集合
     */
    public List<WarnConfig> selectWarnConfigList(WarnConfig warnConfig);

    /**
     * 新增预警信息
     * 
     * @param warnConfig 预警信息
     * @return 结果
     */
    public int insertWarnConfig(WarnConfig warnConfig);

    /**
     * 修改预警信息
     * 
     * @param warnConfig 预警信息
     * @return 结果
     */
    public int updateWarnConfig(WarnConfig warnConfig);

    /**
     * 批量删除预警信息
     * 
     * @param ids 需要删除的预警信息主键集合
     * @return 结果
     */
    public int deleteWarnConfigByIds(Long[] ids);

    /**
     * 删除预警信息信息
     * 
     * @param id 预警信息主键
     * @return 结果
     */
    public int deleteWarnConfigById(Long id);
}
