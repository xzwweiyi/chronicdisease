package com.dchealth.cro.medical.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 绑定患者form
 */
@Data
public class BindPatientDetailForm {

    /**
     * 账号id
     */
    @NotNull(message = "账号ID不能为空")
    private Long accountId;

    /**
     * 用户openId，用于身份验证
     */
    @NotBlank(message = "openId不能为空")
    private String openId;

}
