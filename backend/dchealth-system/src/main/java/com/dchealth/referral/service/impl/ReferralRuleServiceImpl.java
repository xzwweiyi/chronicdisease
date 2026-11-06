package com.dchealth.referral.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.referral.domain.ReferralRule;
import com.dchealth.referral.mapper.ReferralRuleMapper;
import com.dchealth.referral.service.IReferralRuleService;

/**
 * 转诊规则Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class ReferralRuleServiceImpl implements IReferralRuleService
{
    @Autowired
    private ReferralRuleMapper referralRuleMapper;

    /**
     * 查询转诊规则列表
     *
     * @param entity 转诊规则
     * @return 转诊规则集合
     */
    @Override
    public List<ReferralRule> selectReferralRuleList(ReferralRule entity)
    {
        return referralRuleMapper.selectReferralRuleList(entity);
    }

    /**
     * 查询转诊规则
     *
     * @param id 转诊规则主键
     * @return 转诊规则
     */
    @Override
    public ReferralRule selectReferralRuleById(Long id)
    {
        return referralRuleMapper.selectReferralRuleById(id);
    }

    /**
     * 新增转诊规则
     *
     * @param entity 转诊规则
     * @return 结果
     */
    @Override
    public int insertReferralRule(ReferralRule entity)
    {
        return referralRuleMapper.insertReferralRule(entity);
    }

    /**
     * 修改转诊规则
     *
     * @param entity 转诊规则
     * @return 结果
     */
    @Override
    public int updateReferralRule(ReferralRule entity)
    {
        return referralRuleMapper.updateReferralRule(entity);
    }

    /**
     * 删除转诊规则信息
     *
     * @param id 转诊规则主键
     * @return 结果
     */
    @Override
    public int deleteReferralRuleById(Long id)
    {
        return referralRuleMapper.deleteReferralRuleById(id);
    }

    /**
     * 批量删除转诊规则
     *
     * @param ids 需要删除的转诊规则主键集合
     * @return 结果
     */
    @Override
    public int deleteReferralRuleByIds(Long[] ids)
    {
        return referralRuleMapper.deleteReferralRuleByIds(ids);
    }
}
