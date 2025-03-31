package com.dchealth.cro.account.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SendVerfiyCodeForm {
//	@NotNull(message="类型不能为空")
	private String bussinessType;
	@NotNull(message="手机不能为空")
	private String mobile;
	private String appId;

}
