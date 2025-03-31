package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 检验主记录表
注释：此表描述每张检验申请单。一个检验申请与一个标本必须一一对应。此表记录由开单程序生成（病房或门诊或检验科室），由检验科室修改。
以1000张床位计，每天全院的检验申请约为2000件。每月的数据增长量约为6万条。
主键：申请序号(LabTestMaster)实体类
 *
 * @author makejava
 * @since 2023-03-30 11:28:22
 */
public class LabTestMaster extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -83357109464435209L;
    /**
     * 申请序号
     */
    private String testNo;
    /**
     * 优先标志 反映此申请的紧急程度。0-普通 1-紧急
     */
    private Integer priorityIndicator;
    /**
     * 病人标识号
     */
    private String patientId;
    /**
     * 本次住院标识
     */
    private Integer visitId;
    /**
     * 工作单号
     */
    private String workingId;
    /**
     * 执行日期
     */
    private Date executeDate;
    /**
     * 姓名
     */
    private String name;
    /**
     * 姓名拼音
     */
    private String namePhonetic;
    /**
     * 费别
     */
    private String chargeType;
    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 检验主题
     */
    private String subject;
    /**
     * 检验目的
     */
    private String testCause;
    /**
     * 临床诊断
     */
    private String relevantClinicDiag;
    /**
     * 标本
     */
    private String specimen;
    /**
     * 标本说明
     */
    private String notesForSpcm;
    /**
     * 标本采样日期及时间
     */
    private Date spcmSampleDateTime;
    /**
     * 标本收到日期及时间
     */
    private Date spcmReceivedDateTime;
    /**
     * 申请日期及时间
     */
    private Date requestedDateTimecolumn1;
    /**
     * 申请科室
     */
    private String orderingDept;
    /**
     * 申请医生
     */
    private String orderingProvider;
    /**
     * 执行科室
     */
    private String performedBy;
    /**
     * 结果状态
     */
    private String resultStatus;
    /**
     * 报告日期及时间
     */
    private Date resultsRptDateTime;
    /**
     * 报告者
     */
    private String transcriptionist;
    /**
     * 校对者
     */
    private String verifiedBy;
    /**
     * 费用
     */
    private Float costs;
    /**
     * 应收费用
     */
    private Float charges;
    /**
     * 计价标志
     */
    private Integer billingIndicator;
    /**
     * 打印标志
     */
    private Integer printIndicator;


    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
    }

    public Integer getPriorityIndicator() {
        return priorityIndicator;
    }

    public void setPriorityIndicator(Integer priorityIndicator) {
        this.priorityIndicator = priorityIndicator;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Integer getVisitId() {
        return visitId;
    }

    public void setVisitId(Integer visitId) {
        this.visitId = visitId;
    }

    public String getWorkingId() {
        return workingId;
    }

    public void setWorkingId(String workingId) {
        this.workingId = workingId;
    }

    public Date getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(Date executeDate) {
        this.executeDate = executeDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePhonetic() {
        return namePhonetic;
    }

    public void setNamePhonetic(String namePhonetic) {
        this.namePhonetic = namePhonetic;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTestCause() {
        return testCause;
    }

    public void setTestCause(String testCause) {
        this.testCause = testCause;
    }

    public String getRelevantClinicDiag() {
        return relevantClinicDiag;
    }

    public void setRelevantClinicDiag(String relevantClinicDiag) {
        this.relevantClinicDiag = relevantClinicDiag;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getNotesForSpcm() {
        return notesForSpcm;
    }

    public void setNotesForSpcm(String notesForSpcm) {
        this.notesForSpcm = notesForSpcm;
    }

    public Date getSpcmSampleDateTime() {
        return spcmSampleDateTime;
    }

    public void setSpcmSampleDateTime(Date spcmSampleDateTime) {
        this.spcmSampleDateTime = spcmSampleDateTime;
    }

    public Date getSpcmReceivedDateTime() {
        return spcmReceivedDateTime;
    }

    public void setSpcmReceivedDateTime(Date spcmReceivedDateTime) {
        this.spcmReceivedDateTime = spcmReceivedDateTime;
    }

    public Date getRequestedDateTimecolumn1() {
        return requestedDateTimecolumn1;
    }

    public void setRequestedDateTimecolumn1(Date requestedDateTimecolumn1) {
        this.requestedDateTimecolumn1 = requestedDateTimecolumn1;
    }

    public String getOrderingDept() {
        return orderingDept;
    }

    public void setOrderingDept(String orderingDept) {
        this.orderingDept = orderingDept;
    }

    public String getOrderingProvider() {
        return orderingProvider;
    }

    public void setOrderingProvider(String orderingProvider) {
        this.orderingProvider = orderingProvider;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public Date getResultsRptDateTime() {
        return resultsRptDateTime;
    }

    public void setResultsRptDateTime(Date resultsRptDateTime) {
        this.resultsRptDateTime = resultsRptDateTime;
    }

    public String getTranscriptionist() {
        return transcriptionist;
    }

    public void setTranscriptionist(String transcriptionist) {
        this.transcriptionist = transcriptionist;
    }

    public String getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(String verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Float getCosts() {
        return costs;
    }

    public void setCosts(Float costs) {
        this.costs = costs;
    }

    public Float getCharges() {
        return charges;
    }

    public void setCharges(Float charges) {
        this.charges = charges;
    }

    public Integer getBillingIndicator() {
        return billingIndicator;
    }

    public void setBillingIndicator(Integer billingIndicator) {
        this.billingIndicator = billingIndicator;
    }

    public Integer getPrintIndicator() {
        return printIndicator;
    }

    public void setPrintIndicator(Integer printIndicator) {
        this.printIndicator = printIndicator;
    }

}

