package com.dchealth.telemedicine.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 远程会诊记录对象 telemedicine_consultation
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class TelemedicineConsultation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 会诊编号 */
    private String consultationNo;

    /** 患者ID */
    private Long patientId;

    /** 患者姓名 */
    private String patientName;

    /** 申请人姓名 */
    private String applicantName;

    /** 会诊类型 */
    private String consultationType;

    /** 计划会诊时间 */
    private Date scheduledTime;

    /** 实际会诊时间 */
    private Date actualTime;

    /** 参与人员（多人逗号分隔） */
    private String participants;

    /** 主诉 */
    private String chiefComplaint;

    /** 会诊摘要 */
    private String summary;

    /** 会诊结论 */
    private String conclusion;

    /** 诊疗建议 */
    private String recommendation;

    /** 会诊状态 */
    private String consultationStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsultationNo() {
        return consultationNo;
    }

    public void setConsultationNo(String consultationNo) {
        this.consultationNo = consultationNo;
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

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Date getActualTime() {
        return actualTime;
    }

    public void setActualTime(Date actualTime) {
        this.actualTime = actualTime;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getChiefComplaint() {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getConsultationStatus() {
        return consultationStatus;
    }

    public void setConsultationStatus(String consultationStatus) {
        this.consultationStatus = consultationStatus;
    }

    @Override
    public String toString() {
        return "TelemedicineConsultation{id=" + id + "}";
    }
}
