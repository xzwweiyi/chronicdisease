package com.dchealth.cro.account.form;

import javax.validation.constraints.NotBlank;

public class UserUpdatePasswordForm {
	@NotBlank(message = "账号ID不能为空")
	private Long accountId;
	@NotBlank(message = "密码不能为空")
	private String password;
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
  
}
