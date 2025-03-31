package com.dchealth.cro.health.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class HealthListSearchForm {
	private Integer pageNo =1;
	private Integer pageSize =10;
	@NotNull(message = "就诊患者ID不能为空")
	private String personId;
	private String type = "1";//1：健康档案 2：体检报告
	private Long patientId;
}
