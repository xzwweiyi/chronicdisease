package com.dchealth.medical.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import lombok.Data;

/**
 * 患者信息-对应omop对象 mp_person
 * 
 * @date 2023-11-08
 */
@Data
public class MpPerson extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 主键自增 */
    private Long id;

    /** 医疗机构id */
    @Excel(name = "医疗机构id")
    private String careSiteId;

    /** 患者id */
    @Excel(name = "患者id")
    private String personId;

    /** 患者姓名 */
    @Excel(name = "患者姓名")
    private String personName;

    /** 性别名称 */
    @Excel(name = "性别名称")
    private String genderSourceValue;

    /** 证件号 */
    @Excel(name = "证件号")
    private String cardId;

    /** 证件类型 */
    @Excel(name = "证件类型")
    private String cardType;

    /** 患者电话 */
    @Excel(name = "患者电话")
    private String phoneNumber;

    /** 患者电话 */
    @Excel(name = "患者电话")
    private String mobile;

    /** 现住址 */
    @Excel(name = "现住址")
    private String locationId;

    /** 家庭住址 */
    @Excel(name = "家庭住址")
    private String address;

    /** 联系人 */
    @Excel(name = "联系人")
    private String contactsName;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    private String contactsPhone;

    /** 职业名称 */
    @Excel(name = "职业名称")
    private String personCareer;

    /** 婚姻状况名称 */
    @Excel(name = "婚姻状况名称")
    private String maritalStatus;

    /** 文化程度名称 */
    @Excel(name = "文化程度名称")
    private String degreeOfEducation;

    /** 性别 */
    @Excel(name = "性别")
    private String sex;
    
    private Long researchId;
    private Long accountId;
    private Long deptId;


    private String patientName;
   
}
