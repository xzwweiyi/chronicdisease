package com.dchealth.etl.domain.target;

import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 唐都-辅助检查-超声项目相关表(TdAuxiliaryExaminationUltrasound)实体类
 *
 * @author makejava
 * @since 2023-03-29 16:20:37
 */
public class TdAuxiliaryExaminationUltrasound extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 760449471227281727L;
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
     * 胸片报告
     */
    private String cr;
    /**
     * X线（胸部以外）
     */
    private String crOther;
    /**
     * 心脏超声
     */
    private String cardiacUltrasound;
    /**
     * 腹部超声
     */
    private String abdominalUltrasound;
    /**
     * 专科超声男
     */
    private String specializedUltrasoundMale;
    /**
     * 专科超声女
     */
    private String specializedUltrasoundFemale;
    /**
     * 甲状腺超声
     */
    private String thyroidUltrasound;
    /**
     * 颈动脉超声
     */
    private String carotidUltrasound;
    /**
     * 血管多普勒超声
     */
    private String vascularDopplerUltrasound;


    /**
     * 乳腺彩超
     */
    private String biRads;
    /**
     * 阴式彩超(女)
     */
    private String tvs;
    /**
     * 子宫附件彩超
     */
    private String uterineAppendagesUltrasound;
    /**
     * 肝胆胰脾
     */
    private String lgps;
    /**
     * 肝胆胰脾肾
     */
    private String lgpsk;
    /**
     * 肝胆胰脾肾前列腺膀胱
     */
    private String lgpskpb;
    /**
     * 肝胆胰脾肾子宫附件
     */
    private String lgpskua;
    /**
     * 双侧乳腺(钼靶)
     */
    private String bilateralMammaryGland;
    /**
     * 颈椎正位片
     */
    private String cervicalSpine;
    /**
     * 颈椎侧位片
     */
    private String cervicalLateralFilm;
    /**
     * 腰椎正位片
     */
    private String lumbarSpine;
    /**
     * 腰椎侧位片
     */
    private String lumbarSpineFilm;
    /**
     * 胸部正位片
     */
    private String chestXray;
    /**
     * 左膝关节正位
     */
    private String leftKneeJointAp;
    /**
     * 左膝关节侧位
     */
    private String lkjlp;
    /**
     * 右膝关节正位
     */
    private String rightKneeJointAp;
    /**
     * 右膝关节侧位
     */
    private String rkjlp;

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

    public String getCr() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr = cr;
    }

    public String getCrOther() {
        return crOther;
    }

    public void setCrOther(String crOther) {
        this.crOther = crOther;
    }

    public String getCardiacUltrasound() {
        return cardiacUltrasound;
    }

    public void setCardiacUltrasound(String cardiacUltrasound) {
        this.cardiacUltrasound = cardiacUltrasound;
    }

    public String getAbdominalUltrasound() {
        return abdominalUltrasound;
    }

    public void setAbdominalUltrasound(String abdominalUltrasound) {
        this.abdominalUltrasound = abdominalUltrasound;
    }

    public String getSpecializedUltrasoundMale() {
        return specializedUltrasoundMale;
    }

    public void setSpecializedUltrasoundMale(String specializedUltrasoundMale) {
        this.specializedUltrasoundMale = specializedUltrasoundMale;
    }

    public String getSpecializedUltrasoundFemale() {
        return specializedUltrasoundFemale;
    }

    public void setSpecializedUltrasoundFemale(String specializedUltrasoundFemale) {
        this.specializedUltrasoundFemale = specializedUltrasoundFemale;
    }

    public String getThyroidUltrasound() {
        return thyroidUltrasound;
    }

    public void setThyroidUltrasound(String thyroidUltrasound) {
        this.thyroidUltrasound = thyroidUltrasound;
    }

    public String getCarotidUltrasound() {
        return carotidUltrasound;
    }

    public void setCarotidUltrasound(String carotidUltrasound) {
        this.carotidUltrasound = carotidUltrasound;
    }

    public String getVascularDopplerUltrasound() {
        return vascularDopplerUltrasound;
    }

    public void setVascularDopplerUltrasound(String vascularDopplerUltrasound) {
        this.vascularDopplerUltrasound = vascularDopplerUltrasound;
    }

    public String getBiRads() {
        return biRads;
    }

    public void setBiRads(String biRads) {
        this.biRads = biRads;
    }

    public String getTvs() {
        return tvs;
    }

    public void setTvs(String tvs) {
        this.tvs = tvs;
    }

    public String getUterineAppendagesUltrasound() {
        return uterineAppendagesUltrasound;
    }

    public void setUterineAppendagesUltrasound(String uterineAppendagesUltrasound) {
        this.uterineAppendagesUltrasound = uterineAppendagesUltrasound;
    }

    public String getLgps() {
        return lgps;
    }

    public void setLgps(String lgps) {
        this.lgps = lgps;
    }

    public String getLgpsk() {
        return lgpsk;
    }

    public void setLgpsk(String lgpsk) {
        this.lgpsk = lgpsk;
    }

    public String getLgpskpb() {
        return lgpskpb;
    }

    public void setLgpskpb(String lgpskpb) {
        this.lgpskpb = lgpskpb;
    }

    public String getLgpskua() {
        return lgpskua;
    }

    public void setLgpskua(String lgpskua) {
        this.lgpskua = lgpskua;
    }

    public String getBilateralMammaryGland() {
        return bilateralMammaryGland;
    }

    public void setBilateralMammaryGland(String bilateralMammaryGland) {
        this.bilateralMammaryGland = bilateralMammaryGland;
    }

    public String getCervicalSpine() {
        return cervicalSpine;
    }

    public void setCervicalSpine(String cervicalSpine) {
        this.cervicalSpine = cervicalSpine;
    }

    public String getCervicalLateralFilm() {
        return cervicalLateralFilm;
    }

    public void setCervicalLateralFilm(String cervicalLateralFilm) {
        this.cervicalLateralFilm = cervicalLateralFilm;
    }

    public String getLumbarSpine() {
        return lumbarSpine;
    }

    public void setLumbarSpine(String lumbarSpine) {
        this.lumbarSpine = lumbarSpine;
    }

    public String getLumbarSpineFilm() {
        return lumbarSpineFilm;
    }

    public void setLumbarSpineFilm(String lumbarSpineFilm) {
        this.lumbarSpineFilm = lumbarSpineFilm;
    }

    public String getChestXray() {
        return chestXray;
    }

    public void setChestXray(String chestXray) {
        this.chestXray = chestXray;
    }

    public String getLeftKneeJointAp() {
        return leftKneeJointAp;
    }

    public void setLeftKneeJointAp(String leftKneeJointAp) {
        this.leftKneeJointAp = leftKneeJointAp;
    }

    public String getLkjlp() {
        return lkjlp;
    }

    public void setLkjlp(String lkjlp) {
        this.lkjlp = lkjlp;
    }

    public String getRightKneeJointAp() {
        return rightKneeJointAp;
    }

    public void setRightKneeJointAp(String rightKneeJointAp) {
        this.rightKneeJointAp = rightKneeJointAp;
    }

    public String getRkjlp() {
        return rkjlp;
    }

    public void setRkjlp(String rkjlp) {
        this.rkjlp = rkjlp;
    }
}

