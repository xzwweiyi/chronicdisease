package com.dchealth.aieval.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * AI评估项目对象 ai_evaluation_project
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class AiEvaluationProject extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 项目名称 */
    private String projectName;

    /** 项目编码 */
    private String projectCode;

    /** 病种类型 */
    private String diseaseType;

    /** 评估类型 */
    private String evaluationType;

    /** 项目描述 */
    private String description;

    /** 模型配置（JSON格式） */
    private String modelConfig;

    /** 规则配置（JSON格式） */
    private String ruleConfig;

    /** 输入字段（逗号分隔） */
    private String inputFields;

    /** 输出字段（逗号分隔） */
    private String outputFields;

    /** 评估数据数量（统计用，非表字段） */
    private Long dataCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public void setEvaluationType(String evaluationType) {
        this.evaluationType = evaluationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(String modelConfig) {
        this.modelConfig = modelConfig;
    }

    public String getRuleConfig() {
        return ruleConfig;
    }

    public void setRuleConfig(String ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public String getInputFields() {
        return inputFields;
    }

    public void setInputFields(String inputFields) {
        this.inputFields = inputFields;
    }

    public String getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(String outputFields) {
        this.outputFields = outputFields;
    }

    public Long getDataCount() {
        return dataCount;
    }

    public void setDataCount(Long dataCount) {
        this.dataCount = dataCount;
    }

    @Override
    public String toString() {
        return "AiEvaluationProject{id=" + id + "}";
    }
}
