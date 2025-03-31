package com.dchealth.etl.domain.target;

import com.dchealth.elasticsearch.annotation.ESID;
import com.dchealth.elasticsearch.annotation.ESIndices;
import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 诊断记录(TdDiagnosis)实体类
 *
 * @author makejava
 * @since 2023-04-04 16:33:42
 */
@ESIndices(indexName = "index_tddiagnosis")
public class TdDiagnosis extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -16926819667937982L;
    /**
     * 自增主键
     */
    @ESID
    private Integer id;
    /**
     * 病人标识
     */
    private String patientId;
    /**
     * 体检id
     */
    private String studyId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 诊断类别
     */
    private String diagnosisType;
    /**
     * 诊断序号
     */
    private Integer diagnosisNo;
    /**
     * 诊断描述、终检结论
     */
    private String diagnosisDesc;
    /**
     * 诊断日期、终检日期
     */
    private Date diagnosisDate;
    /**
     * 终检医生
     */
    private String finalCheckDoctor;
    /**
     * 检查结果
     */
    private String checkDesc;
    /**
     * 结论医生
     */
    private String checkDoctor;
    /**
     * 结论日期
     */
    private Date checkDate;
    /**
     * 保健建议
     */
    private String healthAdvice;
    /**
     * 饮食建议
     */
    private String dietaryAdvice;
    /**
     * 运动建议
     */
    private String sportsAdvice;
    /**
     * 治疗建议
     */
    private String treatmentRecommendations;
    /**
     * 诊断日期和终检日期的日期格式
     */
    private Date resultDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(String diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public Integer getDiagnosisNo() {
        return diagnosisNo;
    }

    public void setDiagnosisNo(Integer diagnosisNo) {
        this.diagnosisNo = diagnosisNo;
    }

    public String getDiagnosisDesc() {
        return diagnosisDesc;
    }

    public void setDiagnosisDesc(String diagnosisDesc) {
        this.diagnosisDesc = diagnosisDesc;
    }

    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public String getFinalCheckDoctor() {
        return finalCheckDoctor;
    }

    public void setFinalCheckDoctor(String finalCheckDoctor) {
        this.finalCheckDoctor = finalCheckDoctor;
    }

    public String getCheckDesc() {
        return checkDesc;
    }

    public void setCheckDesc(String checkDesc) {
        this.checkDesc = checkDesc;
    }

    public String getCheckDoctor() {
        return checkDoctor;
    }

    public void setCheckDoctor(String checkDoctor) {
        this.checkDoctor = checkDoctor;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getHealthAdvice() {
        return healthAdvice;
    }

    public void setHealthAdvice(String healthAdvice) {
        this.healthAdvice = healthAdvice;
    }

    public String getDietaryAdvice() {
        return dietaryAdvice;
    }

    public void setDietaryAdvice(String dietaryAdvice) {
        this.dietaryAdvice = dietaryAdvice;
    }

    public String getSportsAdvice() {
        return sportsAdvice;
    }

    public void setSportsAdvice(String sportsAdvice) {
        this.sportsAdvice = sportsAdvice;
    }

    public String getTreatmentRecommendations() {
        return treatmentRecommendations;
    }

    public void setTreatmentRecommendations(String treatmentRecommendations) {
        this.treatmentRecommendations = treatmentRecommendations;
    }

    public Date getResultDate() {
        return resultDate;
    }

    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }
}

