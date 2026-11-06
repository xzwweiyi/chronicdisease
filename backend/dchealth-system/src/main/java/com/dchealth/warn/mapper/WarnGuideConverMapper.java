package com.dchealth.warn.mapper;

import java.util.List;
import com.dchealth.warn.domain.WarnGuideConver;

/**
 * 预警指导会话Mapper接口
 * 
 * @author lchbu
 * @date 2022-10-08
 */
public interface WarnGuideConverMapper 
{
    /**
     * 查询预警指导会话
     * 
     * @param warnGuideConverId 预警指导会话主键
     * @return 预警指导会话
     */
    public WarnGuideConver selectWarnGuideConverByWarnGuideConverId(Long warnGuideConverId);

    /**
     * 查询预警指导会话列表
     * 
     * @param warnGuideConver 预警指导会话
     * @return 预警指导会话集合
     */
    public List<WarnGuideConver> selectWarnGuideConverList(WarnGuideConver warnGuideConver);

    /**
     * 新增预警指导会话
     * 
     * @param warnGuideConver 预警指导会话
     * @return 结果
     */
    public int insertWarnGuideConver(WarnGuideConver warnGuideConver);

    /**
     * 修改预警指导会话
     * 
     * @param warnGuideConver 预警指导会话
     * @return 结果
     */
    public int updateWarnGuideConver(WarnGuideConver warnGuideConver);

    /**
     * 删除预警指导会话
     * 
     * @param warnGuideConverId 预警指导会话主键
     * @return 结果
     */
    public int deleteWarnGuideConverByWarnGuideConverId(Long warnGuideConverId);

    /**
     * 批量删除预警指导会话
     * 
     * @param warnGuideConverIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnGuideConverByWarnGuideConverIds(Long[] warnGuideConverIds);
}
