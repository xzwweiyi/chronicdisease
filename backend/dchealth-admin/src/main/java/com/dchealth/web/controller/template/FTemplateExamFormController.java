package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.dchealth.template.domain.FTemplateExamForm;
import com.dchealth.template.service.IFTemplateExamFormService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 单模板-自评量Controller
 * 
 * @author lchbu
 * @date 2022-11-11
 */
@RestController
@RequestMapping("/template/f_template_exam_form")
public class FTemplateExamFormController extends BaseController
{
    @Autowired
    private IFTemplateExamFormService fTemplateExamFormService;

    /**
     * 查询单模板-自评量列表
     */
    @PreAuthorize("@ss.hasPermi('template:examform:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateExamForm fTemplateExamForm)
    {
        startPage();
        List<FTemplateExamForm> list = fTemplateExamFormService.selectFTemplateExamFormList(fTemplateExamForm);
        return getDataTable(list);
    }

    /**
     * 导出单模板-自评量列表
     */
    @PreAuthorize("@ss.hasPermi('template:examform:export')")
    @Log(title = "单模板-自评量", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateExamForm fTemplateExamForm)
    {
        List<FTemplateExamForm> list = fTemplateExamFormService.selectFTemplateExamFormList(fTemplateExamForm);
        ExcelUtil<FTemplateExamForm> util = new ExcelUtil<FTemplateExamForm>(FTemplateExamForm.class);
        util.exportExcel(response, list, "单模板-自评量数据");
    }

    /**
     * 获取单模板-自评量详细信息
     */
    @PreAuthorize("@ss.hasPermi('template:examform:query')")
    @GetMapping(value = "/{templateFormId}")
    public AjaxResult getInfo(@PathVariable("templateFormId") Long templateFormId)
    {
        return AjaxResult.success(fTemplateExamFormService.selectFTemplateExamFormByTemplateFormId(templateFormId));
    }

    /**
     * 新增单模板-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examform:add')")
    @Log(title = "单模板-自评量", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplateExamForm fTemplateExamForm)
    {
        return toAjax(fTemplateExamFormService.insertFTemplateExamForm(fTemplateExamForm));
    }

    /**
     * 修改单模板-自评量表
     */
    @PreAuthorize("@ss.hasPermi('template:examform:edit')")
    @Log(title = "单模板-自评量", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplateExamForm fTemplateExamForm)
    {
        return toAjax(fTemplateExamFormService.updateFTemplateExamForm(fTemplateExamForm));
    }

    /**
     * 删除单模板-自评量
     */
    @PreAuthorize("@ss.hasPermi('template:examform:remove')")
    @Log(title = "单模板-自评量", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateFormIds}")
    public AjaxResult remove(@PathVariable Long[] templateFormIds)
    {
        return toAjax(fTemplateExamFormService.deleteFTemplateExamFormByTemplateFormIds(templateFormIds));
    }
}
