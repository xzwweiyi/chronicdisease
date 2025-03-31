package com.dchealth.medical.domain.vo;

import com.dchealth.warn.domain.WarnEarlyRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 患者详情
 */
@Data
public class PatientDetailVO {

    /**
     * 患者id
     */
    private Long patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者code
     */
    private String patientCode;

    /**
     * 患者创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 性别（0:未知，1：男，2：女）
     */
    private Integer sex;

    /**待完善参数**/

    //随访意见
    private String followOpinion;
    //调整队列1保持原队列2更换队列
    private String adjustType;
    //计划队列
    private Long planResearchId;
    //研究名称
    private String planResearchName;
    //队列id
    private Long researchId;
    //表单模版内容
    private List<MedicalFormVO> medicalFormVOS;
    //表单（问卷）模版内容
    private List<MedicalFormVO> medicalFormExamVOS;
    //预警模版内容
    private List<WarnEarlyRecord> warnEarlyRecordList;


    //后台评价状态0无状态1待评价2已完成
    private String evaluateStatusBack;
    //后台回复时效1-5颗星
    private String evaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String evaluateQualityBack;
    //后台回复评价
    private String evaluateReplyBack;

    //评价状态0无状态1待评价2已完成
    private String evaluateStatus;
    //回复时效1-5颗星
    private String evaluatePrescription;
    //回复质量1-5颗星
    private String evaluateQuality;
    //回复评价
    private String evaluateReply;
    //是否忽略随访：0患者本次不随访 1患者已经完成随访
    private String ignoreType;

    //超窗处理状态0无1更新随访时间2忽略本次3失访
    private String overTimeHandleStatus;
    //失访原因0无失访1不在参与2失联3其他
    private String lossCause;
    //失访原因其他
    private String lossCauseOther;



    //告警后台评价状态0无状态1待评价2已完成
    private String warnEvaluateStatusBack;
    //后台回复时效1-5颗星
    private String  warnEvaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String  warnEvaluateQualityBack;
    //后台回复评价
    private String  warnEvaluateReplyBack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  warnEvaluateReplyBackTime;


    //超窗后台评价状态0无状态1待评价2已完成
    private String overEvaluateStatusBack;
    //后台回复时效1-5颗星
    private String  overEvaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String  overEvaluateQualityBack;
    //后台回复评价
    private String  overEvaluateReplyBack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  overEvaluateReplyBackTime;

    /**
     * 是否提交
     */
    private Boolean  submit;
}
