package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 门诊诊断表(OutpMedrec)实体类
 *
 * @author makejava
 * @since 2023-05-18 15:33:58
 */
public class OutpMedrec extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -53907804509274075L;
    
    private String patientId;
    
    private Integer outpId;
    /**
     * 就诊日期
     */
    private Date visitDate;
    /**
     * 症状
     */
    private String symptom;
    /**
     * 结论
     */
    private String result;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Integer getOutpId() {
        return outpId;
    }

    public void setOutpId(Integer outpId) {
        this.outpId = outpId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}

