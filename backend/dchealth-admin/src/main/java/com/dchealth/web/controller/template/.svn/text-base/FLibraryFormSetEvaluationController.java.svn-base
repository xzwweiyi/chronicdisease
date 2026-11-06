package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.template.domain.FLibraryFormSetEvaluation;
import com.dchealth.template.service.IFLibraryFormSetEvaluationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 标准量设置评价结果Controller
 * 
 * @author lchbu
 * @date 2023-04-03
 */
@RestController
@RequestMapping("/template/f_library_form_set_evaluation")
public class FLibraryFormSetEvaluationController extends BaseController
{
    @Autowired
    private IFLibraryFormSetEvaluationService fLibraryFormSetEvaluationService;

    /**
     * 查询标准量设置评价结果列表
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(FLibraryFormSetEvaluation fLibraryFormSetEvaluation)
    {
        startPage();
        List<FLibraryFormSetEvaluation> list = fLibraryFormSetEvaluationService.selectFLibraryFormSetEvaluationList(fLibraryFormSetEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出标准量设置评价结果列表
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:export')")
//    @Log(title = "标准量设置评价结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FLibraryFormSetEvaluation fLibraryFormSetEvaluation)
    {
        List<FLibraryFormSetEvaluation> list = fLibraryFormSetEvaluationService.selectFLibraryFormSetEvaluationList(fLibraryFormSetEvaluation);
        ExcelUtil<FLibraryFormSetEvaluation> util = new ExcelUtil<FLibraryFormSetEvaluation>(FLibraryFormSetEvaluation.class);
        util.exportExcel(response, list, "标准量设置评价结果数据");
    }

    /**
     * 获取标准量设置评价结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fLibraryFormSetEvaluationService.selectFLibraryFormSetEvaluationById(id));
    }

    /**
     * 新增标准量设置评价结果
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:add')")
//    @Log(title = "标准量设置评价结果", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody FLibraryFormSetEvaluation fLibraryFormSetEvaluation){
        fLibraryFormSetEvaluation.setCreateBy(getUsername());
        return toAjax(fLibraryFormSetEvaluationService.insertFLibraryFormSetEvaluation(fLibraryFormSetEvaluation));
    }

    /**
     * 修改标准量设置评价结果
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:edit')")
//    @Log(title = "标准量设置评价结果", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody FLibraryFormSetEvaluation fLibraryFormSetEvaluation){
        return toAjax(fLibraryFormSetEvaluationService.updateFLibraryFormSetEvaluation(fLibraryFormSetEvaluation));
    }

    /**
     * 根据ID删除
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:delete')")
//    @Log(title = "标准量设置评价结果", businessType = BusinessType.UPDATE)
    @GetMapping(value = "delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(fLibraryFormSetEvaluationService.deleteFLibraryFormSetEvaluationById(id));
    }


    /**
     * 删除标准量设置评价结果
     */
    @PreAuthorize("@ss.hasPermi('template:f_library_form_set_evaluation:remove')")
//    @Log(title = "标准量设置评价结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fLibraryFormSetEvaluationService.deleteFLibraryFormSetEvaluationByIds(ids));
    }
}
