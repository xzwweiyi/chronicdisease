package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.template.domain.FMedicalExamSetEvaluation;
import com.dchealth.template.service.IFMedicalExamSetEvaluationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
 * 属性题目设置评价结果Controller
 * 
 * @author lchbu
 * @date 2023-04-03
 */
@RestController
@RequestMapping("/template/evaluation")
public class FMedicalExamSetEvaluationController extends BaseController
{
    @Autowired
    private IFMedicalExamSetEvaluationService fMedicalExamSetEvaluationService;

    /**
     * 查询属性题目设置评价结果列表
     */
    @PreAuthorize("@ss.hasPermi('template:evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        startPage();
        List<FMedicalExamSetEvaluation> list = fMedicalExamSetEvaluationService.selectFMedicalExamSetEvaluationList(fMedicalExamSetEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出属性题目设置评价结果列表
     */
    @PreAuthorize("@ss.hasPermi('template:evaluation:export')")
//    @Log(title = "属性题目设置评价结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        List<FMedicalExamSetEvaluation> list = fMedicalExamSetEvaluationService.selectFMedicalExamSetEvaluationList(fMedicalExamSetEvaluation);
        ExcelUtil<FMedicalExamSetEvaluation> util = new ExcelUtil<FMedicalExamSetEvaluation>(FMedicalExamSetEvaluation.class);
        util.exportExcel(response, list, "属性题目设置评价结果数据");
    }

    /**
     * 获取属性题目设置评价结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:evaluation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fMedicalExamSetEvaluationService.selectFMedicalExamSetEvaluationById(id));
    }

    /**
     * 新增属性题目设置评价结果
     */
    @PreAuthorize("@ss.hasPermi('template:evaluation:add')")
//    @Log(title = "属性题目设置评价结果", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        fMedicalExamSetEvaluation.setCreateBy(getUsername());
        return toAjax(fMedicalExamSetEvaluationService.insertFMedicalExamSetEvaluation(fMedicalExamSetEvaluation));
    }

    /**
     * 修改属性题目设置评价结果
     */
    @PreAuthorize("@ss.hasPermi('template:evaluation:edit')")
//    @Log(title = "属性题目设置评价结果", businessType = BusinessType.UPDATE)
//    @PutMapping
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody FMedicalExamSetEvaluation fMedicalExamSetEvaluation)
    {
        return toAjax(fMedicalExamSetEvaluationService.updateFMedicalExamSetEvaluation(fMedicalExamSetEvaluation));
    }

    @PreAuthorize("@ss.hasPermi('template:evaluation:delete')")
    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fMedicalExamSetEvaluationService.deleteFMedicalExamSetEvaluationById(id));
    }

    /**
     * 删除属性题目设置评价结果
     */
    @PreAuthorize("@ss.hasPermi('template:evaluation:remove')")
    @Log(title = "属性题目设置评价结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fMedicalExamSetEvaluationService.deleteFMedicalExamSetEvaluationByIds(ids));
    }
}
