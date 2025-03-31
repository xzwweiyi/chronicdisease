package com.dchealth.web.controller.dict;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.dict.domain.FMedicalDictData;
import com.dchealth.dict.service.IFMedicalDictDataService;
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
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2022-06-06
 */
@RestController
@RequestMapping("/system/dict/medical/data")
public class FMedicalDictDataController extends BaseController
{
    @Autowired
    private IFMedicalDictDataService fMedicalDictDataService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('medical:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(FMedicalDictData fMedicalDictData)
    {
        startPage();
        List<FMedicalDictData> list = fMedicalDictDataService.selectFMedicalDictDataList(fMedicalDictData);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('medical:data:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FMedicalDictData fMedicalDictData)
    {
        List<FMedicalDictData> list = fMedicalDictDataService.selectFMedicalDictDataList(fMedicalDictData);
        ExcelUtil<FMedicalDictData> util = new ExcelUtil<FMedicalDictData>(FMedicalDictData.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fMedicalDictDataService.selectFMedicalDictDataById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('medical:data:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FMedicalDictData fMedicalDictData)
    {
        return toAjax(fMedicalDictDataService.insertFMedicalDictData(fMedicalDictData));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('medical:data:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FMedicalDictData fMedicalDictData)
    {
        return toAjax(fMedicalDictDataService.updateFMedicalDictData(fMedicalDictData));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('medical:data:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fMedicalDictDataService.deleteFMedicalDictDataByIds(ids));
    }
}

