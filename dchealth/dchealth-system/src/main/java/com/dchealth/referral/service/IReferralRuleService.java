package com.dchealth.referral.service;

import java.util.List;
import com.dchealth.referral.domain.ReferralRule;

/**
 * 转诊规则Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IReferralRuleService
{
    /**
     * 查询转诊规则列表
     *
     * @param entity 转诊规则
     * @return 转诊规则集合
     */
    public List<ReferralRule> selectReferralRuleList(ReferralRule entity);

    /**
     * 查询转诊规则
     *
     * @param id 转诊规则主键
     * @return 转诊规则
     */
    public ReferralRule selectReferralRuleById(Long id);

    /**
     * 新增转诊规则
     *
     * @param entity 转诊规则
     * @return 结果
     */
    public int insertReferralRule(ReferralRule entity);

    /**
     * 修改转诊规则
     *
     * @param entity 转诊规则
     * @return 结果
     */
    public int updateReferralRule(ReferralRule entity);

    /**
     * 删除转诊规则信息
     *
     * @param id 转诊规则主键
     * @return 结果
     */
    public int deleteReferralRuleById(Long id);

    /**
     * 批量删除转诊规则
     *
     * @param ids 需要删除的转诊规则主键集合
     * @return 结果
     */
    public int deleteReferralRuleByIds(Long[] ids);
}
