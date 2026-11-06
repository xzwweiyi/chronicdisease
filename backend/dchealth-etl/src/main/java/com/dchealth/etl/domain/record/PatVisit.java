package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 病人住院主记录(PatVisit)实体类
 *
 * @author makejava
 * @since 2023-04-13 09:26:57
 */
public class PatVisit extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -99509735848695262L;
    /**
     * 病人标识
     */
    private String patientId;
    /**
     * 病人本次住院标识
     */
    private Long visitId;
    /**
     * 入院科室
     */
    private String deptAdmissionTo;
    /**
     * 入院日期及时间
     */
    private Date admissionDateTime;
    /**
     * 出院科室
     */
    private String deptDischargeFrom;
    /**
     * 出院日期及时间
     */
    private Date dischargeDateTime;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 婚姻状态
     */
    private String maritalStatus;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public String getDeptAdmissionTo() {
        return deptAdmissionTo;
    }

    public void setDeptAdmissionTo(String deptAdmissionTo) {
        this.deptAdmissionTo = deptAdmissionTo;
    }

    public Date getAdmissionDateTime() {
        return admissionDateTime;
    }

    public void setAdmissionDateTime(Date admissionDateTime) {
        this.admissionDateTime = admissionDateTime;
    }

    public String getDeptDischargeFrom() {
        return deptDischargeFrom;
    }

    public void setDeptDischargeFrom(String deptDischargeFrom) {
        this.deptDischargeFrom = deptDischargeFrom;
    }

    public Date getDischargeDateTime() {
        return dischargeDateTime;
    }

    public void setDischargeDateTime(Date dischargeDateTime) {
        this.dischargeDateTime = dischargeDateTime;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

}

