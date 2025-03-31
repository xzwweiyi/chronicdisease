package com.dchealth.cro.question.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 质疑对象 question_record
 * 
 * @author lchbu
 * @date 2022-11-09
 */
@Data
public class QuestionRecord
{
    private static final long serialVersionUID = 1L;

    /** 质疑id */
    private Long questionId;

    /** 档案id */
    private Long recordId;

    /** 发送人 */
    private Long fromUserId;

    /** 发送内容 */
    private String fromContent;

    /** 回复人 */
    private Long replyUserId;

    /** 回复内容 */
    private String replyContent;

    /** 本条质疑状态1待处理2进行中3完成 */
    private String questionStatus;

    /** 提出质疑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fromTime;

    /** 回复质疑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date replyTime;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 患者姓名
     */
    private String patientName;

    private String type; //1 app质疑  2 edc后台质疑'
}
