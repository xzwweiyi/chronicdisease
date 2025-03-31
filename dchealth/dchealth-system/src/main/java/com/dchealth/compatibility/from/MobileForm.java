package com.dchealth.compatibility.from;

import lombok.Data;

/**
 *
 * @author zhangxiao
 * @date 2025/03/12
 */
@Data
public class MobileForm {
    /**
     * 患者ID
     */
    private String patientId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 就诊id
     */
    private String personId;
}
