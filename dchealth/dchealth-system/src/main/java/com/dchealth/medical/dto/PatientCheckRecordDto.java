package com.dchealth.medical.dto;

import lombok.Data;

/**
 * @Author jiaorui
 * @Date 2023/3/27
 * @Description:
 */
@Data
public class PatientCheckRecordDto {

    private long id;

    /**
     * 1：健康档案 2：体检报告
     */
    private Integer status;

    /**
     * 唐都医院患者ID
     */
    private String patientId;

    /**
     * 创建日期-健康档案，体检报告的创建日期
     */
    private String checkDate;


    /**
     * 体检Id
     */
    private String studyId;

    /**
     * 身份证号
     */
    private String idCardNo;

    /**
     * 患者ID，对应patient_base_info表中的ID（自动生成）
     */
    private long patientBaseId;

    private String nodeName;
}
