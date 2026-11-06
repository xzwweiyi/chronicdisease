package com.dchealth.medical.domain.vo;

import lombok.Data;

/**
 * 患者联系方式 VO（用于随访工作站右侧浮动联系面板）。
 */
@Data
public class PatientContactVO {

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 公众号 openId
     */
    private String openId;

    /**
     * 是否已绑定微信
     */
    private Boolean hasWechat;
}
