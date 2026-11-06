package com.dchealth.compatibility.from;

import javax.validation.constraints.NotNull;

import lombok.Data;

import java.util.Date;

@Data
public class DetailForm {
	@NotNull(message = "就诊ID不能为空")
	private String visitId;// 就诊ID
	@NotNull(message = "用户ID不能为空")
	private String personId;
	@NotNull(message = "查询类型不能为空")
	private String type;// 1= 健康档案  2=体检

	/**
	 * 后台数据编码
	 */
	private String visitType;// 就诊类型, 1住院 2门诊 3体检	4预住院

	private String patientId;

	/**
	 * 检验检查日期
	 */
	private Date checkDate;
}
