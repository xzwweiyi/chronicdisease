package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 唐都-心肌酶表(TdMyocardiaEnzyme)实体类
 *
 * @author makejava
 * @since 2023-04-03 09:35:31
 */
public class TdMyocardiaEnzyme extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 639245461026887228L;
    /**
     * 自增主键
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
     * 血清乳酸脱氢酶
     */
    private String serumLactateDehydrogenase;
    /**
     * 天门冬氨酸氨基转移酶
     */
    private String aspartateAminotransferase;
    /**
     * 血清肌酸激酶-MB同工酶活性测定
     */
    private String serumCreatineKinaseMbAssay;
    /**
     * 血清肌酸激酶测定
     */
    private String serumCreatineKinaseAssay;
    /**
     * α-羟丁酸脱氢酶
     */
    private String hbdh;


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

    public String getSerumLactateDehydrogenase() {
        return serumLactateDehydrogenase;
    }

    public void setSerumLactateDehydrogenase(String serumLactateDehydrogenase) {
        this.serumLactateDehydrogenase = serumLactateDehydrogenase;
    }

    public String getAspartateAminotransferase() {
        return aspartateAminotransferase;
    }

    public void setAspartateAminotransferase(String aspartateAminotransferase) {
        this.aspartateAminotransferase = aspartateAminotransferase;
    }

    public String getSerumCreatineKinaseMbAssay() {
        return serumCreatineKinaseMbAssay;
    }

    public void setSerumCreatineKinaseMbAssay(String serumCreatineKinaseMbAssay) {
        this.serumCreatineKinaseMbAssay = serumCreatineKinaseMbAssay;
    }

    public String getSerumCreatineKinaseAssay() {
        return serumCreatineKinaseAssay;
    }

    public void setSerumCreatineKinaseAssay(String serumCreatineKinaseAssay) {
        this.serumCreatineKinaseAssay = serumCreatineKinaseAssay;
    }

    public String getHbdh() {
        return hbdh;
    }

    public void setHbdh(String hbdh) {
        this.hbdh = hbdh;
    }

}

