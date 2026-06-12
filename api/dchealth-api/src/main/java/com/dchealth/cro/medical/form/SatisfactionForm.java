package com.dchealth.cro.medical.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SatisfactionForm {


	  private Integer pageNo =1;//页码
	  private Integer pageSize =10;//每页记录数
	@NotBlank(message = "用户ID不能为空")
	private String accountId;//用户Id
	private Long accountIds;//用户Id

	  
	  

}
