package com.dchealth.aieval.service;

import java.util.List;
import com.dchealth.aieval.domain.AiEvaluationRule;

/**
 * AI评估规则Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IAiEvaluationRuleService
{
    /**
     * 查询AI评估规则列表
     *
     * @param entity AI评估规则
     * @return AI评估规则集合
     */
    public List<AiEvaluationRule> selectAiEvaluationRuleList(AiEvaluationRule entity);

    /**
     * 查询AI评估规则
     *
     * @param id AI评估规则主键
     * @return AI评估规则
     */
    public AiEvaluationRule selectAiEvaluationRuleById(Long id);

    /**
     * 新增AI评估规则
     *
     * @param entity AI评估规则
     * @return 结果
     */
    public int insertAiEvaluationRule(AiEvaluationRule entity);

    /**
     * 修改AI评估规则
     *
     * @param entity AI评估规则
     * @return 结果
     */
    public int updateAiEvaluationRule(AiEvaluationRule entity);

    /**
     * 删除AI评估规则信息
     *
     * @param id AI评估规则主键
     * @return 结果
     */
    public int deleteAiEvaluationRuleById(Long id);

    /**
     * 批量删除AI评估规则
     *
     * @param ids 需要删除的AI评估规则主键集合
     * @return 结果
     */
    public int deleteAiEvaluationRuleByIds(Long[] ids);
}
