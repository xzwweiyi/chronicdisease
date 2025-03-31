package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 药品处方主记录(DrugPrescMaster)实体类
 *
 * @author makejava
 * @since 2023-04-24 17:53:50
 */
public class DrugPrescMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -78318269500530494L;
    
    private Date prescDate;
    /**
     * 处方号
     */
    private String prescNo;
    /**
     * 病人标识号
     */
    private String patientId;
    /**
     * 处方类别
     */
    private Long prescType;
    /**
     * 处方来源
     */
    private Long prescSource;


    public Date getPrescDate() {
        return prescDate;
    }

    public void setPrescDate(Date prescDate) {
        this.prescDate = prescDate;
    }

    public String getPrescNo() {
        return prescNo;
    }

    public void setPrescNo(String prescNo) {
        this.prescNo = prescNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Long getPrescType() {
        return prescType;
    }

    public void setPrescType(Long prescType) {
        this.prescType = prescType;
    }

    public Long getPrescSource() {
        return prescSource;
    }

    public void setPrescSource(Long prescSource) {
        this.prescSource = prescSource;
    }

}

