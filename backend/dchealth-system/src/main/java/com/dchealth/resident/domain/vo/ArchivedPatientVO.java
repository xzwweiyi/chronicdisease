package com.dchealth.resident.domain.vo;

import java.util.Date;

/**
 * 居民档案列表VO — 合并 mp_person + resident_archive 字段
 */
public class ArchivedPatientVO {
    // === mp_person 基础字段 ===
    private String personId;
    private String personName;
    private String genderSourceValue;
    private String cardId;
    private String cardType;
    private String phoneNumber;
    private String locationId;
    private String address;
    private String contactsName;
    private String contactsPhone;
    private String personCareer;
    private String maritalStatus;
    private String degreeOfEducation;
    private String sex;
    private String careSiteId;
    private Date createTime;
    private Date updateTime;

    // === resident_archive 管理字段 ===
    private Long id;                // resident_archive.id
    private String archiveCode;
    private Integer age;
    private Date birthDate;
    private String diseaseTypes;
    private Date archiveDate;
    private String archiveStatus;
    private String managingInstitution;
    private String managingDoctor;
    private String archiveSource;
    private Date transferInDate;
    private String isSigned;
    private String familyHistory;
    private String allergyHistory;
    private String community;
    private String remark;

    // === Getters and Setters ===
    public String getPersonId() { return personId; }
    public void setPersonId(String personId) { this.personId = personId; }
    public String getPersonName() { return personName; }
    public void setPersonName(String personName) { this.personName = personName; }
    public String getGenderSourceValue() { return genderSourceValue; }
    public void setGenderSourceValue(String genderSourceValue) { this.genderSourceValue = genderSourceValue; }
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactsName() { return contactsName; }
    public void setContactsName(String contactsName) { this.contactsName = contactsName; }
    public String getContactsPhone() { return contactsPhone; }
    public void setContactsPhone(String contactsPhone) { this.contactsPhone = contactsPhone; }
    public String getPersonCareer() { return personCareer; }
    public void setPersonCareer(String personCareer) { this.personCareer = personCareer; }
    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
    public String getDegreeOfEducation() { return degreeOfEducation; }
    public void setDegreeOfEducation(String degreeOfEducation) { this.degreeOfEducation = degreeOfEducation; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public String getCareSiteId() { return careSiteId; }
    public void setCareSiteId(String careSiteId) { this.careSiteId = careSiteId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getArchiveCode() { return archiveCode; }
    public void setArchiveCode(String archiveCode) { this.archiveCode = archiveCode; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public String getDiseaseTypes() { return diseaseTypes; }
    public void setDiseaseTypes(String diseaseTypes) { this.diseaseTypes = diseaseTypes; }
    public Date getArchiveDate() { return archiveDate; }
    public void setArchiveDate(Date archiveDate) { this.archiveDate = archiveDate; }
    public String getArchiveStatus() { return archiveStatus; }
    public void setArchiveStatus(String archiveStatus) { this.archiveStatus = archiveStatus; }
    public String getManagingInstitution() { return managingInstitution; }
    public void setManagingInstitution(String managingInstitution) { this.managingInstitution = managingInstitution; }
    public String getManagingDoctor() { return managingDoctor; }
    public void setManagingDoctor(String managingDoctor) { this.managingDoctor = managingDoctor; }
    public String getArchiveSource() { return archiveSource; }
    public void setArchiveSource(String archiveSource) { this.archiveSource = archiveSource; }
    public Date getTransferInDate() { return transferInDate; }
    public void setTransferInDate(Date transferInDate) { this.transferInDate = transferInDate; }
    public String getIsSigned() { return isSigned; }
    public void setIsSigned(String isSigned) { this.isSigned = isSigned; }
    public String getFamilyHistory() { return familyHistory; }
    public void setFamilyHistory(String familyHistory) { this.familyHistory = familyHistory; }
    public String getAllergyHistory() { return allergyHistory; }
    public void setAllergyHistory(String allergyHistory) { this.allergyHistory = allergyHistory; }
    public String getCommunity() { return community; }
    public void setCommunity(String community) { this.community = community; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
}
