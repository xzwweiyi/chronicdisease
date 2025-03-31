package com.dchealth.cro.health.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class DetailForm {
	@NotNull(message = "就诊ID不能为空")
	private String visitId;// 就诊ID
	@NotNull(message = "用户ID不能为空")
	private String personId;
	@NotNull(message = "查询类型不能为空")
	private String type;// 1= 健康档案  2=体检 4=预住院
}
