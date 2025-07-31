package com.dchealth.cro.medical.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 绑定患者form
 */
@Data
public class BindPatientForm {

    /**
     * 账号id
     */
    @NotNull(message = "账号ID不能为空")
    private Long accountId;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    private String idCardNo;

    /**
     * 就诊id
     */
    @NotBlank(message = "就诊ID不能为空")
    private String registrySigns;

    /**
     * 电话号 （对接预问诊系统添加字段）
     */
    private String mobile;
}
