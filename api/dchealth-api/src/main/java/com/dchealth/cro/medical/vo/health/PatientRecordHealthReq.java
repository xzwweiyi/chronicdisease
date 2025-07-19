package com.dchealth.cro.medical.vo.health;


import lombok.Data;

import java.io.Serializable;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/6/27 10:43
 * @Version: 1.0
 */
@Data
public class PatientRecordHealthReq implements Serializable {

    /**
     * 健康管理档案id
     */
    private Long healthId;

    /**
     * 随访id
     */
    private Long followId;

    /**
     * 档案管理id
     */
    private Long recordId;

    private Long patientId;

    /**
     * 健康管理类型
     */
    private Integer healthManageType;

}
