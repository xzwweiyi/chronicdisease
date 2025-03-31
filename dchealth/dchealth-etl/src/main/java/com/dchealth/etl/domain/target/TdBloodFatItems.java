package com.dchealth.etl.domain.target;

import java.util.Date;
import java.io.Serializable;

/**
 * 血脂项目表(TdBloodFatItems)实体类
 *
 * @author makejava
 * @since 2023-04-10 17:06:28
 */
public class TdBloodFatItems implements Serializable {
    private static final long serialVersionUID = 115538643097028355L;
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
     * 总胆固醇
     */
    private String totalCholesterol;
    /**
     * 甘油三酯
     */
    private String triglycerides;
    /**
     * 高密度脂蛋白胆固醇
     */
    private String hdlCholesterol;
    /**
     * 载脂蛋白A-1
     */
    private String apolipoproteinA1;
    /**
     * 载脂蛋白B
     */
    private String apolipoproteinB;
    /**
     * 低密度脂蛋白胆固醇
     */
    private String ldlCholesterol;


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

    public String getTotalCholesterol() {
        return totalCholesterol;
    }

    public void setTotalCholesterol(String totalCholesterol) {
        this.totalCholesterol = totalCholesterol;
    }

    public String getTriglycerides() {
        return triglycerides;
    }

    public void setTriglycerides(String triglycerides) {
        this.triglycerides = triglycerides;
    }

    public String getHdlCholesterol() {
        return hdlCholesterol;
    }

    public void setHdlCholesterol(String hdlCholesterol) {
        this.hdlCholesterol = hdlCholesterol;
    }

    public String getApolipoproteinA1() {
        return apolipoproteinA1;
    }

    public void setApolipoproteinA1(String apolipoproteinA1) {
        this.apolipoproteinA1 = apolipoproteinA1;
    }

    public String getApolipoproteinB() {
        return apolipoproteinB;
    }

    public void setApolipoproteinB(String apolipoproteinB) {
        this.apolipoproteinB = apolipoproteinB;
    }

    public String getLdlCholesterol() {
        return ldlCholesterol;
    }

    public void setLdlCholesterol(String ldlCholesterol) {
        this.ldlCholesterol = ldlCholesterol;
    }

}

