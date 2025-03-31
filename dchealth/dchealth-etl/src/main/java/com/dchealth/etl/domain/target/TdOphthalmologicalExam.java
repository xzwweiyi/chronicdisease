package com.dchealth.etl.domain.target;

import java.util.Date;
import java.io.Serializable;

/**
 * 眼科检查表(TdOphthalmologicalExam)实体类
 *
 * @author makejava
 * @since 2023-04-07 14:55:07
 */
public class TdOphthalmologicalExam implements Serializable {
    private static final long serialVersionUID = -14380849855577251L;
    
    private Integer id;
    /**
     * 体检Id
     */
    private String studyId;
    /**
     * 检查号序号
     */
    private String examNo;
    /**
     * 检验序号
     */
    private String testNo;
    /**
     * 结果正常标志 高，字段以逗号分隔保存
     */
    private String abnormalIndicatorHigh;
    /**
     * 结果正常标志 低，字段以逗号分隔保存
     */
    private String abnormalIndicatorLow;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 视力（左）
     */
    private String visualAcuityLeft;
    /**
     * 视力（右）
     */
    private String visualAcuityRight;
    /**
     * 矫正视力左
     */
    private String correctedVisualAcuityLeft;
    /**
     * 矫正视力右
     */
    private String correctedVisualAcuityRight;
    /**
     * 眼位
     */
    private String eyePosition;
    /**
     * 眼球运动
     */
    private String eyeMovement;
    /**
     * 眼睑
     */
    private String eyelid;
    /**
     * 睑结膜
     */
    private String conjunctivaOfEyelids;
    /**
     * 球结膜
     */
    private String conjunctivaOfEyeball;
    /**
     * 眼压检查
     */
    private String intraocularPressureExam;
    /**
     * 裂隙灯检查
     */
    private String slitLampExam;
    /**
     * 眼底照相
     */
    private String fundusPhotography;
    /**
     * 色觉检查
     */
    private String colorVisionExam;
    /**
     * 泪器
     */
    private String lacrimalApparatus;


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

    public String getVisualAcuityLeft() {
        return visualAcuityLeft;
    }

    public void setVisualAcuityLeft(String visualAcuityLeft) {
        this.visualAcuityLeft = visualAcuityLeft;
    }

    public String getVisualAcuityRight() {
        return visualAcuityRight;
    }

    public void setVisualAcuityRight(String visualAcuityRight) {
        this.visualAcuityRight = visualAcuityRight;
    }

    public String getCorrectedVisualAcuityLeft() {
        return correctedVisualAcuityLeft;
    }

    public void setCorrectedVisualAcuityLeft(String correctedVisualAcuityLeft) {
        this.correctedVisualAcuityLeft = correctedVisualAcuityLeft;
    }

    public String getCorrectedVisualAcuityRight() {
        return correctedVisualAcuityRight;
    }

    public void setCorrectedVisualAcuityRight(String correctedVisualAcuityRight) {
        this.correctedVisualAcuityRight = correctedVisualAcuityRight;
    }

    public String getEyePosition() {
        return eyePosition;
    }

    public void setEyePosition(String eyePosition) {
        this.eyePosition = eyePosition;
    }

    public String getEyeMovement() {
        return eyeMovement;
    }

    public void setEyeMovement(String eyeMovement) {
        this.eyeMovement = eyeMovement;
    }

    public String getEyelid() {
        return eyelid;
    }

    public void setEyelid(String eyelid) {
        this.eyelid = eyelid;
    }

    public String getConjunctivaOfEyelids() {
        return conjunctivaOfEyelids;
    }

    public void setConjunctivaOfEyelids(String conjunctivaOfEyelids) {
        this.conjunctivaOfEyelids = conjunctivaOfEyelids;
    }

    public String getConjunctivaOfEyeball() {
        return conjunctivaOfEyeball;
    }

    public void setConjunctivaOfEyeball(String conjunctivaOfEyeball) {
        this.conjunctivaOfEyeball = conjunctivaOfEyeball;
    }

    public String getIntraocularPressureExam() {
        return intraocularPressureExam;
    }

    public void setIntraocularPressureExam(String intraocularPressureExam) {
        this.intraocularPressureExam = intraocularPressureExam;
    }

    public String getSlitLampExam() {
        return slitLampExam;
    }

    public void setSlitLampExam(String slitLampExam) {
        this.slitLampExam = slitLampExam;
    }

    public String getFundusPhotography() {
        return fundusPhotography;
    }

    public void setFundusPhotography(String fundusPhotography) {
        this.fundusPhotography = fundusPhotography;
    }

    public String getColorVisionExam() {
        return colorVisionExam;
    }

    public void setColorVisionExam(String colorVisionExam) {
        this.colorVisionExam = colorVisionExam;
    }

    public String getLacrimalApparatus() {
        return lacrimalApparatus;
    }

    public void setLacrimalApparatus(String lacrimalApparatus) {
        this.lacrimalApparatus = lacrimalApparatus;
    }

}

