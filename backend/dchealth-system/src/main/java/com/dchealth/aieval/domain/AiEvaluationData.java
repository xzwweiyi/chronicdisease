package com.dchealth.aieval.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * AI评估数据对象 ai_evaluation_data
 *
 * @author dchealth
 * @date 2026-09-02
 */
public class AiEvaluationData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 所属评估项目ID */
    private Long projectId;

    /** 患者ID */
    private Long patientId;

    /** 患者姓名 */
    private String patientName;

    /** 评估日期 */
    private Date evalDate;

    /** 评估结果(高风险/中风险/低风险) */
    private String evalResult;

    /** 评估评分 */
    private Integer score;

    /** 评估人 */
    private String evaluator;

    /** 评估明细数据(各项指标描述) */
    private String evalDetail;

    /** AI分析结果 */
    private String aiAnalysis;

    /** 下一步采取措施 */
    private String aiMeasures;

    /** 分析所用模型 */
    private String aiModel;

    /** 分析状态 0未分析 1分析中 2已完成 3失败 */
    private String aiStatus;

    /** 分析时间 */
    private Date aiTime;

    /** 状态(0正常 1停用) */
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Date getEvalDate() {
        return evalDate;
    }

    public void setEvalDate(Date evalDate) {
        this.evalDate = evalDate;
    }

    public String getEvalResult() {
        return evalResult;
    }

    public void setEvalResult(String evalResult) {
        this.evalResult = evalResult;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public String getEvalDetail() {
        return evalDetail;
    }

    public void setEvalDetail(String evalDetail) {
        this.evalDetail = evalDetail;
    }

    public String getAiAnalysis() {
        return aiAnalysis;
    }

    public void setAiAnalysis(String aiAnalysis) {
        this.aiAnalysis = aiAnalysis;
    }

    public String getAiMeasures() {
        return aiMeasures;
    }

    public void setAiMeasures(String aiMeasures) {
        this.aiMeasures = aiMeasures;
    }

    public String getAiModel() {
        return aiModel;
    }

    public void setAiModel(String aiModel) {
        this.aiModel = aiModel;
    }

    public String getAiStatus() {
        return aiStatus;
    }

    public void setAiStatus(String aiStatus) {
        this.aiStatus = aiStatus;
    }

    public Date getAiTime() {
        return aiTime;
    }

    public void setAiTime(Date aiTime) {
        this.aiTime = aiTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
