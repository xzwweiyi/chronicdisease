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
import com.dchealth.template.domain.FLibraryForm;
import com.dchealth.template.service.IFLibraryFormService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 基础表单Controller
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
@RestController
@RequestMapping("/template/f_library_form")
public class FLibraryFormController extends BaseController
{
    @Autowired
    private IFLibraryFormService fLibraryFormService;

    /**
     * 查询属性库单列表
     */
    @PreAuthorize("@ss.hasPermi('base:library:list')")
    @GetMapping("/list")
    public TableDataInfo list(FLibraryForm fLibraryForm)
    {
        startPage();
        List<FLibraryForm> list = fLibraryFormService.selectFLibraryFormList(fLibraryForm);
        return getDataTable(list);
    }

    /**
     * 标准填写项/拍照项组件库
     */
    @GetMapping("/listAll")
    public AjaxResult listAll(FLibraryForm fLibraryForm)
    {
        List<FLibraryForm> list = fLibraryFormService.selectFLibraryFormList(fLibraryForm);
        return AjaxResult.success(list);
    }

    /**
     * 导出属性库单列表
     */
    @PreAuthorize("@ss.hasPermi('base:library:export')")
    @Log(title = "基础表单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FLibraryForm fLibraryForm)
    {
        List<FLibraryForm> list = fLibraryFormService.selectFLibraryFormList(fLibraryForm);
        ExcelUtil<FLibraryForm> util = new ExcelUtil<FLibraryForm>(FLibraryForm.class);
        util.exportExcel(response, list, "基础表单数据");
    }

    /**
     * 获取属性库单详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:library:query')")
    @GetMapping(value = "/{libraryId}")
    public AjaxResult getInfo(@PathVariable("libraryId") Long libraryId)
    {
        return AjaxResult.success(fLibraryFormService.selectFLibraryFormByLibraryId(libraryId));
    }

    /**
     * 表单库点击事件
     */
    @GetMapping(value = {"/childFormAndProperty/{libraryId}","/childFormAndProperty/{libraryId}/{formNameSearch}"})
    public AjaxResult childFormAndProperty(@PathVariable("libraryId") Long libraryId,@PathVariable(value = "formNameSearch", required = false) String formNameSearch)
    {
        return AjaxResult.success(fLibraryFormService.childFormAndProperty(libraryId,formNameSearch));
    }

    /**
     * 新增属性库单
     */
    @PreAuthorize("@ss.hasPermi('base:library:add')")
    @Log(title = "基础表单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FLibraryForm fLibraryForm)
    {
        return toAjax(fLibraryFormService.insertFLibraryForm(fLibraryForm));
    }

    /**
     * 修改属性库单
     */
    @PreAuthorize("@ss.hasPermi('base:library:edit')")
    @Log(title = "基础表单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FLibraryForm fLibraryForm)
    {
        return toAjax(fLibraryFormService.updateFLibraryForm(fLibraryForm));
    }

    /**
     * 删除属性库单
     */
    @PreAuthorize("@ss.hasPermi('base:library:remove')")
    @Log(title = "基础表单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{libraryIds}")
    public AjaxResult remove(@PathVariable Long[] libraryIds)
    {
//        return toAjax(fLibraryFormService.deleteFLibraryFormByLibraryIds(libraryIds));
        return toAjax(fLibraryFormService.updateDelFlagByLibraryIds(libraryIds));
    }
}
