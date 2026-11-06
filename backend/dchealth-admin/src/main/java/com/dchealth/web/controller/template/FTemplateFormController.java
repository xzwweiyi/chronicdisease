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
import com.dchealth.template.domain.FTemplateForm;
import com.dchealth.template.service.IFTemplateFormService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 单模板-表單信息Controller
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@RestController
@RequestMapping("/template/f_template_form")
public class FTemplateFormController extends BaseController
{
    @Autowired
    private IFTemplateFormService fTemplateFormService;

    /**
     * 查询单模板列表
     */
    @PreAuthorize("@ss.hasPermi('base:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateForm fTemplateForm)
    {
        fTemplateForm.setParentId(0l);
        List<FTemplateForm> list = fTemplateFormService.selectFTemplateFormList(fTemplateForm);
        return getDataTable(list);
    }
    /**
     * 复制单模板
     */
    @PreAuthorize("@ss.hasPermi('base:template:add')")
    @Log(title = "复制单模板", businessType = BusinessType.INSERT)
    @PostMapping(value = "/{templateFormId}/{templateId}")
    public AjaxResult copy(@PathVariable("templateId") Long templateId,@PathVariable("templateFormId") Long templateFormId)
    {
        return toAjax(fTemplateFormService.copyFTemplateForm(templateId,templateFormId));
    }
    /**
     * 导出单模板列表
     */
    @PreAuthorize("@ss.hasPermi('base:template:export')")
    @Log(title = "单模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateForm fTemplateForm)
    {
        List<FTemplateForm> list = fTemplateFormService.selectFTemplateFormList(fTemplateForm);
        ExcelUtil<FTemplateForm> util = new ExcelUtil<FTemplateForm>(FTemplateForm.class);
        util.exportExcel(response, list, "单模板数据");
    }

    /**
     * 获取单模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:template:query')")
    @GetMapping(value = "/{templateFormId}")
    public AjaxResult getInfo(@PathVariable("templateFormId") Long templateFormId)
    {
        return AjaxResult.success(fTemplateFormService.selectFTemplateFormByTemplateFormId(templateFormId));
    }

    /**
     * 获取单模表单板详细信息
     */
    @GetMapping(value = "/detail/{templateFormId}")
    public AjaxResult detail(@PathVariable("templateFormId") Long templateFormId)
    {
        return AjaxResult.success(fTemplateFormService.detail(templateFormId));
    }

    /**
     * 新增单模板
     */
    @PreAuthorize("@ss.hasPermi('base:template:add')")
    @Log(title = "单模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplateForm fTemplateForm)
    {
        return toAjax(fTemplateFormService.insertFTemplateForm(fTemplateForm));
    }

    /**
     * 修改单模板
     */
    @PreAuthorize("@ss.hasPermi('base:template:edit')")
    @Log(title = "单模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplateForm fTemplateForm)
    {
        return toAjax(fTemplateFormService.updateFTemplateForm(fTemplateForm));
    }

    /**
     * 删除单模板
     */
    @PreAuthorize("@ss.hasPermi('base:template:remove')")
    @Log(title = "单模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateFormIds}")
    public AjaxResult remove(@PathVariable Long[] templateFormIds)
    {
        return toAjax(fTemplateFormService.deleteFTemplateFormByTemplateFormIds(templateFormIds));
    }

    /**
     * 查询表单信息及表单属性信息
     */
    @PreAuthorize("@ss.hasPermi('base:template:query')")
    @GetMapping(value = "/queryTemplateFrom/{templateId}")
    public AjaxResult queryTemplateFrom(@PathVariable("templateId") Long templateId){
        //根据模板id查询相关表单信息
        FTemplateForm fTemplateForm = fTemplateFormService.queryTemplateFromByTemId(templateId);
        return AjaxResult.success(fTemplateForm);
    }

}
