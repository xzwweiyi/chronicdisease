package com.dchealth.warn.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.warn.mapper.WarnMedicalEarlyConditionMapper;
import com.dchealth.warn.domain.WarnMedicalEarlyCondition;
import com.dchealth.warn.service.IWarnMedicalEarlyConditionService;

/**
 * 预警表单子表Service业务层处理
 * 
 * @author lchbu
 * @date 2023-01-09
 */
@Service
public class WarnMedicalEarlyConditionServiceImpl implements IWarnMedicalEarlyConditionService 
{
    @Autowired
    private WarnMedicalEarlyConditionMapper warnMedicalEarlyConditionMapper;

    /**
     * 查询预警表单子表
     * 
     * @param id 预警表单子表主键
     * @return 预警表单子表
     */
    @Override
    public WarnMedicalEarlyCondition selectWarnMedicalEarlyConditionById(Long id)
    {
        return warnMedicalEarlyConditionMapper.selectWarnMedicalEarlyConditionById(id);
    }

    /**
     * 查询预警表单子表列表
     * 
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 预警表单子表
     */
    @Override
    public List<WarnMedicalEarlyCondition> selectWarnMedicalEarlyConditionList(WarnMedicalEarlyCondition warnMedicalEarlyCondition)
    {
        return warnMedicalEarlyConditionMapper.selectWarnMedicalEarlyConditionList(warnMedicalEarlyCondition);
    }

    /**
     * 新增预警表单子表
     * 
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 结果
     */
    @Override
    public int insertWarnMedicalEarlyCondition(WarnMedicalEarlyCondition warnMedicalEarlyCondition)
    {
        warnMedicalEarlyCondition.setCreateTime(DateUtils.getNowDate());
        return warnMedicalEarlyConditionMapper.insertWarnMedicalEarlyCondition(warnMedicalEarlyCondition);
    }

    /**
     * 修改预警表单子表
     * 
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 结果
     */
    @Override
    public int updateWarnMedicalEarlyCondition(WarnMedicalEarlyCondition warnMedicalEarlyCondition)
    {
        return warnMedicalEarlyConditionMapper.updateWarnMedicalEarlyCondition(warnMedicalEarlyCondition);
    }

    /**
     * 批量删除预警表单子表
     * 
     * @param ids 需要删除的预警表单子表主键
     * @return 结果
     */
    @Override
    public int deleteWarnMedicalEarlyConditionByIds(Long[] ids)
    {
        return warnMedicalEarlyConditionMapper.deleteWarnMedicalEarlyConditionByIds(ids);
    }

    /**
     * 删除预警表单子表信息
     * 
     * @param id 预警表单子表主键
     * @return 结果
     */
    @Override
    public int deleteWarnMedicalEarlyConditionById(Long id)
    {
        return warnMedicalEarlyConditionMapper.deleteWarnMedicalEarlyConditionById(id);
    }
}
