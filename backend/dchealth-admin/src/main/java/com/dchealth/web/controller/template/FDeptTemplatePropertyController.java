package com.dchealth.web.controller.template;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.template.domain.FTemplateForm;
import com.dchealth.template.domain.FTemplateProperty;
import com.dchealth.template.service.IFTemplatePropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 表單屬性属性Controller
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@RestController
@RequestMapping("/template/dept/f_template_property")
public class FDeptTemplatePropertyController extends BaseController
{
    @Autowired
    private IFTemplatePropertyService fTemplatePropertyService;

    /**
     * 查询属性列表
     */
    @PreAuthorize("@ss.hasPermi('baseDept:templateProperty:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateProperty fTemplateProperty)
    {
        startPage();
        List<FTemplateProperty> list = fTemplatePropertyService.selectFTemplatePropertyList(fTemplateProperty);
        return getDataTable(list);
    }

    /**
     * 导出属性列表
     */
    @PreAuthorize("@ss.hasPermi('baseDept:templateProperty:export')")
    @Log(title = "属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateProperty fTemplateProperty)
    {
        List<FTemplateProperty> list = fTemplatePropertyService.selectFTemplatePropertyList(fTemplateProperty);
        ExcelUtil<FTemplateProperty> util = new ExcelUtil<FTemplateProperty>(FTemplateProperty.class);
        util.exportExcel(response, list, "属性数据");
    }

    /**
     * 获取属性详细信息
     */
    @PreAuthorize("@ss.hasPermi('baseDept:templateProperty:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fTemplatePropertyService.selectFTemplatePropertyById(id));
    }


    /**
     * 模板编辑内容保存入口
     */
    /*@PreAuthorize("@ss.hasPermi('template:property:edit')")
    @Log(title = "属性", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplate fTemplate)
    {
        return toAjax(fTemplatePropertyService.updateFTemplateProperty(fTemplate));
    }*/

    /**
     * 表单入口修改
     */
    @PreAuthorize("@ss.hasPermi('baseDept:templateProperty:edit')")
    @Log(title = "属性", businessType = BusinessType.UPDATE)
    @PutMapping("/formSubmit")
    public AjaxResult edit(@RequestBody FTemplateForm fTemplateForm)
    {
        return toAjax(fTemplatePropertyService.updateFTemplateProperty(fTemplateForm));
    }

    /**
     * 修改属性
     */
    @PreAuthorize("@ss.hasPermi('baseDept:template:edit')")
    @Log(title = "属性", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/only")
    public AjaxResult edit(@RequestBody FTemplateProperty fTemplateProperty)
    {
        return toAjax(fTemplatePropertyService.updateFTemplateProperty(fTemplateProperty));
    }

    /**
     * 删除属性
     */
    @PreAuthorize("@ss.hasPermi('baseDept:templateProperty:remove')")
    @Log(title = "属性", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fTemplatePropertyService.deleteFTemplatePropertyByIds(ids));
    }
}
