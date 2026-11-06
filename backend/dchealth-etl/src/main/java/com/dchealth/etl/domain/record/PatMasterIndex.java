package com.dchealth.etl.domain.record;

import com.dchealth.etl.domain.BaseEntity;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 病人主索引患者信息表](PatMasterIndex)实体类
 *
 * @author makejava
 * @since 2023-03-27 13:44:51
 */
public class PatMasterIndex extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -73762967763644112L;
    /**
     * 病人唯一标识号，可以由用户赋予具体的含义，如：病案号，门诊号等
     */
    @Id
    private String patientId;
    /**
     * 住院号
     */
    private String inpNo;
    /**
     * 姓名
     */
    private String name;
    /**
     * 姓名拼音
     */
    private String namePhonetic;
    /**
     * 性别
     */
    private String sex;
    /**
     * 出生日期
     */
    private String dateOfBirth;
    /**
     * 出生地
     */
    private String birthPlace;
    /**
     * 民族
     */
    private String nation;
    /**
     * 身份证号
     */
    private String idNo;
    /**
     * 身份
     */
    private String identity;
    /**
     * 费别
     */
    private String chargeType;
    /**
     * 合同单位
     */
    private String unitInContract;
    /**
     * 通信地址
     */
    private String mailingAddress;
    /**
     * 邮政编码
     */
    private String zipCode;
    /**
     * 家庭电话号码
     */
    private String phoneNumberHome;
    /**
     * 单位电话号码
     */
    private String phoneNumberBusiness;
    /**
     * 联系人姓名
     */
    private String nextOfKin;
    /**
     * 与联系人关系
     */
    private String relationship;
    /**
     * 联系人地址
     */
    private String nextOfKinAddr;
    /**
     * 联系人邮政编码
     */
    private String nextOfKinZipCode;
    /**
     * 联系人电话号码
     */
    private String nextOfKinPhone;
    /**
     * 上次就诊日期
     */
    private Date lastVisitDate;
    /**
     * 上次就诊日期
     */
    private Integer vipIndicator;
    
    private String createDate;
    /**
     * 操作员
     */
    private String operator;
    
    private Integer cardStatus;
    
    private String rundcode;
    
    private Integer bankflag;
    
    private String bankcardNo;
    
    private Integer secretLevel;
    /**
     * 国籍
     */
    private String citizenship;
    
    private Integer column1;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getInpNo() {
        return inpNo;
    }

    public void setInpNo(String inpNo) {
        this.inpNo = inpNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePhonetic() {
        return namePhonetic;
    }

    public void setNamePhonetic(String namePhonetic) {
        this.namePhonetic = namePhonetic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getUnitInContract() {
        return unitInContract;
    }

    public void setUnitInContract(String unitInContract) {
        this.unitInContract = unitInContract;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumberHome() {
        return phoneNumberHome;
    }

    public void setPhoneNumberHome(String phoneNumberHome) {
        this.phoneNumberHome = phoneNumberHome;
    }

    public String getPhoneNumberBusiness() {
        return phoneNumberBusiness;
    }

    public void setPhoneNumberBusiness(String phoneNumberBusiness) {
        this.phoneNumberBusiness = phoneNumberBusiness;
    }

    public String getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(String nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getNextOfKinAddr() {
        return nextOfKinAddr;
    }

    public void setNextOfKinAddr(String nextOfKinAddr) {
        this.nextOfKinAddr = nextOfKinAddr;
    }

    public String getNextOfKinZipCode() {
        return nextOfKinZipCode;
    }

    public void setNextOfKinZipCode(String nextOfKinZipCode) {
        this.nextOfKinZipCode = nextOfKinZipCode;
    }

    public String getNextOfKinPhone() {
        return nextOfKinPhone;
    }

    public void setNextOfKinPhone(String nextOfKinPhone) {
        this.nextOfKinPhone = nextOfKinPhone;
    }

    public Date getLastVisitDate() {
        return lastVisitDate;
    }

    public void setLastVisitDate(Date lastVisitDate) {
        this.lastVisitDate = lastVisitDate;
    }

    public Integer getVipIndicator() {
        return vipIndicator;
    }

    public void setVipIndicator(Integer vipIndicator) {
        this.vipIndicator = vipIndicator;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(Integer cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getRundcode() {
        return rundcode;
    }

    public void setRundcode(String rundcode) {
        this.rundcode = rundcode;
    }

    public Integer getBankflag() {
        return bankflag;
    }

    public void setBankflag(Integer bankflag) {
        this.bankflag = bankflag;
    }

    public String getBankcardNo() {
        return bankcardNo;
    }

    public void setBankcardNo(String bankcardNo) {
        this.bankcardNo = bankcardNo;
    }

    public Integer getSecretLevel() {
        return secretLevel;
    }

    public void setSecretLevel(Integer secretLevel) {
        this.secretLevel = secretLevel;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public Integer getColumn1() {
        return column1;
    }

    public void setColumn1(Integer column1) {
        this.column1 = column1;
    }

}

