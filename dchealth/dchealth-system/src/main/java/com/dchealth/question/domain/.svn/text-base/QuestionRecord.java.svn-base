package com.dchealth.question.domain;

import java.util.Date;

import com.dchealth.question.domain.form.QuestionForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 质疑对象 question_record
 * @author lchbu
 * @date 2022-09-28
 */
@Data
public class QuestionRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 质疑id */
    private Long questionId;

    /** 档案id */
    @Excel(name = "档案id")
    private Long recordId;

    /** 发送人 */
    @Excel(name = "发送人")
    private Long fromUserId;

    /** 发送内容 */
    @Excel(name = "发送内容")
    private String fromContent;

    /** 回复人 */
    @Excel(name = "回复人")
    private Long replyUserId;

    /** 回复内容 */
    @Excel(name = "回复内容")
    private String replyContent;

    /** 本条质疑状态1待处理2进行中3完成 */
    @Excel(name = "本条质疑状态1待处理2进行中3完成")
    private String questionStatus;

    /** ： 区分质疑类型 1 app质疑  2 edc后台质疑 */
    @Excel(name = "区分质疑类型： 1 app质疑  2 edc后台质疑")
    private String type;

    /** 提出质疑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提出质疑时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;

    /** 回复质疑时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "回复质疑时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date replyTime;

    /**表单id*/
    private Long medicalFormId;

    private String fromUserName;
    private String replyUserName;

    private boolean canReplay=false;
    private String edcStatus;

    public QuestionRecord(QuestionForm questionForm){
        this.recordId = questionForm.getRecordId();
        this.fromUserId = questionForm.getFromUserId();
        this.fromContent = questionForm.getFromContent();
        this.fromTime = new Date();
    }

    public QuestionRecord(){

    }

}
