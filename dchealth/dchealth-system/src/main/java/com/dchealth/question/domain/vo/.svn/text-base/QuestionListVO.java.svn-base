package com.dchealth.question.domain.vo;

import com.dchealth.question.domain.QuestionRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 质疑集合
 */
@Data
public class QuestionListVO {

    /** 质疑id */
    private Long questionId;

    /** 发送内容 */
    private String fromContent;

    /** 回复内容 */
    private String replyContent;

    /** 提出质疑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;

    /** 回复质疑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    private String fromUserName;
    private String replyUserName;

    public QuestionListVO(QuestionRecord questionRecord){
        this.questionId = questionRecord.getQuestionId();
        this.fromContent = questionRecord.getFromContent();
        this.replyContent = questionRecord.getReplyContent();
        this.fromTime = questionRecord.getFromTime();
        this.replyTime = questionRecord.getReplyTime();
    }
}
