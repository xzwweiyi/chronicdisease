package com.dchealth.screening.domain.vo;

import java.util.Date;

/**
 * 筛查患者VO — 合并 mp_person + 各筛查表字段
 * 用于筛查列表页展示，患者人口学信息来源于 mp_person
 *
 * @author dchealth
 * @date 2026-07-29
 */
public class ScreeningPatientVO {

    // ========== mp_person 基础字段 ==========
    private String personId;
    private String personName;
    private String cardId;
    private String genderSourceValue;
    private String sex;
    private String phoneNumber;
    private Date mpCreateTime;
    private Date mpUpdateTime;

    // ========== 筛查记录通用字段（id 指向各筛查表主键） ==========
    private Long id;
    private String idCard;
    private String phone;
    private Date createTime;
    private Date updateTime;
    private String remark;

    // ========== 各筛查类型通过额外属性承载，共用此 VO ==========
    // --- screening_apply ---
    private Date applyDate;
    private String applyType;
    private String diseaseHistory;
    private String currentMedication;
    private String applyReason;
    private String auditStatus;
    private String auditOpinion;

    // --- screening_clinic ---
    private Date visitDate;
    private String department;
    private String diagnosis;
    private String icdCode;
    private String diseaseType;
    private String doctorName;

    // --- screening_outside ---
    private Date screeningDate;
    private String screeningLocation;
    private String screeningResult;
    private String handler;

    // --- screening_physical ---
    private Date examDate;
    private String examInstitution;
    private String bmi;
    private String bloodPressure;
    private String bloodSugar;
    private String bloodLipids;
    private String examResult;
    private String diseaseRisk;

    // ========== Getters and Setters ==========

    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }
    public String getGenderSourceValue() { return genderSourceValue; }
    public void setGenderSourceValue(String genderSourceValue) { this.genderSourceValue = genderSourceValue; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public Date getMpCreateTime() { return mpCreateTime; }
    public void setMpCreateTime(Date mpCreateTime) { this.mpCreateTime = mpCreateTime; }
    public Date getMpUpdateTime() { return mpUpdateTime; }
    public void setMpUpdateTime(Date mpUpdateTime) { this.mpUpdateTime = mpUpdateTime; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getApplyDate() { return applyDate; }
    public void setApplyDate(Date applyDate) { this.applyDate = applyDate; }
    public String getApplyType() { return applyType; }
    public void setApplyType(String applyType) { this.applyType = applyType; }
    public String getDiseaseHistory() { return diseaseHistory; }
    public void setDiseaseHistory(String diseaseHistory) { this.diseaseHistory = diseaseHistory; }
    public String getCurrentMedication() { return currentMedication; }
    public void setCurrentMedication(String currentMedication) { this.currentMedication = currentMedication; }
    public String getApplyReason() { return applyReason; }
    public void setApplyReason(String applyReason) { this.applyReason = applyReason; }
    public String getAuditStatus() { return auditStatus; }
    public void setAuditStatus(String auditStatus) { this.auditStatus = auditStatus; }
    public String getAuditOpinion() { return auditOpinion; }
    public void setAuditOpinion(String auditOpinion) { this.auditOpinion = auditOpinion; }

    public Date getVisitDate() { return visitDate; }
    public void setVisitDate(Date visitDate) { this.visitDate = visitDate; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getIcdCode() { return icdCode; }
    public void setIcdCode(String icdCode) { this.icdCode = icdCode; }
    public String getDiseaseType() { return diseaseType; }
    public void setDiseaseType(String diseaseType) { this.diseaseType = diseaseType; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public Date getScreeningDate() { return screeningDate; }
    public void setScreeningDate(Date screeningDate) { this.screeningDate = screeningDate; }
    public String getScreeningLocation() { return screeningLocation; }
    public void setScreeningLocation(String screeningLocation) { this.screeningLocation = screeningLocation; }
    public String getScreeningResult() { return screeningResult; }
    public void setScreeningResult(String screeningResult) { this.screeningResult = screeningResult; }
    public String getHandler() { return handler; }
    public void setHandler(String handler) { this.handler = handler; }

    public Date getExamDate() { return examDate; }
    public void setExamDate(Date examDate) { this.examDate = examDate; }
    public String getExamInstitution() { return examInstitution; }
    public void setExamInstitution(String examInstitution) { this.examInstitution = examInstitution; }
    public String getBmi() { return bmi; }
    public void setBmi(String bmi) { this.bmi = bmi; }
    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    public String getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(String bloodSugar) { this.bloodSugar = bloodSugar; }
    public String getBloodLipids() { return bloodLipids; }
    public void setBloodLipids(String bloodLipids) { this.bloodLipids = bloodLipids; }
    public String getExamResult() { return examResult; }
    public void setExamResult(String examResult) { this.examResult = examResult; }
    public String getDiseaseRisk() { return diseaseRisk; }
    public void setDiseaseRisk(String diseaseRisk) { this.diseaseRisk = diseaseRisk; }
}
