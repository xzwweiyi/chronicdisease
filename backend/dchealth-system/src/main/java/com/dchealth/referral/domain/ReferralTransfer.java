package com.dchealth.referral.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 转诊记录对象 referral_transfer
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ReferralTransfer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 患者ID */
    private Long patientId;

    /** 患者姓名 */
    private String patientName;

    /** 转出机构 */
    private String fromInstitution;

    /** 转入机构 */
    private String toInstitution;

    /** 转出科室 */
    private String fromDept;

    /** 转入科室 */
    private String toDept;

    /** 转诊类型 */
    private String transferType;

    /** 转诊日期 */
    private Date transferDate;

    /** 转诊原因 */
    private String reason;

    /** 诊断信息 */
    private String diagnosis;

    /** 陪同信息 */
    private String accompanyingInfo;

    /** 转诊状态 */
    private String transferStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFromInstitution() {
        return fromInstitution;
    }

    public void setFromInstitution(String fromInstitution) {
        this.fromInstitution = fromInstitution;
    }

    public String getToInstitution() {
        return toInstitution;
    }

    public void setToInstitution(String toInstitution) {
        this.toInstitution = toInstitution;
    }

    public String getFromDept() {
        return fromDept;
    }

    public void setFromDept(String fromDept) {
        this.fromDept = fromDept;
    }

    public String getToDept() {
        return toDept;
    }

    public void setToDept(String toDept) {
        this.toDept = toDept;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public Date getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getAccompanyingInfo() {
        return accompanyingInfo;
    }

    public void setAccompanyingInfo(String accompanyingInfo) {
        this.accompanyingInfo = accompanyingInfo;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    @Override
    public String toString() {
        return "ReferralTransfer{id=" + id + "}";
    }
}
