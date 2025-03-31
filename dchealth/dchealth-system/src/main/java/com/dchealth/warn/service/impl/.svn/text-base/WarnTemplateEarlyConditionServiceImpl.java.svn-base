package com.dchealth.warn.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.warn.mapper.WarnTemplateEarlyConditionMapper;
import com.dchealth.warn.domain.WarnTemplateEarlyCondition;
import com.dchealth.warn.service.IWarnTemplateEarlyConditionService;

/**
 * 预警模板子表Service业务层处理
 * 
 * @author lchbu
 * @date 2023-01-09
 */
@Service
public class WarnTemplateEarlyConditionServiceImpl implements IWarnTemplateEarlyConditionService 
{
    @Autowired
    private WarnTemplateEarlyConditionMapper warnTemplateEarlyConditionMapper;

    /**
     * 查询预警模板子表
     * 
     * @param id 预警模板子表主键
     * @return 预警模板子表
     */
    @Override
    public WarnTemplateEarlyCondition selectWarnTemplateEarlyConditionById(Long id)
    {
        return warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionById(id);
    }

    /**
     * 查询预警模板子表列表
     * 
     * @param warnTemplateEarlyCondition 预警模板子表
     * @return 预警模板子表
     */
    @Override
    public List<WarnTemplateEarlyCondition> selectWarnTemplateEarlyConditionList(WarnTemplateEarlyCondition warnTemplateEarlyCondition)
    {
        return warnTemplateEarlyConditionMapper.selectWarnTemplateEarlyConditionList(warnTemplateEarlyCondition);
    }

    /**
     * 新增预警模板子表
     * 
     * @param warnTemplateEarlyCondition 预警模板子表
     * @return 结果
     */
    @Override
    public int insertWarnTemplateEarlyCondition(WarnTemplateEarlyCondition warnTemplateEarlyCondition)
    {
        warnTemplateEarlyCondition.setCreateTime(DateUtils.getNowDate());
        return warnTemplateEarlyConditionMapper.insertWarnTemplateEarlyCondition(warnTemplateEarlyCondition);
    }

    /**
     * 修改预警模板子表
     * 
     * @param warnTemplateEarlyCondition 预警模板子表
     * @return 结果
     */
    @Override
    public int updateWarnTemplateEarlyCondition(WarnTemplateEarlyCondition warnTemplateEarlyCondition)
    {
        return warnTemplateEarlyConditionMapper.updateWarnTemplateEarlyCondition(warnTemplateEarlyCondition);
    }

    /**
     * 批量删除预警模板子表
     * 
     * @param ids 需要删除的预警模板子表主键
     * @return 结果
     */
    @Override
    public int deleteWarnTemplateEarlyConditionByIds(Long[] ids)
    {
        return warnTemplateEarlyConditionMapper.deleteWarnTemplateEarlyConditionByIds(ids);
    }

    /**
     * 删除预警模板子表信息
     * 
     * @param id 预警模板子表主键
     * @return 结果
     */
    @Override
    public int deleteWarnTemplateEarlyConditionById(Long id)
    {
        return warnTemplateEarlyConditionMapper.deleteWarnTemplateEarlyConditionById(id);
    }
}
