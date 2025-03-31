package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 血常规(TdBloodRoutine)实体类
 *
 * @author makejava
 * @since 2023-04-03 09:26:17
 */
public class TdBloodRoutine extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 293453183502057327L;
    /**
     * id
     */
    private Integer id;
    /**
     * 体检id
     */
    private String studyId;
    /**
     * 检查序号
     */
    private String examNo;
    /**
     * 检验序号
     */
    private String testNo;
    /**
     * 结果正常标志 高 ，字段以逗号分隔保存
     */
    private String abnormalIndicatorHigh;
    /**
     * 结果正常标志 低 ，字段以逗号分隔保存
     */
    private String abnormalIndicatorLow;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 结果时间
     */
    private Date resultTime;
    /**
     * 白细胞计数
     */
    private String whiteCellCount;
    /**
     * 红细胞计数
     */
    private String redCellCount;
    /**
     * 血红蛋白
     */
    private String hemoglobin;
    /**
     * 红细胞比容
     */
    private String hematocrit;
    /**
     * 平均红细胞体积
     */
    private String meanCorpusVolume;
    /**
     * 平均红细胞血红蛋白含量
     */
    private String meanCorpusHemoglobin;
    /**
     * 平均红细胞血红蛋白浓度
     */
    private String meanCorpusHemConcent;
    /**
     * 红细胞分布宽度-变异系数
     */
    private String redCellDisWidthVariation;
    /**
     * 血小板计数
     */
    private String plateletCount;
    /**
     * 平均血小板体积
     */
    private String meanPlateletVolume;
    /**
     * 血小板分布宽度
     */
    private String plateletDisWidth;
    /**
     * 淋巴细胞百分比
     */
    private String lymphocytePercentage;
    /**
     * 中性粒细胞百分比
     */
    private String neutrophilPercentage;
    /**
     * 淋巴细胞绝对值
     */
    private String absLymphocyteCount;
    /**
     * 中性粒细胞绝对值
     */
    private String absNeutrophilCount;
    /**
     * 红细胞分布宽度-标准差
     */
    private String redCellDisDeviation;
    /**
     * 血小板压积
     */
    private String plateletcrit;
    /**
     * 单核细胞百分比
     */
    private String monocytePercentage;
    /**
     * 单核细胞绝对值
     */
    private String absMonocyteCount;
    /**
     * 嗜酸性细胞百分比
     */
    private String eosinophilPercentage;
    /**
     * 嗜酸性细胞绝对值
     */
    private String absEosinophilCount;
    /**
     * 嗜碱性细胞百分比
     */
    private String basophilPercentage;
    /**
     * 嗜碱性细胞绝对值
     */
    private String absBasophilCount;
    /**
     * 大血小板比率
     */
    private String largePlateletRatio;
    /**
     * 血小板比积
     */
    private String plateletSpecificVolume;
    /**
     * ABO血型鉴定
     */
    private String aboBlood;
    /**
     * Rh(D)血型鉴定
     */
    private String rhBlood;
    /**
     * 红细胞沉降率
     */
    private String esr;
    /**
     * 电解质测定-钙（血）
     */
    private String determinationElectCalcium;
    /**
     * 淀粉酶测定（血）
     */
    private String amylaseAssay;

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

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public String getWhiteCellCount() {
        return whiteCellCount;
    }

    public void setWhiteCellCount(String whiteCellCount) {
        this.whiteCellCount = whiteCellCount;
    }

    public String getRedCellCount() {
        return redCellCount;
    }

    public void setRedCellCount(String redCellCount) {
        this.redCellCount = redCellCount;
    }

    public String getHemoglobin() {
        return hemoglobin;
    }

    public void setHemoglobin(String hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public String getHematocrit() {
        return hematocrit;
    }

    public void setHematocrit(String hematocrit) {
        this.hematocrit = hematocrit;
    }

    public String getMeanCorpusVolume() {
        return meanCorpusVolume;
    }

    public void setMeanCorpusVolume(String meanCorpusVolume) {
        this.meanCorpusVolume = meanCorpusVolume;
    }

    public String getMeanCorpusHemoglobin() {
        return meanCorpusHemoglobin;
    }

    public void setMeanCorpusHemoglobin(String meanCorpusHemoglobin) {
        this.meanCorpusHemoglobin = meanCorpusHemoglobin;
    }

    public String getMeanCorpusHemConcent() {
        return meanCorpusHemConcent;
    }

    public void setMeanCorpusHemConcent(String meanCorpusHemConcent) {
        this.meanCorpusHemConcent = meanCorpusHemConcent;
    }

    public String getRedCellDisWidthVariation() {
        return redCellDisWidthVariation;
    }

    public void setRedCellDisWidthVariation(String redCellDisWidthVariation) {
        this.redCellDisWidthVariation = redCellDisWidthVariation;
    }

    public String getPlateletCount() {
        return plateletCount;
    }

    public void setPlateletCount(String plateletCount) {
        this.plateletCount = plateletCount;
    }

    public String getMeanPlateletVolume() {
        return meanPlateletVolume;
    }

    public void setMeanPlateletVolume(String meanPlateletVolume) {
        this.meanPlateletVolume = meanPlateletVolume;
    }

    public String getPlateletDisWidth() {
        return plateletDisWidth;
    }

    public void setPlateletDisWidth(String plateletDisWidth) {
        this.plateletDisWidth = plateletDisWidth;
    }

    public String getLymphocytePercentage() {
        return lymphocytePercentage;
    }

    public void setLymphocytePercentage(String lymphocytePercentage) {
        this.lymphocytePercentage = lymphocytePercentage;
    }

    public String getNeutrophilPercentage() {
        return neutrophilPercentage;
    }

    public void setNeutrophilPercentage(String neutrophilPercentage) {
        this.neutrophilPercentage = neutrophilPercentage;
    }

    public String getAbsLymphocyteCount() {
        return absLymphocyteCount;
    }

    public void setAbsLymphocyteCount(String absLymphocyteCount) {
        this.absLymphocyteCount = absLymphocyteCount;
    }

    public String getAbsNeutrophilCount() {
        return absNeutrophilCount;
    }

    public void setAbsNeutrophilCount(String absNeutrophilCount) {
        this.absNeutrophilCount = absNeutrophilCount;
    }

    public String getRedCellDisDeviation() {
        return redCellDisDeviation;
    }

    public void setRedCellDisDeviation(String redCellDisDeviation) {
        this.redCellDisDeviation = redCellDisDeviation;
    }

    public String getPlateletcrit() {
        return plateletcrit;
    }

    public void setPlateletcrit(String plateletcrit) {
        this.plateletcrit = plateletcrit;
    }

    public String getMonocytePercentage() {
        return monocytePercentage;
    }

    public void setMonocytePercentage(String monocytePercentage) {
        this.monocytePercentage = monocytePercentage;
    }

    public String getAbsMonocyteCount() {
        return absMonocyteCount;
    }

    public void setAbsMonocyteCount(String absMonocyteCount) {
        this.absMonocyteCount = absMonocyteCount;
    }

    public String getEosinophilPercentage() {
        return eosinophilPercentage;
    }

    public void setEosinophilPercentage(String eosinophilPercentage) {
        this.eosinophilPercentage = eosinophilPercentage;
    }

    public String getAbsEosinophilCount() {
        return absEosinophilCount;
    }

    public void setAbsEosinophilCount(String absEosinophilCount) {
        this.absEosinophilCount = absEosinophilCount;
    }

    public String getBasophilPercentage() {
        return basophilPercentage;
    }

    public void setBasophilPercentage(String basophilPercentage) {
        this.basophilPercentage = basophilPercentage;
    }

    public String getAbsBasophilCount() {
        return absBasophilCount;
    }

    public void setAbsBasophilCount(String absBasophilCount) {
        this.absBasophilCount = absBasophilCount;
    }

    public String getLargePlateletRatio() {
        return largePlateletRatio;
    }

    public void setLargePlateletRatio(String largePlateletRatio) {
        this.largePlateletRatio = largePlateletRatio;
    }

    public String getPlateletSpecificVolume() {
        return plateletSpecificVolume;
    }

    public void setPlateletSpecificVolume(String plateletSpecificVolume) {
        this.plateletSpecificVolume = plateletSpecificVolume;
    }

    public String getAboBlood() {
        return aboBlood;
    }

    public void setAboBlood(String aboBlood) {
        this.aboBlood = aboBlood;
    }

    public String getRhBlood() {
        return rhBlood;
    }

    public void setRhBlood(String rhBlood) {
        this.rhBlood = rhBlood;
    }

    public String getEsr() {
        return esr;
    }

    public void setEsr(String esr) {
        this.esr = esr;
    }

    public String getDeterminationElectCalcium() {
        return determinationElectCalcium;
    }

    public void setDeterminationElectCalcium(String determinationElectCalcium) {
        this.determinationElectCalcium = determinationElectCalcium;
    }

    public String getAmylaseAssay() {
        return amylaseAssay;
    }

    public void setAmylaseAssay(String amylaseAssay) {
        this.amylaseAssay = amylaseAssay;
    }
}

