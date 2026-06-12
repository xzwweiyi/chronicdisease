package com.dchealth.cro.cms.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CollectionActicleSearchForm {
	  private Integer pageNo =1;//页码
	  private Integer pageSize =10;//每页记录数
	  @NotBlank(message = "用户ID不能为空")
	  private String accountId;//用户ID

	  private Long accountIds;//用户ID
	  private String tenantId;
	  
	  
	  

}
