package com.dchealth.resident.domain;

import java.util.Date;
import com.dchealth.common.core.domain.BaseEntity;

public class ResidentArchive extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String archiveCode;
    private String residentName;
    private String gender;
    private Integer age;
    private Date birthDate;
    private String idCard;
    private String phone;
    private String address;
    private String community;
    private Date archiveDate;
    private String archiveStatus;
    private String managingInstitution;
    private String managingDoctor;
    private String diseaseTypes;
    private String archiveSource;
    private Date transferInDate;
    private String isSigned;
    private String familyHistory;
    private String allergyHistory;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getArchiveCode() { return archiveCode; }
    public void setArchiveCode(String archiveCode) { this.archiveCode = archiveCode; }
    public String getResidentName() { return residentName; }
    public void setResidentName(String residentName) { this.residentName = residentName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCommunity() { return community; }
    public void setCommunity(String community) { this.community = community; }
    public Date getArchiveDate() { return archiveDate; }
    public void setArchiveDate(Date archiveDate) { this.archiveDate = archiveDate; }
    public String getArchiveStatus() { return archiveStatus; }
    public void setArchiveStatus(String archiveStatus) { this.archiveStatus = archiveStatus; }
    public String getManagingInstitution() { return managingInstitution; }
    public void setManagingInstitution(String managingInstitution) { this.managingInstitution = managingInstitution; }
    public String getManagingDoctor() { return managingDoctor; }
    public void setManagingDoctor(String managingDoctor) { this.managingDoctor = managingDoctor; }
    public String getDiseaseTypes() { return diseaseTypes; }
    public void setDiseaseTypes(String diseaseTypes) { this.diseaseTypes = diseaseTypes; }
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
}
