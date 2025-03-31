package com.dchealth.medical.domain.vo;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.question.domain.QuestionRecord;
import lombok.Data;

@Data
public class QuestionRecordVO {

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 患者姓名
     */
    private String patientName;

    /** 提出质疑时间 */
    private String fromTime;

    /** 回复质疑时间 */
    private String replyTime;

    /** 发送内容 */
    private String fromContent;

    /** 回复内容 */
    private String replyContent;

    /**
     * 质疑ID
     */
    private Long questionId;

    public QuestionRecordVO(){}

    public QuestionRecordVO(QuestionRecord questionRecord){
        this.questionId = questionRecord.getQuestionId();
        /*this.doctorName = questionRecord.getDoctorName();
        this.patientName = questionRecord.getPatientName();*/
        this.fromContent = questionRecord.getFromContent();
        this.replyContent = questionRecord.getReplyContent();
        if (questionRecord.getFromTime() != null){
            this.fromTime = DateUtils.parseDateToStrFull(questionRecord.getFromTime());
        }
        if (questionRecord.getReplyTime() != null){
            this.replyTime = DateUtils.parseDateToStrFull(questionRecord.getReplyTime());
        }
    }

}
