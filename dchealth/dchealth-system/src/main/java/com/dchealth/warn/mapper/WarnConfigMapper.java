package com.dchealth.warn.mapper;

import java.util.List;
import com.dchealth.warn.domain.WarnConfig;

/**
 * 预警信息Mapper接口
 * 
 * @author lchbu
 * @date 2022-08-30
 */
public interface WarnConfigMapper 
{
    /**
     * 查询预警信息
     * 
     * @param id 预警信息主键
     * @return 预警信息
     */
    public WarnConfig selectWarnConfigById(Long id);

    /**
     * 根据随访节点id查询
     * @param id
     * @return
     */
    public List<WarnConfig> selectWarnConfigByFollowId(Long id);

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
     * 批量
     * @param list
     * @return
     */
    public int batchInsertWarnConfig(List<WarnConfig> list);

    /**
     * 修改预警信息
     * 
     * @param warnConfig 预警信息
     * @return 结果
     */
    public int updateWarnConfig(WarnConfig warnConfig);

    /**
     * 删除预警信息
     * 
     * @param id 预警信息主键
     * @return 结果
     */
    public int deleteWarnConfigById(Long id);

    /**
     * 根据followId删除
     * @param followId
     * @return
     */
    public int deleteWarnConfigByByFollowId(Long followId);

    /**
     * 批量删除预警信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnConfigByIds(Long[] ids);
}
