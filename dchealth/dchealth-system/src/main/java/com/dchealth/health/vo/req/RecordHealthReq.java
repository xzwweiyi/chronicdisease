package com.dchealth.health.vo.req;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/2 13:51
 * @Version: 1.0
 */
@Data
public class RecordHealthReq {

    /**
     * 患者档案id
     */
    private Long recordId;

    /**
     * 健康管理id
     */
    private Long healthId;

    /**
     * 患者id
     */
    private Long patientId;
}
