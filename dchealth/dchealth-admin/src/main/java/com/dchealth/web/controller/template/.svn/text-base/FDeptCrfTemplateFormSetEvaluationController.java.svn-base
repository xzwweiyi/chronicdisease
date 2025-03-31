package com.dchealth.web.controller.template;

import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.template.domain.FTemplateFormSetEvaluation;
import com.dchealth.template.service.IFTemplateFormSetEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 单模板设置评测结果Controller
 * 
 * @author lchbu
 * @date 2023-04-04
 */
@RestController
@RequestMapping("/template/dept/f_template_form_set_evaluation")
public class FDeptCrfTemplateFormSetEvaluationController extends BaseController
{
    @Autowired
    private IFTemplateFormSetEvaluationService fTemplateFormSetEvaluationService;

    /**
     * 查询单模板设置评测结果列表
     */
    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateFormSetEvaluation fTemplateFormSetEvaluation){
        startPage();
        List<FTemplateFormSetEvaluation> list = fTemplateFormSetEvaluationService.selectFTemplateFormSetEvaluationList(fTemplateFormSetEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出单模板设置评测结果列表
     */
    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:export')")
//    @Log(title = "单模板设置评测结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateFormSetEvaluation fTemplateFormSetEvaluation){
        List<FTemplateFormSetEvaluation> list = fTemplateFormSetEvaluationService.selectFTemplateFormSetEvaluationList(fTemplateFormSetEvaluation);
        ExcelUtil<FTemplateFormSetEvaluation> util = new ExcelUtil<FTemplateFormSetEvaluation>(FTemplateFormSetEvaluation.class);
        util.exportExcel(response, list, "单模板设置评测结果数据");
    }

    /**
     * 获取单模板设置评测结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id){
        return AjaxResult.success(fTemplateFormSetEvaluationService.selectFTemplateFormSetEvaluationById(id));
    }

    /**
     * 新增单模板设置评测结果
     */
    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:add')")
//    @Log(title = "单模板设置评测结果", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FTemplateFormSetEvaluation fTemplateFormSetEvaluation){
        fTemplateFormSetEvaluation.setCreateBy(getUsername());
        return toAjax(fTemplateFormSetEvaluationService.insertFTemplateFormSetEvaluation(fTemplateFormSetEvaluation));
    }

    /**
     * 修改单模板设置评测结果
     */
    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:edit')")
//    @Log(title = "单模板设置评测结果", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody FTemplateFormSetEvaluation fTemplateFormSetEvaluation){
        return toAjax(fTemplateFormSetEvaluationService.updateFTemplateFormSetEvaluation(fTemplateFormSetEvaluation));
    }

    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:delete')")
    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(fTemplateFormSetEvaluationService.deleteFTemplateFormSetEvaluationById(id));
    }

    /**
     * 删除单模板设置评测结果
     */
    @PreAuthorize("@ss.hasPermi('templateDept:f_template_form_set_evaluation:remove')")
//    @Log(title = "单模板设置评测结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(fTemplateFormSetEvaluationService.deleteFTemplateFormSetEvaluationByIds(ids));
    }
}
