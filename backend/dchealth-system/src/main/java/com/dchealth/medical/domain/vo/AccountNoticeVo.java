package com.dchealth.medical.domain.vo;

import lombok.Data;

@Data
public class AccountNoticeVo {

	//超窗用到
	private Long recordId;

	//6随访提醒时需要
	private Long researchId;

	//6随访提醒时需要
	private Long patientId;

	//超窗时用到
	private String stageName;


	//1:通知2:公告3:个人消息4:档案消息5:质疑消息6:患者随访提醒7:患者超窗提醒8:患者预警提醒9:患者随访意见提醒10:患者质疑提醒11:患者入组提醒
	// 12:患者质疑未处理13:患者预警未处理14:患者随访未提交15:医生满意度评价16:医生随访处理任务提醒17:医生超窗任务提醒18:医生预警处理任务提醒
	private String type;


	private String recordNum; // 周期数
	//	private String templateType;
    //	private Long questionId;
    //	private String questionType;
}
