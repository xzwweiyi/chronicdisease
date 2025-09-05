package com.dchealth.cro.api.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/25 10:55
 * @Version: 1.0
 */
@Data
public class BindPatientVo {

    @NotNull(message = "身份证号不能为空")
    private String card;

    @NotNull(message = "患者就诊id不能为空")
    private String personId;

    @NotNull(message = "手机号不能为空")
    private String mobile;

    @NotNull(message = "科室编码不能为空")
    private String deptCode;

    @NotNull(message = "是否同意不能为空")
    private Long isAgree;
}
