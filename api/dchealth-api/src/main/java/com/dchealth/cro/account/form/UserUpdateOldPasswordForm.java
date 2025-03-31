package com.dchealth.cro.account.form;

import javax.validation.constraints.NotBlank;

public class UserUpdateOldPasswordForm {
	//@NotNull(message = "账号ID不能为空")
	private Long accountId;
	//@NotBlank(message = "旧密码不能为空")
	private String oldPassword;
	@NotBlank(message = "新密码不能为空")
	private String password;

	private String verifyCode;//验证码
	private String phone;
	private String tenantId;
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
