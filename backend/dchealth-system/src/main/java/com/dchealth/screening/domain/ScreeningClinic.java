package com.dchealth.screening.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 门诊筛查记录对象 screening_clinic
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ScreeningClinic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 居民姓名 */
    private String residentName;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 就诊日期 */
    private Date visitDate;

    /** 就诊科室 */
    private String department;

    /** 诊断结果 */
    private String diagnosis;

    /** ICD编码 */
    private String icdCode;

    /** 病种类型 */
    private String diseaseType;

    /** 医生姓名 */
    private String doctorName;

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

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getIcdCode() {
        return icdCode;
    }

    public void setIcdCode(String icdCode) {
        this.icdCode = icdCode;
    }

    public String getDiseaseType() {
        return diseaseType;
    }

    public void setDiseaseType(String diseaseType) {
        this.diseaseType = diseaseType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @Override
    public String toString() {
        return "ScreeningClinic{id=" + id + "}";
    }
}
