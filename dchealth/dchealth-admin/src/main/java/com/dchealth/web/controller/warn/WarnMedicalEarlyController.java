package com.dchealth.web.controller.warn;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.warn.service.IWarnMedicalEarlyConditionService;
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
import com.dchealth.warn.domain.WarnMedicalEarly;
import com.dchealth.warn.service.IWarnMedicalEarlyService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 预警表单Controller
 * 
 * @author lchbu
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/warn/medical")
public class WarnMedicalEarlyController extends BaseController
{
    @Autowired
    private IWarnMedicalEarlyService warnMedicalEarlyService;

    @Autowired
    private IWarnMedicalEarlyConditionService warnMedicalEarlyConditionService;

    /**
     * 查询预警表单列表
     */
    @PreAuthorize("@ss.hasPermi('warn:medical:list')")
    @GetMapping("/list")
    public TableDataInfo list(WarnMedicalEarly warnMedicalEarly)
    {
        startPage();
        List<WarnMedicalEarly> list = warnMedicalEarlyService.selectWarnMedicalEarlyList(warnMedicalEarly);
        return getDataTable(list);
    }

    /**
     * 导出预警表单列表
     */
    @PreAuthorize("@ss.hasPermi('warn:medical:export')")
    @Log(title = "预警表单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WarnMedicalEarly warnMedicalEarly)
    {
        List<WarnMedicalEarly> list = warnMedicalEarlyService.selectWarnMedicalEarlyList(warnMedicalEarly);
        ExcelUtil<WarnMedicalEarly> util = new ExcelUtil<WarnMedicalEarly>(WarnMedicalEarly.class);
        util.exportExcel(response, list, "预警表单数据");
    }

    /**
     * 获取预警表单详细信息
     */
    @PreAuthorize("@ss.hasPermi('warn:medical:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(warnMedicalEarlyService.selectWarnMedicalEarlyById(id));
    }

    /**
     * 新增预警表单
     */
    @PreAuthorize("@ss.hasPermi('warn:medical:add')")
    @Log(title = "预警表单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WarnMedicalEarly warnMedicalEarly)
    {
        return toAjax(warnMedicalEarlyService.insertWarnMedicalEarly(warnMedicalEarly));
    }

    /**
     * 修改预警表单
     */
    @PreAuthorize("@ss.hasPermi('warn:medical:edit')")
    @Log(title = "预警表单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WarnMedicalEarly warnMedicalEarly)
    {
        return toAjax(warnMedicalEarlyService.updateWarnMedicalEarly(warnMedicalEarly));
    }

    /**
     * 删除预警表单
     */
    @PreAuthorize("@ss.hasPermi('warn:medical:remove')")
    @Log(title = "预警表单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(warnMedicalEarlyService.deleteWarnMedicalEarlyByIds(ids));
    }

    /**
     * 删除预警表单规则
     */
    @DeleteMapping("condition/{id}")
    public AjaxResult conditionRemove(@PathVariable Long id)
    {
        return toAjax(warnMedicalEarlyConditionService.deleteWarnMedicalEarlyConditionById(id));
    }
}
