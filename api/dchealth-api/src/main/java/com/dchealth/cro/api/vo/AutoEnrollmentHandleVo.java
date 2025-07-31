package com.dchealth.cro.api.vo;

import lombok.Data;

/**
 * 入组处理实体
 */
@Data
public class AutoEnrollmentHandleVo {

    /**
     * 处理类型
     * 1待分配2变更3确认入组4.取消入组
     */
    private String handleType;

    /**
     * 入组或修改入组队列id
     */
    private Long researchId;

//    /**
//     * 患者ID
//     */
//    private Long patientId;
//
//    private Long userId;
//
//    private String personId;

}
