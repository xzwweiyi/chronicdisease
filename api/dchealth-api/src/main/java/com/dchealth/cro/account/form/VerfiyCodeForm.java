package com.dchealth.cro.account.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VerfiyCodeForm {
	@NotNull(message = "手机号不能为空")
	private String mobile;//手机号
	@NotNull(message = "验证不能为空")
	private String verifyCode;// 验证码
	private String tenantId;
}
