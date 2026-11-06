package com.dchealth.contract.domain.vo;

import com.dchealth.contract.domain.ContractSign;

/**
 * 签约记录VO — 关联 resident_archive + contract_team + contract_package
 * 用于签约管理列表页展示完整的患者签约信息
 *
 * @author dchealth
 * @date 2026-07-01
 */
public class ContractSignVO extends ContractSign {

    private static final long serialVersionUID = 1L;

    // ========== resident_archive 字段 ==========
    /** 性别 */
    private String gender;
    /** 年龄 */
    private Integer age;
    /** 身份证号 */
    private String idCard;
    /** 联系电话 */
    private String phone;
    /** 管理机构 */
    private String managingInstitution;
    /** 管理医生 */
    private String managingDoctor;
    /** 慢病类型 */
    private String diseaseTypes;
    /** 风险等级 */
    private String riskLevel;

    // ========== contract_team 字段 ==========
    /** 签约团队名称 */
    private String teamName;

    // ========== contract_package 字段 ==========
    /** 服务包名称 */
    private String packageName;
    /** 服务包价格 */
    private java.math.BigDecimal packagePrice;

    // ========== mp_person 字段（来源：resident_archive.id_card → mp_person.card_id） ==========
    /** 患者姓名（mp_person） */
    private String mpPersonName;
    /** 性别来源值 */
    private String mpGenderSourceValue;
    /** 手机号 */
    private String mpPhoneNumber;
    /** 性别编码 */
    private String mpSex;

    // ========== getters/setters ==========

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getIdCard() { return idCard; }
    public void setIdCard(String idCard) { this.idCard = idCard; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getManagingInstitution() { return managingInstitution; }
    public void setManagingInstitution(String managingInstitution) { this.managingInstitution = managingInstitution; }

    public String getManagingDoctor() { return managingDoctor; }
    public void setManagingDoctor(String managingDoctor) { this.managingDoctor = managingDoctor; }

    public String getDiseaseTypes() { return diseaseTypes; }
    public void setDiseaseTypes(String diseaseTypes) { this.diseaseTypes = diseaseTypes; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }

    public java.math.BigDecimal getPackagePrice() { return packagePrice; }
    public void setPackagePrice(java.math.BigDecimal packagePrice) { this.packagePrice = packagePrice; }

    public String getMpPersonName() { return mpPersonName; }
    public void setMpPersonName(String mpPersonName) { this.mpPersonName = mpPersonName; }
    public String getMpGenderSourceValue() { return mpGenderSourceValue; }
    public void setMpGenderSourceValue(String mpGenderSourceValue) { this.mpGenderSourceValue = mpGenderSourceValue; }
    public String getMpPhoneNumber() { return mpPhoneNumber; }
    public void setMpPhoneNumber(String mpPhoneNumber) { this.mpPhoneNumber = mpPhoneNumber; }
    public String getMpSex() { return mpSex; }
    public void setMpSex(String mpSex) { this.mpSex = mpSex; }
}
