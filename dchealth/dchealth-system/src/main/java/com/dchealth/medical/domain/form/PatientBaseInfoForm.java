package com.dchealth.medical.domain.form;

import lombok.Data;

import java.util.Date;

/**
 * 患者基础库form
 */
@Data
public class PatientBaseInfoForm {
    /**
     * 健康档案类型id
     */
    private Long id;
    /**
     * 档案id
     */
    private Long recordId;

    /**
     * 患者ID
     */
    private Long basePatientId;

    /**
     * 队列ID
     */
    private Long researchId;

    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 类型
     */
    private String type;

}
