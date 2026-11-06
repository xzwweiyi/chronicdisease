package com.dchealth.question.service;

import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.form.QuestionForm;
import com.dchealth.question.domain.form.QuestionQuickForm;
import com.dchealth.question.domain.vo.QuestionListVO;
import com.dchealth.question.domain.vo.QuestionQuickListVO;

import java.util.List;

public interface IQuestionService {

    /**
     * 快捷回复
     * @return
     */
    public List<QuestionQuickListVO> quickList(Long userId);

    /**
     * 保存快捷回复
     * @param questionQuickForm
     * @return
     */
    public int quickSave(QuestionQuickForm questionQuickForm);

    /**
     * 删除快捷回复
     * @param quickId
     * @return
     */
    public int quickDelete(Long quickId);

    /**
     * 质疑列表
     * @param recordId
     * @return
     */
    public List<QuestionListVO> questionList(Long recordId);

    /**
     * 发送质疑
     * @param questionForm
     * @return
     */
    public int sendQuestion(QuestionForm questionForm);

    public int saveQuestionRecord(QuestionRecord questionRecord);

    public List<QuestionRecord> listQuestionRecord(QuestionRecord questionRecord);

    int editPatientRecordEdcStatus(Long recordId,Long medicalFormId);
}
