package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 检查报告表
此表描述检查报告内容，由检查执行科室生成。
主键：申请序号。(ExamReport)实体类
 *
 * @author makejava
 * @since 2023-03-30 17:27:22
 */
public class ExamReport extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 671294763406018073L;
    /**
     * 申请序号
     */
    private String examNo;
    /**
     * 检查参数
     */
    private String examPara;
    /**
     * 检查所见
     */
    private String description;
    /**
     * 印象
     */
    private String impression;
    /**
     * 建议
     */
    private String recommendation;
    /**
     * 是否阳性 1-阳性，即检查可能有病变，其他为阴性
     */
    private String isAbnormal;
    /**
     * 使用仪器
     */
    private String device;
    /**
     * 报告中图象编号
     */
    private String useImage;
    /**
     * 备注
     */
    private String memo;
    /**
     * 报告名称
     */
    private String reportname;

    /**
     * 检查类别
     */
    private String examClass;
    /**
     * 检查子类
     */
    private String examSubClass;

    /**
     * 报告日期及时间
     */
    private Date examDateTime;

    public String getExamNo() {
        return examNo;
    }

    public void setExamNo(String examNo) {
        this.examNo = examNo;
    }

    public String getExamPara() {
        return examPara;
    }

    public void setExamPara(String examPara) {
        this.examPara = examPara;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(String isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUseImage() {
        return useImage;
    }

    public void setUseImage(String useImage) {
        this.useImage = useImage;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getExamClass() {
        return examClass;
    }

    public void setExamClass(String examClass) {
        this.examClass = examClass;
    }

    public String getExamSubClass() {
        return examSubClass;
    }

    public void setExamSubClass(String examSubClass) {
        this.examSubClass = examSubClass;
    }

    public Date getExamDateTime() {
        return examDateTime;
    }

    public void setExamDateTime(Date examDateTime) {
        this.examDateTime = examDateTime;
    }
}

