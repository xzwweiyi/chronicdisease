package com.dchealth.aieval.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * AI评估规则对象 ai_evaluation_rule
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class AiEvaluationRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 规则名称 */
    private String ruleName;

    /** 规则编码 */
    private String ruleCode;

    /** 所属项目ID */
    private Long projectId;

    /** 规则表达式 */
    private String ruleExpression;

    /** 风险等级 */
    private String riskLevel;

    /** 分值区间 */
    private String scoreRange;

    /** 处置建议 */
    private String advice;

    /** 优先级 */
    private Integer priority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRuleExpression() {
        return ruleExpression;
    }

    public void setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getScoreRange() {
        return scoreRange;
    }

    public void setScoreRange(String scoreRange) {
        this.scoreRange = scoreRange;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "AiEvaluationRule{id=" + id + "}";
    }
}
