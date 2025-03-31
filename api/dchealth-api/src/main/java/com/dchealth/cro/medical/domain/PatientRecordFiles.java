package com.dchealth.cro.medical.domain;

import lombok.Data;

import java.util.Date;

/**
 * 档案图片
 */
@Data
public class PatientRecordFiles {

    /**  */
    private Long id;

    /** 图片地址 */
    private String imageUrl;

    /** 患者ID */
    private Long patientId;

    /** 档案ID */
    private Long recordId;

    /** medical_form的主键id */
    private Long medicalFormId;

    private Long propertyId;

    private String propertyCode;

    /** 图片code */
    private String imageCode;

    /** 父级图片code */
    private String imageParentCode;

    private Date createTime;

    private Long examRecordId;

}
