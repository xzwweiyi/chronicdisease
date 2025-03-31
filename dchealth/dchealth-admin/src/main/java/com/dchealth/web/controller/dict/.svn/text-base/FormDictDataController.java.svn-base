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
import com.dchealth.dict.domain.FormDictData;
import com.dchealth.dict.service.IFormDictDataService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 字典数据Controller
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
@RestController
@RequestMapping("/dict/form_dict_data")
public class FormDictDataController extends BaseController
{
    @Autowired
    private IFormDictDataService formDictDataService;

    /**
     * 查询字典数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(FormDictData formDictData)
    {
        startPage();
        List<FormDictData> list = formDictDataService.selectFormDictDataList(formDictData);
        return getDataTable(list);
    }

    /**
     * 导出字典数据列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:export')")
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FormDictData formDictData)
    {
        List<FormDictData> list = formDictDataService.selectFormDictDataList(formDictData);
        ExcelUtil<FormDictData> util = new ExcelUtil<FormDictData>(FormDictData.class);
        util.exportExcel(response, list, "字典数据数据");
    }

    /**
     * 获取字典数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:data:query')")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable("dictCode") Long dictCode)
    {
        return AjaxResult.success(formDictDataService.selectFormDictDataByDictCode(dictCode));
    }

    /**
     * 新增字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:data:add')")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FormDictData formDictData)
    {
        return toAjax(formDictDataService.insertFormDictData(formDictData));
    }

    /**
     * 修改字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:data:edit')")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FormDictData formDictData)
    {
        return toAjax(formDictDataService.updateFormDictData(formDictData));
    }

    /**
     * 删除字典数据
     */
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "字典数据", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes)
    {
        return toAjax(formDictDataService.deleteFormDictDataByDictCodes(dictCodes));
    }
}
