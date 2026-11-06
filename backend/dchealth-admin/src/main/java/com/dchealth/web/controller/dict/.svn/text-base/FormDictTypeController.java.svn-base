package com.dchealth.web.controller.dict;

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
import com.dchealth.dict.domain.FormDictType;
import com.dchealth.dict.service.IFormDictTypeService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 字典类型Controller
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
@RestController
@RequestMapping("/dict/form_dict_type")
public class FormDictTypeController extends BaseController
{
    @Autowired
    private IFormDictTypeService formDictTypeService;

    /**
     * 查询字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(FormDictType formDictType)
    {
        startPage();
        List<FormDictType> list = formDictTypeService.selectFormDictTypeList(formDictType);
        return getDataTable(list);
    }

    /**
     * 导出字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('system:type:export')")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FormDictType formDictType)
    {
        List<FormDictType> list = formDictTypeService.selectFormDictTypeList(formDictType);
        ExcelUtil<FormDictType> util = new ExcelUtil<FormDictType>(FormDictType.class);
        util.exportExcel(response, list, "字典类型数据");
    }

    /**
     * 获取字典类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:type:query')")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable("dictId") Long dictId)
    {
        return AjaxResult.success(formDictTypeService.selectFormDictTypeByDictId(dictId));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FormDictType formDictType)
    {
        return toAjax(formDictTypeService.insertFormDictType(formDictType));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FormDictType formDictType)
    {
        return toAjax(formDictTypeService.updateFormDictType(formDictType));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:type:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dictIds}")
    public AjaxResult remove(@PathVariable Long[] dictIds)
    {
        return toAjax(formDictTypeService.deleteFormDictTypeByDictIds(dictIds));
    }
}
