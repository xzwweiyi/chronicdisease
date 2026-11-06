package com.dchealth.compatibility.from;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class HealthListSearchForm {

	@NotNull(message = "就诊患者ID不能为空")
	private String personId;

	private String patientId;

	private String type = "1";//1：健康档案 2：体检报告
}
