package com.dchealth.web.controller.question;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.form.SaveQuestionRecordVo;
import com.dchealth.question.service.IQuestionRecordService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 质疑Controller
 * 
 * @author lchbu
 * @date 2023-02-23
 */
@RestController
@RequestMapping("/questionRecord")
public class QuestionRecordController extends BaseController
{
    @Autowired
    private IQuestionRecordService questionRecordService;

    /**
     * 查询质疑列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(QuestionRecord questionRecord)
    {
        startPage();
        List<QuestionRecord> list = questionRecordService.selectQuestionRecordList(questionRecord);
        return getDataTable(list);
    }

    /**
     * 导出质疑列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "质疑", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QuestionRecord questionRecord)
    {
        List<QuestionRecord> list = questionRecordService.selectQuestionRecordList(questionRecord);
        ExcelUtil<QuestionRecord> util = new ExcelUtil<QuestionRecord>(QuestionRecord.class);
        util.exportExcel(response, list, "质疑数据");
    }

    /**
     * 获取质疑详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{questionId}")
    public AjaxResult getInfo(@PathVariable("questionId") Long questionId)
    {
        return AjaxResult.success(questionRecordService.selectQuestionRecordByQuestionId(questionId));
    }

    /**
     * 新增质疑
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "质疑", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QuestionRecord questionRecord)
    {
        return toAjax(questionRecordService.insertQuestionRecord(questionRecord));
    }


    //随访工作站随访质疑
    @PreAuthorize("@ss.hasPermi('system:record:saveQuestionRecord')")
    @PostMapping("/saveQuestionRecord")
    public AjaxResult saveQuestionRecord(@RequestBody SaveQuestionRecordVo saveQuestionRecordVo)
    {
        return toAjax(questionRecordService.saveQuestionRecord(saveQuestionRecordVo));
    }


    /**
     * 修改质疑
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "质疑", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QuestionRecord questionRecord)
    {
        return toAjax(questionRecordService.updateQuestionRecord(questionRecord));
    }

    /**
     * 删除质疑
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "质疑", businessType = BusinessType.DELETE)
	@DeleteMapping("/{questionIds}")
    public AjaxResult remove(@PathVariable Long[] questionIds)
    {
        return toAjax(questionRecordService.deleteQuestionRecordByQuestionIds(questionIds));
    }
}
