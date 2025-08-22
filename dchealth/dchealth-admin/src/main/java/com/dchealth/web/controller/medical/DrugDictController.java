package com.dchealth.web.controller.medical;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.medical.domain.DrugDict;
import com.dchealth.medical.service.IDrugDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 药品字典Controller
 *
 * @author wangpf
 * @date 2025-08-13
 */
@RestController
@RequestMapping("/drug/dict")
public class DrugDictController extends BaseController {
    @Autowired
    private IDrugDictService drugDictService;

    /**
     * 查询药品字典列表
     */
//    @PreAuthorize("@ss.hasPermi('drug:dict:list')")
    @PostMapping("/list")
    public TableDataInfo list(@RequestBody DrugDict drugDict) {
        startPage();
        List<DrugDict> list = drugDictService.selectDrugDictList(drugDict);
        return getDataTable(list);
    }

    /**
     * 导出药品字典列表
     */
    @PreAuthorize("@ss.hasPermi('drug:dict:export')")
    @Log(title = "药品字典", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DrugDict drugDict) {
        List<DrugDict> list = drugDictService.selectDrugDictList(drugDict);
        ExcelUtil<DrugDict> util = new ExcelUtil<DrugDict>(DrugDict.class);
        util.exportExcel(response, list, "药品字典数据");
    }

    /**
     * 获取药品字典详细信息
     */
    @PreAuthorize("@ss.hasPermi('drug:dict:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(drugDictService.selectDrugDictById(id));
    }

    /**
     * 新增药品字典
     */
    @PreAuthorize("@ss.hasPermi('drug:dict:add')")
    @Log(title = "药品字典", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DrugDict drugDict) {
        return toAjax(drugDictService.insertDrugDict(drugDict));
    }

    /**
     * 修改药品字典
     */
    @PreAuthorize("@ss.hasPermi('drug:dict:edit')")
    @Log(title = "药品字典", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DrugDict drugDict) {
        return toAjax(drugDictService.updateDrugDict(drugDict));
    }

    /**
     * 删除药品字典
     */
    @PreAuthorize("@ss.hasPermi('drug:dict:remove')")
    @Log(title = "药品字典", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(drugDictService.deleteDrugDictByIds(ids));
    }
}
