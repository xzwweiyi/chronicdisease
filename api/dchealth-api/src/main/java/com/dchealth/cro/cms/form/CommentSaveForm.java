package com.dchealth.cro.cms.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
@Data
public class CommentSaveForm {
	  @NotBlank(message = "评论用户不能为空")
	  private String accountId;//用户ID

	private Long accountIds;//用户ID
	  private String tenantId;
	  @NotNull(message = "资讯信息不能为空")
	  private Long articleId;
	  @NotNull(message = "评论内容不能为空")
	  private String content;
	  @NotNull(message = "用户端类型不能为空，1=医生端 2=患者端")
	  private String appType;
}
