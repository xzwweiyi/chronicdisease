package com.dchealth.web.controller.warn;

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
import com.dchealth.warn.domain.WarnTemplateEarly;
import com.dchealth.warn.service.IWarnTemplateEarlyService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 预警模板Controller
 * 
 * @author lchbu
 * @date 2023-01-09
 */
@RestController
@RequestMapping("/warn/template")
public class WarnTemplateEarlyController extends BaseController
{
    @Autowired
    private IWarnTemplateEarlyService warnTemplateEarlyService;

    /**
     * 查询预警模板列表
     */
    @PreAuthorize("@ss.hasPermi('warn:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(WarnTemplateEarly warnTemplateEarly)
    {
        startPage();
        List<WarnTemplateEarly> list = warnTemplateEarlyService.selectWarnTemplateEarlyList(warnTemplateEarly);
        return getDataTable(list);
    }

    /**
     * 导出预警模板列表
     */
    @PreAuthorize("@ss.hasPermi('warn:template:export')")
    @Log(title = "预警模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WarnTemplateEarly warnTemplateEarly)
    {
        List<WarnTemplateEarly> list = warnTemplateEarlyService.selectWarnTemplateEarlyList(warnTemplateEarly);
        ExcelUtil<WarnTemplateEarly> util = new ExcelUtil<WarnTemplateEarly>(WarnTemplateEarly.class);
        util.exportExcel(response, list, "预警模板数据");
    }

    /**
     * 获取预警模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('warn:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(warnTemplateEarlyService.selectWarnTemplateEarlyById(id));
    }

    /**
     * 新增预警模板
     */
    @PreAuthorize("@ss.hasPermi('warn:template:add')")
    @Log(title = "预警模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WarnTemplateEarly warnTemplateEarly)
    {
        return toAjax(warnTemplateEarlyService.insertWarnTemplateEarly(warnTemplateEarly));
    }

    /**
     * 修改预警模板
     */
    @PreAuthorize("@ss.hasPermi('warn:template:edit')")
    @Log(title = "预警模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WarnTemplateEarly warnTemplateEarly)
    {
        return toAjax(warnTemplateEarlyService.updateWarnTemplateEarly(warnTemplateEarly));
    }

    /**
     * 删除预警模板
     */
    @PreAuthorize("@ss.hasPermi('warn:template:remove')")
    @Log(title = "预警模板", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(warnTemplateEarlyService.deleteWarnTemplateEarlyByIds(ids));
    }
}
