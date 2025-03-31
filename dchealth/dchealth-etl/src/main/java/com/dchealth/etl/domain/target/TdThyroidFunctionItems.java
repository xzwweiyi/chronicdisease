package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 甲状腺功能项目表(TdThyroidFunctionItems)实体类
 *
 * @author makejava
 * @since 2023-04-03 09:37:38
 */
public class TdThyroidFunctionItems extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -84179605116195497L;
    
    private Integer id;
    /**
     * 体检Id
     */
    private Integer studyId;
    /**
     * 检查序号
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
     * 更新时间
     */
    private Date updateTime;
    /**
     * 三碘甲状腺原氨酸(T3)
     */
    private String triiodothyronine;
    /**
     * 甲状腺素（T4）
     */
    private String thyroxine;
    /**
     * 促甲状腺激素(TSH)
     */
    private String thyroidStimulatingHormone;
    /**
     * 游离三碘甲状腺原氨酸(T3)
     */
    private String freeTriiodothyronine;
    /**
     * 游离甲状腺素（FT4）
     */
    private String freeThyroxine;
    /**
     * 抗甲状腺球蛋白抗体
     */
    private String antithyroglobulinAntibody;
    /**
     * 抗甲状腺过氧化物酶抗体
     */
    private String antithyroidPeroxidaseAntibody;
    /**
     * 促甲状腺素受体抗体
     */
    private String thyrotropinReceptorAntibody;
    /**
     * 抗甲状腺微粒体抗体
     */
    private String antithyroidMicrosomeAntibody;
    /**
     * 甲状腺球蛋白
     */
    private String thyroglobulin;
    /**
     * 游离三碘甲状腺原氨酸
     */
    private String ft3;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudyId() {
        return studyId;
    }

    public void setStudyId(Integer studyId) {
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

    public String getTriiodothyronine() {
        return triiodothyronine;
    }

    public void setTriiodothyronine(String triiodothyronine) {
        this.triiodothyronine = triiodothyronine;
    }

    public String getThyroxine() {
        return thyroxine;
    }

    public void setThyroxine(String thyroxine) {
        this.thyroxine = thyroxine;
    }

    public String getThyroidStimulatingHormone() {
        return thyroidStimulatingHormone;
    }

    public void setThyroidStimulatingHormone(String thyroidStimulatingHormone) {
        this.thyroidStimulatingHormone = thyroidStimulatingHormone;
    }

    public String getFreeTriiodothyronine() {
        return freeTriiodothyronine;
    }

    public void setFreeTriiodothyronine(String freeTriiodothyronine) {
        this.freeTriiodothyronine = freeTriiodothyronine;
    }

    public String getFreeThyroxine() {
        return freeThyroxine;
    }

    public void setFreeThyroxine(String freeThyroxine) {
        this.freeThyroxine = freeThyroxine;
    }

    public String getAntithyroglobulinAntibody() {
        return antithyroglobulinAntibody;
    }

    public void setAntithyroglobulinAntibody(String antithyroglobulinAntibody) {
        this.antithyroglobulinAntibody = antithyroglobulinAntibody;
    }

    public String getAntithyroidPeroxidaseAntibody() {
        return antithyroidPeroxidaseAntibody;
    }

    public void setAntithyroidPeroxidaseAntibody(String antithyroidPeroxidaseAntibody) {
        this.antithyroidPeroxidaseAntibody = antithyroidPeroxidaseAntibody;
    }

    public String getThyrotropinReceptorAntibody() {
        return thyrotropinReceptorAntibody;
    }

    public void setThyrotropinReceptorAntibody(String thyrotropinReceptorAntibody) {
        this.thyrotropinReceptorAntibody = thyrotropinReceptorAntibody;
    }

    public String getAntithyroidMicrosomeAntibody() {
        return antithyroidMicrosomeAntibody;
    }

    public void setAntithyroidMicrosomeAntibody(String antithyroidMicrosomeAntibody) {
        this.antithyroidMicrosomeAntibody = antithyroidMicrosomeAntibody;
    }

    public String getThyroglobulin() {
        return thyroglobulin;
    }

    public void setThyroglobulin(String thyroglobulin) {
        this.thyroglobulin = thyroglobulin;
    }

    public String getFt3() {
        return ft3;
    }

    public void setFt3(String ft3) {
        this.ft3 = ft3;
    }
}

