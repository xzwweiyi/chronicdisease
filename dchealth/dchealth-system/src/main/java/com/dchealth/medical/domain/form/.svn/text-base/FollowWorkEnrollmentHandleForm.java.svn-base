package com.dchealth.medical.domain.form;

import lombok.Data;

/**
 * 入组处理实体
 */
@Data
public class FollowWorkEnrollmentHandleForm {

    /**
     * 处理类型
     * 1待分配2变更3确认入组
     */
    private String handleType;

    /**
     * 入组或修改入组队列id
     */
    private Long researchId;

    /**
     * 患者ID
     */
    private Long patientId;

    private Long userId;

    private String updateBy;

    /**
     * 拒绝编码  1, 2, 3
     */
    private String refuseCode;

    /**
     * 拒绝原因
     * 1,不符合入组条件, 2,患者多次拒绝入组 3 自定义内容
     */
    private String refuseReason;

}
