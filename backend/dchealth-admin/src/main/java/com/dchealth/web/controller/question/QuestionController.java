package com.dchealth.web.controller.question;

import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.form.QuestionForm;
import com.dchealth.question.domain.form.QuestionQuickForm;
import com.dchealth.question.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 质疑
 */
@RestController
@RequestMapping("/question")
public class QuestionController extends BaseController {

    @Autowired
    private IQuestionService questionService;


    /**
     * 快捷回复列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:quick:list')")
    @GetMapping("/quick/list")
    public AjaxResult quickList()
    {
        return AjaxResult.success(questionService.quickList(getUserId()));
    }

    /**
     * 快捷回复保存
     * @return
     */
    @PostMapping("/quick/save")
    public AjaxResult quickSave(@RequestBody QuestionQuickForm questionQuickForm)
    {
        questionQuickForm.setUserId(getUserId());
        questionQuickForm.setDeptId(getDeptId());
        return AjaxResult.success(questionService.quickSave(questionQuickForm));
    }

    /**
     * 快捷回复删除
     * @param quickId
     * @return
     */
    @DeleteMapping("/quick/delete/{quickId}")
    public AjaxResult quickDelete(@PathVariable Long quickId)
    {
        return toAjax(questionService.quickDelete(quickId));
    }

    /**
     * 质疑列表
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:list')")
    @GetMapping("/qt/list/{recordId}")
    public AjaxResult questionList(@PathVariable Long recordId)
    {
        return AjaxResult.success(questionService.questionList(recordId));
    }

    /**
     * 发质疑
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:save')")
    @PostMapping("/qt/save")
    public AjaxResult questionSave(@RequestBody QuestionForm questionForm)
    {
        questionForm.setFromUserId(getUserId());
        return AjaxResult.success(questionService.sendQuestion(questionForm));
    }

    /**
     * 随访设置发起质疑
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:save')")
    @PostMapping("/saveQuestionRecord")
    public AjaxResult saveQuestionRecord(@RequestBody QuestionRecord questionRecord)
    {
        questionRecord.setFromUserId(getUserId());
        return AjaxResult.success(questionService.saveQuestionRecord(questionRecord));
    }

    /**
     * edc发起质疑,区分提问和回复权限
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:saveedcask')")
    @PostMapping("/saveEdcAskQuestionRecord")
    public AjaxResult saveEdcAskQuestionRecord(@RequestBody QuestionRecord questionRecord)
    {
        questionRecord.setFromUserId(getUserId());
        return AjaxResult.success(questionService.saveQuestionRecord(questionRecord));
    }

    /**
     * edc发起质疑,区分提问和回复权限
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:saveedcreply')")
    @PostMapping("/saveEdcReplyQuestionRecord")
    public AjaxResult saveEdcReplyQuestionRecord(@RequestBody QuestionRecord questionRecord)
    {
        questionRecord.setFromUserId(getUserId());
        return AjaxResult.success(questionService.saveQuestionRecord(questionRecord));
    }

    /**
     * 随访设置发起质疑
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:list')")
    @PostMapping("/listQuestionRecord")
    public AjaxResult listQuestionRecord(@RequestBody QuestionRecord questionRecord)
    {
        return AjaxResult.success(questionService.listQuestionRecord(questionRecord));
    }

    /**
     * EDC 锁库
     * @return
     */
    @PreAuthorize("@ss.hasPermi('question:qt:edit')")
    @GetMapping(value = {"/editPatientRecordEdcStatus/{recordId}","/editPatientRecordEdcStatus/{recordId}/{medicalFormId}"})
    public AjaxResult editPatientRecordEdcStatus(@PathVariable(value = "recordId", required = true) Long recordId,
                                                 @PathVariable(value = "medicalFormId", required = false) Long medicalFormId)
    {
        return AjaxResult.success(questionService.editPatientRecordEdcStatus(recordId,medicalFormId));
    }

}
