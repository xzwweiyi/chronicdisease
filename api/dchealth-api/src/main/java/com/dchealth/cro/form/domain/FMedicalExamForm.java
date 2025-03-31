package com.dchealth.cro.form.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.dchealth.cro.common.base.BaseEntity;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 单模板-问卷对象 f_medical_exam_form
 * 
 * @author lchbu
 * @date 2022-11-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FMedicalExamForm extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表单id */
    private Long medicalFormId;

    /** 随访表单id */
    private Long followId;

    /** 表单编码 */
    private String medicalCode;

    /** 表单名称 */
    private String medicalName;

    /** 父id */
    private Long parentId;

    /** 是否开启APP端图片上传 0未开启 1 开启  */
    private String openImage;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    private String openProperty;

    /** 是否开启动态添加 0未开启 1 开启  */
    private String openMultiple;

    /** $column.columnComment */
    private String attribute;

    /** 版本号 */
    private String versionCode;

    /** 0模块1标准项2拍照项3自评量表4预警填报 */
    private String formType;

    /** 是否动态表格0否1是 */
    private String dynamic;

    /** 表单来源 */
    private String formSource;

    /** 栏目编号 */
    private Long categoryId;

    /**
     * 问卷信息
     */
    private List<FMedicalExamSubject> subjectList;

    /** cms关联id */
    private Long articleId;

    private Long patientId;

    /** 患者填报记录id */
    private Long examRecordId;

    /** 分数 */
    private Long scores;
    /** 评价 */
    private String evaluation;

    /** 是否提交；true提交，false保存 */
    private boolean flag;

    private Long researchId;

    private String status;//0待填写 1保存 2提交

    /**
     * 是否再来一次
     *  用途是  这个问卷是不是复制出来的
     */
    private Boolean isFillAgain = false;

    /**
     * 用途是控制是否展示“再来一次”按钮
     * true：展示，false：隐藏
     */
    private Boolean isShowAgain = true;
    /**
     * 上传图片
     */
    private List<PatientRecordFilesForm> imageUrls;
    private String imageUrlsJson;
    public Boolean getFillAgain() {
        return isFillAgain;
    }

    public void setFillAgain(Boolean fillAgain) {
        isFillAgain = fillAgain;
    }


    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getScores() {
        return scores;
    }

    public void setScores(Long scores) {
        this.scores = scores;
    }

    public Long getExamRecordId() {
        return examRecordId;
    }

    public void setExamRecordId(Long examRecordId) {
        this.examRecordId = examRecordId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<FMedicalExamSubject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<FMedicalExamSubject> subjectList) {
        this.subjectList = subjectList;
    }
    public Boolean getShowAgain() {
        return isShowAgain;
    }

    public void setShowAgain(Boolean showAgain) {
        isShowAgain = showAgain;
    }
    public void setMedicalFormId(Long medicalFormId)
    {
        this.medicalFormId = medicalFormId;
    }

    public Long getMedicalFormId() 
    {
        return medicalFormId;
    }
    public void setFollowId(Long followId) 
    {
        this.followId = followId;
    }

    public Long getFollowId() 
    {
        return followId;
    }
    public void setMedicalCode(String medicalCode) 
    {
        this.medicalCode = medicalCode;
    }

    public String getMedicalCode() 
    {
        return medicalCode;
    }
    public void setMedicalName(String medicalName) 
    {
        this.medicalName = medicalName;
    }

    public String getMedicalName() 
    {
        return medicalName;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setOpenImage(String openImage) 
    {
        this.openImage = openImage;
    }

    public String getOpenImage() 
    {
        return openImage;
    }
    public void setOpenProperty(String openProperty) 
    {
        this.openProperty = openProperty;
    }

    public String getOpenProperty() 
    {
        return openProperty;
    }
    public void setOpenMultiple(String openMultiple) 
    {
        this.openMultiple = openMultiple;
    }

    public String getOpenMultiple() 
    {
        return openMultiple;
    }
    public void setAttribute(String attribute) 
    {
        this.attribute = attribute;
    }

    public String getAttribute() 
    {
        return attribute;
    }
    public void setVersionCode(String versionCode) 
    {
        this.versionCode = versionCode;
    }

    public String getVersionCode() 
    {
        return versionCode;
    }
    public void setFormType(String formType) 
    {
        this.formType = formType;
    }

    public String getFormType() 
    {
        return formType;
    }
    public void setDynamic(String dynamic) 
    {
        this.dynamic = dynamic;
    }

    public String getDynamic() 
    {
        return dynamic;
    }
    public void setFormSource(String formSource) 
    {
        this.formSource = formSource;
    }

    public String getFormSource() 
    {
        return formSource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("medicalFormId", getMedicalFormId())
            .append("followId", getFollowId())
            .append("medicalCode", getMedicalCode())
            .append("medicalName", getMedicalName())
            .append("parentId", getParentId())
            .append("openImage", getOpenImage())
            .append("openProperty", getOpenProperty())
            .append("openMultiple", getOpenMultiple())
            .append("attribute", getAttribute())
            .append("versionCode", getVersionCode())
            .append("formType", getFormType())
            .append("dynamic", getDynamic())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("formSource", getFormSource())
            .toString();
    }
}
