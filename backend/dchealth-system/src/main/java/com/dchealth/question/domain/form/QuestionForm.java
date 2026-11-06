package com.dchealth.question.domain.form;

import lombok.Data;

/**
 * 质疑form
 */
@Data
public class QuestionForm {

    /**
     * 提问人ID
     */
    private Long fromUserId;

    /**
     * 提问内容
     */
    private String fromContent;

    /**
     * 档案ID
     */
    private Long recordId;
}
