package com.dchealth.cro.account.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MessageSearchForm {
	  private Integer pageNo =1;//页码
	  private Integer pageSize =10;//每页记录数
	  @NotBlank(message = "用户ID不能为空")
	  private String accountId;//用户Id

	private Long accountIds;//用户Id
	  private String tenantId;//租户ID
	  


	private String isDoctor;
	private String isPatient;
	  

}
