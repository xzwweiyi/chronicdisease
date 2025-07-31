package com.dchealth.cro.health.domain;

import com.dchealth.cro.medical.domain.PatientInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 患者信息-对应omop对象 mp_person
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpPerson {
	  /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 主键自增 */
    private Long id;

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;
    private Long patientId;

    /** 患者姓名 */
    private String personName;

    /** 性别名称 */
    private String genderSourceValue;

    /** 证件号 */
    private String cardId;

    /** 证件类型 */
    private String cardType;

    /** 患者电话 */
    private String phoneNumber;

    /** 现住址 */
    private String locationId;

    /** 家庭住址 */
    private String address;

    /** 联系人 */
    private String contactsName;

    /** 联系人电话 */
    private String contactsPhone;

    /** 职业名称 */
    private String personCareer;

    /** 婚姻状况名称 */
    private String maritalStatus;

    /** 文化程度名称 */
    private String degreeOfEducation;
    private Long accountId;
    private List<PatientInfo> patientInfoList;


    private Long researchId;
    private Long deptId;
    private String mobile;
    private String patientName;

   
}
