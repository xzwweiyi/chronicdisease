package com.dchealth.medical.dto;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2026/1/8 9:29
 * @Version: 1.0
 */
@Data
public class FMedicalFormAndPropertyInfoDto {

    private Long recordId;

    private Long patientId;

    private Long researchId;

    private Long followId;

    private Long medicalFormId;

    private String medicalCode;

    private String formType;

    private String propertyCode;

    private String propertyType;

    private String name;

    private String warnStatus;

    private String loseType;

    private String loseReason;

    private String medicalName;

    private String medicalAttribute;

    private Long parentId;

    private String propertyName;

    private String propertyAttribute;

    private String label;

    private String value;

    private String other;

    private Long propertyId;


}
