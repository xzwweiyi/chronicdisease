package com.dchealth.cro.account.form;

import lombok.Data;

import java.util.List;

@Data
public class MessageVo {

	private String type;//1弹窗，2进队列，3展示toast

	private Long patientId;
	private Long researchId;

	private String content;

	private List<ConfirmEnrollmentVo> confirmEnrollmentVoList;//入组确认

}
