package com.dchealth.medical.domain.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class EvaluateWorkForm {

    /** 档案id */
    private Long recordId;

    /**
     * 1 后台随访评价 2 app端评价 3  后台超窗评价  4  后台预警评价
     */
    private String belongType;

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
     * 更新人
     */
    private String updateBy;



    //后台评价状态0无状态1待评价2已完成
    private String evaluateStatusBack;
    //后台回复时效1-5颗星
    private String evaluatePrescriptionBack;
    //后台回复质量1-5颗星
    private String evaluateQualityBack;
    //后台回复评价
    private String evaluateReplyBack;
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
