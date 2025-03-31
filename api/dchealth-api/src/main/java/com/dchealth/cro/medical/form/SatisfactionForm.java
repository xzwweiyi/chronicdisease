package com.dchealth.cro.medical.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SatisfactionForm {


	  private Integer pageNo =1;//页码
	  private Integer pageSize =10;//每页记录数
	  @NotNull(message = "用户ID不能为空")
	  private Long accountId;//用户Id

	  
	  

}
