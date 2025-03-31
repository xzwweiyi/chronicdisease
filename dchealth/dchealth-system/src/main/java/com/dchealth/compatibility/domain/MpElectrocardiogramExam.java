package com.dchealth.compatibility.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 心电图检查对象 mp_electrocardiogram_exam
 * 
 * @author lchbu
 * @date 2023-12-13
 */
public class MpElectrocardiogramExam
{
    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    private Long id;

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;

    /** 就诊类型 */
    private String visitType;

    /** 就诊id */
    private String visitOccurrenceId;

    /** 标化后标准检查项目 */
    private String technologyName;

    /** 标化后标准身体部位名称 */
    private String sctPosition;

    /** 身体部位一级 */
    private String level1Position;

    /** 身体部位二级 */
    private String level2Position;

    /** 检查时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date technologyDate;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportDate;

    /** 检查报告结果--客观所见 */
    private String objectiveView;

    /** 检查报告结果--主观提示 */
    private String subjectiveHints;

    /** 是否为阳性 */
    private String isAbnormal;

    /** 检查诊断 */
    private String relevantClinicDiagnosis;

    /** 体征 */
    private String physSign;

    /** 执行科室名称 */
    private String exeDepartment;

    /** 开单科室名称 */
    private String departmentName;

    /** 检查方式（增强，平扫） */
    private String testMethod;

    /** 心电图报告 */
    private String pdfReport;

    /**
     * 检测编号
     */
    private String testNo;
    /**
     * 检测来源
     */
    private String checkSourceType;
    /**
     * 视图md5
     */
    private String viewMd5;

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public String getCheckSourceType() {
        return checkSourceType;
    }

    public void setCheckSourceType(String checkSourceType) {
        this.checkSourceType = checkSourceType;
    }

    public String getViewMd5() {
        return viewMd5;
    }

    public void setViewMd5(String viewMd5) {
        this.viewMd5 = viewMd5;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCareSiteId(String careSiteId) 
    {
        this.careSiteId = careSiteId;
    }

    public String getCareSiteId() 
    {
        return careSiteId;
    }
    public void setPersonId(String personId) 
    {
        this.personId = personId;
    }

    public String getPersonId() 
    {
        return personId;
    }
    public void setVisitType(String visitType) 
    {
        this.visitType = visitType;
    }

    public String getVisitType() 
    {
        return visitType;
    }
    public void setVisitOccurrenceId(String visitOccurrenceId) 
    {
        this.visitOccurrenceId = visitOccurrenceId;
    }

    public String getVisitOccurrenceId() 
    {
        return visitOccurrenceId;
    }
    public void setTechnologyName(String technologyName) 
    {
        this.technologyName = technologyName;
    }

    public String getTechnologyName() 
    {
        return technologyName;
    }
    public void setSctPosition(String sctPosition) 
    {
        this.sctPosition = sctPosition;
    }

    public String getSctPosition() 
    {
        return sctPosition;
    }
    public void setLevel1Position(String level1Position) 
    {
        this.level1Position = level1Position;
    }

    public String getLevel1Position() 
    {
        return level1Position;
    }
    public void setLevel2Position(String level2Position) 
    {
        this.level2Position = level2Position;
    }

    public String getLevel2Position() 
    {
        return level2Position;
    }
    public void setTechnologyDate(Date technologyDate) 
    {
        this.technologyDate = technologyDate;
    }

    public Date getTechnologyDate() 
    {
        return technologyDate;
    }
    public void setReportDate(Date reportDate) 
    {
        this.reportDate = reportDate;
    }

    public Date getReportDate() 
    {
        return reportDate;
    }
    public void setObjectiveView(String objectiveView) 
    {
        this.objectiveView = objectiveView;
    }

    public String getObjectiveView() 
    {
        return objectiveView;
    }
    public void setSubjectiveHints(String subjectiveHints) 
    {
        this.subjectiveHints = subjectiveHints;
    }

    public String getSubjectiveHints() 
    {
        return subjectiveHints;
    }
    public void setIsAbnormal(String isAbnormal) 
    {
        this.isAbnormal = isAbnormal;
    }

    public String getIsAbnormal() 
    {
        return isAbnormal;
    }
    public void setRelevantClinicDiagnosis(String relevantClinicDiagnosis) 
    {
        this.relevantClinicDiagnosis = relevantClinicDiagnosis;
    }

    public String getRelevantClinicDiagnosis() 
    {
        return relevantClinicDiagnosis;
    }
    public void setPhysSign(String physSign) 
    {
        this.physSign = physSign;
    }

    public String getPhysSign() 
    {
        return physSign;
    }
    public void setExeDepartment(String exeDepartment) 
    {
        this.exeDepartment = exeDepartment;
    }

    public String getExeDepartment() 
    {
        return exeDepartment;
    }
    public void setDepartmentName(String departmentName) 
    {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() 
    {
        return departmentName;
    }
    public void setTestMethod(String testMethod) 
    {
        this.testMethod = testMethod;
    }

    public String getTestMethod() 
    {
        return testMethod;
    }

    public String getPdfReport() {
        return pdfReport;
    }

    public void setPdfReport(String pdfReport) {
        this.pdfReport = pdfReport;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("careSiteId", getCareSiteId())
            .append("personId", getPersonId())
            .append("visitType", getVisitType())
            .append("visitOccurrenceId", getVisitOccurrenceId())
            .append("technologyName", getTechnologyName())
            .append("sctPosition", getSctPosition())
            .append("level1Position", getLevel1Position())
            .append("level2Position", getLevel2Position())
            .append("technologyDate", getTechnologyDate())
            .append("reportDate", getReportDate())
            .append("objectiveView", getObjectiveView())
            .append("subjectiveHints", getSubjectiveHints())
            .append("isAbnormal", getIsAbnormal())
            .append("relevantClinicDiagnosis", getRelevantClinicDiagnosis())
            .append("physSign", getPhysSign())
            .append("exeDepartment", getExeDepartment())
            .append("departmentName", getDepartmentName())
            .append("testMethod", getTestMethod())
            .toString();
    }
}
