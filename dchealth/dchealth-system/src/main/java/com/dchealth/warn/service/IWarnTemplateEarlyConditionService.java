package com.dchealth.warn.service;

import java.util.List;
import com.dchealth.warn.domain.WarnTemplateEarlyCondition;

/**
 * 预警模板子表Service接口
 * 
 * @author lchbu
 * @date 2023-01-09
 */
public interface IWarnTemplateEarlyConditionService 
{
    /**
     * 查询预警模板子表
     * 
     * @param id 预警模板子表主键
     * @return 预警模板子表
     */
    public WarnTemplateEarlyCondition selectWarnTemplateEarlyConditionById(Long id);

    /**
     * 查询预警模板子表列表
     * 
     * @param warnTemplateEarlyCondition 预警模板子表
     * @return 预警模板子表集合
     */
    public List<WarnTemplateEarlyCondition> selectWarnTemplateEarlyConditionList(WarnTemplateEarlyCondition warnTemplateEarlyCondition);

    /**
     * 新增预警模板子表
     * 
     * @param warnTemplateEarlyCondition 预警模板子表
     * @return 结果
     */
    public int insertWarnTemplateEarlyCondition(WarnTemplateEarlyCondition warnTemplateEarlyCondition);

    /**
     * 修改预警模板子表
     * 
     * @param warnTemplateEarlyCondition 预警模板子表
     * @return 结果
     */
    public int updateWarnTemplateEarlyCondition(WarnTemplateEarlyCondition warnTemplateEarlyCondition);

    /**
     * 批量删除预警模板子表
     * 
     * @param ids 需要删除的预警模板子表主键集合
     * @return 结果
     */
    public int deleteWarnTemplateEarlyConditionByIds(Long[] ids);

    /**
     * 删除预警模板子表信息
     * 
     * @param id 预警模板子表主键
     * @return 结果
     */
    public int deleteWarnTemplateEarlyConditionById(Long id);
}
