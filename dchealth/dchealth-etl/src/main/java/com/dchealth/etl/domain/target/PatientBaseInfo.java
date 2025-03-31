package com.dchealth.etl.domain.target;

import com.dchealth.elasticsearch.annotation.ESID;
import com.dchealth.elasticsearch.annotation.ESIndices;
import com.dchealth.etl.domain.BaseEntity;

import java.util.Date;
import java.io.Serializable;

/**
 * 患者基础表(patientbaseinfo)实体类
 *
 * @author makejava
 * @since 2023-03-27 15:03:36
 */

@ESIndices(indexName = "index_patientbaseinfo")
public class PatientBaseInfo extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -11832454236407166L;
    @ESID
    private Long id;
    /**
     * 登陆账号id
     */
    private Long accountId;
    /**
     * 头像地址
     */
    private String headImage;
    /**
     * 医疗机构编码
     */
    private String hospitalCode;
    /**
     * 就诊标识
     */
    private String registrySigns;
    /**
     * 病案号
     */
    private Long medicalRecordNo;
    /**
     * 患者姓名
     */
    private String name;
    /**
     * 出生日期
     */
    private String birthday;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 性别（0:未知，1：男，2：女）
     */
    private Integer sex;
    /**
     * 婚姻状态
     */
    private String maritalStatus;
    /**
     * 职业
     */
    private String profession;
    /**
     * 工作单位
     */
    private String workUnit;
    /**
     * 民族
     */
    private String nation;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 移动电话
     */
    private String mobile;
    /**
     * 现住址
     */
    private String presentAddress;
    /**
     * 户口地址
     */
    private String permanentAddress;
    /**
     * 文化程度 1=研究生；2=大学本科；3=大学专科和专科学校；4=中等专业学校；5=技工学校；6=高中；7=初中；8=小学；9=文盲或半文盲；10=不详
     */
    private String degreeOfEducation;
    /**
     * 血型 1=A型；2=B型；3=O型、4=AB型；5=不详/1=阴性；2=阳性；3=不详
     */
    private String abo;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 关系
     */
    private String relationship;
    /**
     * 联系人住址
     */
    private String linkManAddress;
    /**
     * 入院途径
     */
    private String admissionHospitalWay;
    /**
     * 联系人电话
     */
    private String linkManMobile;
    /**
     * 入院日期
     */
    private Date admissionHospitalDate;
    /**
     * 入院科室
     */
    private String admissionHospitalDepartment;
    /**
     * 患者状态0失效1生效
     */
    private String patientStatus;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 患者Id
     */
    private String patientId;
    /**
     * 出院科室
     */
    private String deptDischargeFrom;
    /**
     * 出院时间
     */
    private Date dischargeDate;
    /**
     * 上次就诊日期
     */
    private Date lastVisitDate;
    /**
     * 门诊时间
     */
    private Date patientClinicTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getRegistrySigns() {
        return registrySigns;
    }

    public void setRegistrySigns(String registrySigns) {
        this.registrySigns = registrySigns;
    }

    public Long getMedicalRecordNo() {
        return medicalRecordNo;
    }

    public void setMedicalRecordNo(Long medicalRecordNo) {
        this.medicalRecordNo = medicalRecordNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getDegreeOfEducation() {
        return degreeOfEducation;
    }

    public void setDegreeOfEducation(String degreeOfEducation) {
        this.degreeOfEducation = degreeOfEducation;
    }

    public String getAbo() {
        return abo;
    }

    public void setAbo(String abo) {
        this.abo = abo;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getLinkManAddress() {
        return linkManAddress;
    }

    public void setLinkManAddress(String linkManAddress) {
        this.linkManAddress = linkManAddress;
    }

    public String getAdmissionHospitalWay() {
        return admissionHospitalWay;
    }

    public void setAdmissionHospitalWay(String admissionHospitalWay) {
        this.admissionHospitalWay = admissionHospitalWay;
    }

    public String getLinkManMobile() {
        return linkManMobile;
    }

    public void setLinkManMobile(String linkManMobile) {
        this.linkManMobile = linkManMobile;
    }

    public Date getAdmissionHospitalDate() {
        return admissionHospitalDate;
    }

    public void setAdmissionHospitalDate(Date admissionHospitalDate) {
        this.admissionHospitalDate = admissionHospitalDate;
    }

    public String getAdmissionHospitalDepartment() {
        return admissionHospitalDepartment;
    }

    public void setAdmissionHospitalDepartment(String admissionHospitalDepartment) {
        this.admissionHospitalDepartment = admissionHospitalDepartment;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDeptDischargeFrom() {
        return deptDischargeFrom;
    }

    public void setDeptDischargeFrom(String deptDischargeFrom) {
        this.deptDischargeFrom = deptDischargeFrom;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public Date getPatientClinicTime() {
        return patientClinicTime;
    }

    public void setPatientClinicTime(Date patientClinicTime) {
        this.patientClinicTime = patientClinicTime;
    }
}

