package com.dchealth.web.controller.dict;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.dict.domain.FMedicalDictType;
import com.dchealth.dict.service.IFMedicalDictTypeService;
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
 * 字典类型Controller
 *
 * @author ruoyi
 * @date 2022-06-06
 */
@RestController
@RequestMapping("/system/dict/medical/type")
public class FMedicalDictTypeController extends BaseController
{
    @Autowired
    private IFMedicalDictTypeService fMedicalDictTypeService;

    /**
     * 查询字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('medical:type:list')")
    @GetMapping("/list")
    public TableDataInfo list(FMedicalDictType fMedicalDictType)
    {
        startPage();
        List<FMedicalDictType> list = fMedicalDictTypeService.selectFMedicalDictTypeList(fMedicalDictType);
        return getDataTable(list);
    }

    /**
     * 导出字典类型列表
     */
    @PreAuthorize("@ss.hasPermi('medical:type:export')")
    @Log(title = "字典类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FMedicalDictType fMedicalDictType)
    {
        List<FMedicalDictType> list = fMedicalDictTypeService.selectFMedicalDictTypeList(fMedicalDictType);
        ExcelUtil<FMedicalDictType> util = new ExcelUtil<FMedicalDictType>(FMedicalDictType.class);
        util.exportExcel(response, list, "字典类型数据");
    }

    /**
     * 获取字典类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:type:query')")
    @GetMapping(value = "/{dictId}")
    public AjaxResult getInfo(@PathVariable("dictId") Long dictId)
    {
        return AjaxResult.success(fMedicalDictTypeService.selectFMedicalDictTypeByDictId(dictId));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('medical:type:add')")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FMedicalDictType fMedicalDictType)
    {
        return toAjax(fMedicalDictTypeService.insertFMedicalDictType(fMedicalDictType));
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('medical:type:edit')")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FMedicalDictType fMedicalDictType)
    {
        return toAjax(fMedicalDictTypeService.updateFMedicalDictType(fMedicalDictType));
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('medical:type:remove')")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] dictIds)
    {
        return toAjax(fMedicalDictTypeService.deleteFMedicalDictTypeByDictIds(dictIds));
    }
}

