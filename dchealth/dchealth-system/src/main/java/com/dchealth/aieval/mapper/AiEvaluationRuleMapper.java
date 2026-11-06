package com.dchealth.aieval.mapper;

import java.util.List;
import com.dchealth.aieval.domain.AiEvaluationRule;

public interface AiEvaluationRuleMapper {
    List<AiEvaluationRule> selectAiEvaluationRuleList(AiEvaluationRule entity);
    AiEvaluationRule selectAiEvaluationRuleById(Long id);
    int insertAiEvaluationRule(AiEvaluationRule entity);
    int updateAiEvaluationRule(AiEvaluationRule entity);
    int deleteAiEvaluationRuleById(Long id);
    int deleteAiEvaluationRuleByIds(Long[] ids);
}
