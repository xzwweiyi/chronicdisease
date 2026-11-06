package com.dchealth.medical.domain.vo;

import lombok.Data;

@Data
public class PatientEnrollmentInfoVo {
    private Long patientId;//patient_info表id
    private String idCard;
    private Long researchId;//未入组队列
    private Long accountId;
    private Integer deptId;
    private String baseMobile;

}
