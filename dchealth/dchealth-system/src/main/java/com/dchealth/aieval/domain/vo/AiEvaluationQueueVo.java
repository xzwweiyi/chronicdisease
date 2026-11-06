package com.dchealth.aieval.domain.vo;

import java.util.Date;

/**
 * AI评估分析-已发布队列视图对象
 *
 * @author dchealth
 * @date 2026-09-02
 */
public class AiEvaluationQueueVo {

    /** 队列ID */
    private Long id;

    /** 队列名称 */
    private String projectName;

    /** 病种名称 */
    private String diseaseName;

    /** 队列简介 */
    private String researchSynopsis;

    /** 目标患者数 */
    private Long patientNum;

    /** 待处理随访任务数 */
    private Integer pendingTasksNum;

    /** 进行中随访任务数 */
    private Integer ongoingTasksNum;

    /** 已完成随访任务数 */
    private Integer completedTasksNum;

    /** 发布时间 */
    private Date publishTime;

    /** 评估数据数量 */
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

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getResearchSynopsis() {
        return researchSynopsis;
    }

    public void setResearchSynopsis(String researchSynopsis) {
        this.researchSynopsis = researchSynopsis;
    }

    public Long getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(Long patientNum) {
        this.patientNum = patientNum;
    }

    public Integer getPendingTasksNum() {
        return pendingTasksNum;
    }

    public void setPendingTasksNum(Integer pendingTasksNum) {
        this.pendingTasksNum = pendingTasksNum;
    }

    public Integer getOngoingTasksNum() {
        return ongoingTasksNum;
    }

    public void setOngoingTasksNum(Integer ongoingTasksNum) {
        this.ongoingTasksNum = ongoingTasksNum;
    }

    public Integer getCompletedTasksNum() {
        return completedTasksNum;
    }

    public void setCompletedTasksNum(Integer completedTasksNum) {
        this.completedTasksNum = completedTasksNum;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Long getDataCount() {
        return dataCount;
    }

    public void setDataCount(Long dataCount) {
        this.dataCount = dataCount;
    }
}
