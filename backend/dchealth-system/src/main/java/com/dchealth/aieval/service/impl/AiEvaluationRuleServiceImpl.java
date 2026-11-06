package com.dchealth.aieval.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.aieval.domain.AiEvaluationRule;
import com.dchealth.aieval.mapper.AiEvaluationRuleMapper;
import com.dchealth.aieval.service.IAiEvaluationRuleService;

/**
 * AI评估规则Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class AiEvaluationRuleServiceImpl implements IAiEvaluationRuleService
{
    @Autowired
    private AiEvaluationRuleMapper aiEvaluationRuleMapper;

    /**
     * 查询AI评估规则列表
     *
     * @param entity AI评估规则
     * @return AI评估规则集合
     */
    @Override
    public List<AiEvaluationRule> selectAiEvaluationRuleList(AiEvaluationRule entity)
    {
        return aiEvaluationRuleMapper.selectAiEvaluationRuleList(entity);
    }

    /**
     * 查询AI评估规则
     *
     * @param id AI评估规则主键
     * @return AI评估规则
     */
    @Override
    public AiEvaluationRule selectAiEvaluationRuleById(Long id)
    {
        return aiEvaluationRuleMapper.selectAiEvaluationRuleById(id);
    }

    /**
     * 新增AI评估规则
     *
     * @param entity AI评估规则
     * @return 结果
     */
    @Override
    public int insertAiEvaluationRule(AiEvaluationRule entity)
    {
        return aiEvaluationRuleMapper.insertAiEvaluationRule(entity);
    }

    /**
     * 修改AI评估规则
     *
     * @param entity AI评估规则
     * @return 结果
     */
    @Override
    public int updateAiEvaluationRule(AiEvaluationRule entity)
    {
        return aiEvaluationRuleMapper.updateAiEvaluationRule(entity);
    }

    /**
     * 删除AI评估规则信息
     *
     * @param id AI评估规则主键
     * @return 结果
     */
    @Override
    public int deleteAiEvaluationRuleById(Long id)
    {
        return aiEvaluationRuleMapper.deleteAiEvaluationRuleById(id);
    }

    /**
     * 批量删除AI评估规则
     *
     * @param ids 需要删除的AI评估规则主键集合
     * @return 结果
     */
    @Override
    public int deleteAiEvaluationRuleByIds(Long[] ids)
    {
        return aiEvaluationRuleMapper.deleteAiEvaluationRuleByIds(ids);
    }
}
