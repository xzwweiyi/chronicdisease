package com.dchealth.screening.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 体检筛查记录对象 screening_physical
 *
 * @author dchealth
 * @date 2026-06-27
 */
public class ScreeningPhysical extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 居民姓名 */
    private String residentName;

    /** 身份证号 */
    private String idCard;

    /** 联系电话 */
    private String phone;

    /** 体检日期 */
    private Date examDate;

    /** 体检机构 */
    private String examInstitution;

    /** 身体质量指数(BMI) */
    private BigDecimal bmi;

    /** 血压 */
    private String bloodPressure;

    /** 血糖值 */
    private BigDecimal bloodSugar;

    /** 血脂 */
    private String bloodLipids;

    /** 体检结果 */
    private String examResult;

    /** 疾病风险 */
    private String diseaseRisk;

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

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public String getExamInstitution() {
        return examInstitution;
    }

    public void setExamInstitution(String examInstitution) {
        this.examInstitution = examInstitution;
    }

    public BigDecimal getBmi() {
        return bmi;
    }

    public void setBmi(BigDecimal bmi) {
        this.bmi = bmi;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public BigDecimal getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(BigDecimal bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getBloodLipids() {
        return bloodLipids;
    }

    public void setBloodLipids(String bloodLipids) {
        this.bloodLipids = bloodLipids;
    }

    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }

    public String getDiseaseRisk() {
        return diseaseRisk;
    }

    public void setDiseaseRisk(String diseaseRisk) {
        this.diseaseRisk = diseaseRisk;
    }

    @Override
    public String toString() {
        return "ScreeningPhysical{id=" + id + "}";
    }
}
