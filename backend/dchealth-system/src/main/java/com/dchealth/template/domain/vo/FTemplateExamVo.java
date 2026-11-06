package com.dchealth.template.domain.vo;

import com.dchealth.template.domain.FTemplateExamSubject;

import java.util.List;

/**
 * 自评量表参数
 */
public class FTemplateExamVo {

    /** 模板ID */
    private Long templateFormId;

    /**
     * 自评量表题干、选项信息
     */
    private List<FTemplateExamSubject> examSubjectList;

    public Long getTemplateFormId() {
        return templateFormId;
    }

    public void setTemplateFormId(Long templateFormId) {
        this.templateFormId = templateFormId;
    }

    public List<FTemplateExamSubject> getExamSubjectList() {
        return examSubjectList;
    }

    public void setExamSubjectList(List<FTemplateExamSubject> examSubjectList) {
        this.examSubjectList = examSubjectList;
    }
}
