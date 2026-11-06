package com.dchealth.resident.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 居民分级评估记录
 */
public class ResidentGradingRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String personId;
    private Date assessmentDate;
    private String bpClassification;      // 血压分级: I级/II级/III级/正常/正常高值
    private String managementLevel;        // 管理级别: 一级/二级/三级
    private String riskStratification;     // 危险分层: 低危/中危/高危/极高危
    private String cardiovascularFactors;  // 心血管危险因素 JSON数组
    private String targetOrganDamage;      // 靶器官损害 JSON数组
    private String comorbidConditions;     // 并存临床情况 JSON数组
    private String assessor;               // 评估人
    private Date nextAssessmentDate;       // 下次评估日期
    private String remark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public Date getAssessmentDate() { return assessmentDate; }
    public void setAssessmentDate(Date assessmentDate) { this.assessmentDate = assessmentDate; }
    public String getBpClassification() { return bpClassification; }
    public void setBpClassification(String bpClassification) { this.bpClassification = bpClassification; }
    public String getManagementLevel() { return managementLevel; }
    public void setManagementLevel(String managementLevel) { this.managementLevel = managementLevel; }
    public String getRiskStratification() { return riskStratification; }
    public void setRiskStratification(String riskStratification) { this.riskStratification = riskStratification; }
    public String getCardiovascularFactors() { return cardiovascularFactors; }
    public void setCardiovascularFactors(String cardiovascularFactors) { this.cardiovascularFactors = cardiovascularFactors; }
    public String getTargetOrganDamage() { return targetOrganDamage; }
    public void setTargetOrganDamage(String targetOrganDamage) { this.targetOrganDamage = targetOrganDamage; }
    public String getComorbidConditions() { return comorbidConditions; }
    public void setComorbidConditions(String comorbidConditions) { this.comorbidConditions = comorbidConditions; }
    public String getAssessor() { return assessor; }
    public void setAssessor(String assessor) { this.assessor = assessor; }
    public Date getNextAssessmentDate() { return nextAssessmentDate; }
    public void setNextAssessmentDate(Date nextAssessmentDate) { this.nextAssessmentDate = nextAssessmentDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
