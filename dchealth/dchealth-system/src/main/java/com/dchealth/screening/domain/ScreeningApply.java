package com.dchealth.screening.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 筛查申请对象 screening_apply
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ScreeningApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 居民姓名 */
    private String residentName;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 申请日期 */
    private Date applyDate;

    /** 申请类型 */
    private String applyType;

    /** 既往病史 */
    private String diseaseHistory;

    /** 当前用药 */
    private String currentMedication;

    /** 申请原因 */
    private String applyReason;

    /** 审核状态 */
    private String auditStatus;

    /** 审核意见 */
    private String auditOpinion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getDiseaseHistory() {
        return diseaseHistory;
    }

    public void setDiseaseHistory(String diseaseHistory) {
        this.diseaseHistory = diseaseHistory;
    }

    public String getCurrentMedication() {
        return currentMedication;
    }

    public void setCurrentMedication(String currentMedication) {
        this.currentMedication = currentMedication;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    @Override
    public String toString() {
        return "ScreeningApply{id=" + id + "}";
    }
}
