package com.dchealth.cro.account.form;

import lombok.Data;

import java.util.Date;

@Data
public class ConfirmEnrollmentVo {

	/**
	 * 弹框顺序
	 */
	private Integer order;

	/**
	 * 患者id
	 */
	private String patientId;

	/**
	 * 队列id
	 */
	private String researchId;

	/**
	 * 队列名称
	 */
	private String researchName;

	/**
	 * 队列描述
	 */
	private String researchDescription;

	/**
	 * 备注
	 */
	private String remark;

	private Date createTime;

	private Integer isread;

}
