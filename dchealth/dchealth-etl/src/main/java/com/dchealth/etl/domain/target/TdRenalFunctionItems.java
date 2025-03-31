package com.dchealth.etl.domain.target;

import java.util.Date;
import java.io.Serializable;

/**
 * 肾功能项目表(TdRenalFunctionItems)实体类
 *
 * @author makejava
 * @since 2023-04-10 17:05:33
 */
public class TdRenalFunctionItems implements Serializable {
    private static final long serialVersionUID = 307322843149788249L;
    /**
     * 自增住建
     */
    private Integer id;
    /**
     * 体检申请id
     */
    private String studyId;
    /**
     * 病人标识id
     */
    private String patientId;
    /**
     * 健康档案检查申请号
     */
    private String examNo;
    /**
     * 健康档案检验申请号
     */
    private String testNo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 报告时间
     */
    private Date resultTime;
    /**
     * 结果正常标志 高，字段以，分割保存
     */
    private String abnormalIndicatorHigh;
    /**
     * 结果正常标志 低，字段以，分割保存
     */
    private String abnormalIndicatorLow;
    /**
     * 直接胆红素
     */
    private String directBilirubin;
    /**
     * 肌酐
     */
    private String creatinine;
    /**
     * 尿素
     */
    private String bloodUreaNitrogen;
    /**
     * 血清胱抑素C
     */
    private String serumCystatinC;
    /**
     * 二氧化碳
     */
    private String carbonDioxide;
    /**
     * 尿酸
     */
    private String uricAcid;
    /**
     * β2-微球蛋白
     */
    private String microglobulinβ2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getTestNo() {
        return testNo;
    }

    public void setTestNo(String testNo) {
        this.testNo = testNo;
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

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public String getAbnormalIndicatorHigh() {
        return abnormalIndicatorHigh;
    }

    public void setAbnormalIndicatorHigh(String abnormalIndicatorHigh) {
        this.abnormalIndicatorHigh = abnormalIndicatorHigh;
    }

    public String getAbnormalIndicatorLow() {
        return abnormalIndicatorLow;
    }

    public void setAbnormalIndicatorLow(String abnormalIndicatorLow) {
        this.abnormalIndicatorLow = abnormalIndicatorLow;
    }

    public String getDirectBilirubin() {
        return directBilirubin;
    }

    public void setDirectBilirubin(String directBilirubin) {
        this.directBilirubin = directBilirubin;
    }

    public String getCreatinine() {
        return creatinine;
    }

    public void setCreatinine(String creatinine) {
        this.creatinine = creatinine;
    }

    public String getBloodUreaNitrogen() {
        return bloodUreaNitrogen;
    }

    public void setBloodUreaNitrogen(String bloodUreaNitrogen) {
        this.bloodUreaNitrogen = bloodUreaNitrogen;
    }

    public String getSerumCystatinC() {
        return serumCystatinC;
    }

    public void setSerumCystatinC(String serumCystatinC) {
        this.serumCystatinC = serumCystatinC;
    }

    public String getCarbonDioxide() {
        return carbonDioxide;
    }

    public void setCarbonDioxide(String carbonDioxide) {
        this.carbonDioxide = carbonDioxide;
    }

    public String getUricAcid() {
        return uricAcid;
    }

    public void setUricAcid(String uricAcid) {
        this.uricAcid = uricAcid;
    }

    public String getMicroglobulinβ2() {
        return microglobulinβ2;
    }

    public void setMicroglobulinβ2(String microglobulinβ2) {
        this.microglobulinβ2 = microglobulinβ2;
    }
}

