package com.dchealth.cro.medical.form;

import com.dchealth.cro.form.vo.MedicalFormVO;
import com.dchealth.cro.medical.domain.PatientRecordEvaluate;
import com.dchealth.cro.question.domain.QuestionQuick;
import com.dchealth.cro.question.vo.QuestionRecordVO;
import com.dchealth.cro.warn.domain.WarnEarlyRecord;
import com.dchealth.cro.warn.domain.WarnLeaveMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 患者档案详情Form
 */
@Data
public class PatientRecordDetailForm {

    /**
     * 患者档案ID
     */
    private Long recordId;

    /**
     * 表单
     */
    private List<MedicalFormVO> medicalFormVOS;

    /**
     * 质疑
     */
    private List<QuestionRecordVO> questionRecordVOS;

    /**
     * 随访意见
     */
    private String followOpinion;

    /**
     * 质效评价
     */
    private PatientRecordEvaluate patientRecordEvaluate;
    /**
     * 随访状态0无状态1待处理2进行中3已完成
     */
    private String followStatus;
    /**
     * 快捷回复
     */
    private List<QuestionQuick>  questionQuickList;
    /**
     * 回复时效1-5颗星
     */
    private String evaluatePrescription;
    /**
     * 回复质量1-5颗星
     */
    private String evaluateQuality;
    /**
     * 回复评价
     */
    private String evaluateReply;
    /**
     * 预警
     */
    private List<WarnEarlyRecord> warnEarlyRecordList;
    /**
     * 预警指导
     */
    private List<WarnLeaveMessage> warnLeaveMessageList;
    /** 患者ID */
    private Long patientId;

    private String personId;

    /** 患者姓名 */
    private String patientName;
    /** 手机号 */
    private String patientMobile;

    private String visitTime;
    /** 超窗状态  1更新随访时间2忽略本次3失访 */
    private String overTimeHandleStatus;
    /**
     * 失访状态 0不再参与 1失联 2其他--->失访原因0无失访1不在参与2失联3其他
     */
    private String loseType;
    /**
     * 失访原因
     */
    private String loseReason;

    private Long accountId;
    /**
     * 忽略本次的原因 0 患者本次不随访 1 患者已经完成随访
     */
    private String ignoreType;
    /**
     * 第一个处理质疑的医生ID
     */
    private String firstDoctorId;
    /**
     * 0 保存  1 提交
     */
    private Integer isSubmit;
    /**
     * 是否可编辑
     */
    private Boolean isEditable= true;


    /*
     * 预警状态0无预警1待处理2进行中3已完成
     */
    private String warnStatus;

    /*
     * 超窗状态0无超窗1待处理(超窗)2完成
     */
    private String overTimeStatus;

    private String stageName;

    private Integer sex;

    private String createTimeStr;

    private Long researchId;

    private String earlyWarnLevel;


    private Integer followTipStatus;


    //随访后台评价状态0无状态1待评价2已完成
    private String evaluateStatusBack;
    //后台回复时效1-5颗星
    private String  evaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String  evaluateQualityBack;
    //后台回复评价
    private String  evaluateReplyBack;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date evaluateReplyBackTime;


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

}
