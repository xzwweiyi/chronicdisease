package com.dchealth.question.domain.vo;

import com.dchealth.common.annotation.Excel;
import com.dchealth.question.domain.QuestionQuick;
import lombok.Data;

/**
 * 快捷回复列表
 */
@Data
public class QuestionQuickListVO {

    /** 快捷回复id */
    private Long questionQuickId;

    /** 快捷内容 */
    @Excel(name = "快捷内容")
    private String quickContent;

    public QuestionQuickListVO(QuestionQuick questionQuick){
        this.questionQuickId = questionQuick.getQuestionQuickId();
        this.quickContent = questionQuick.getQuickContent();
    }
}
