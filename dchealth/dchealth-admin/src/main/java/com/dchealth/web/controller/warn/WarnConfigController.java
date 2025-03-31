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
import com.dchealth.warn.domain.WarnConfig;
import com.dchealth.warn.service.IWarnConfigService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 预警信息Controller
 * 
 * @author lchbu
 * @date 2022-08-30
 */
@RestController
@RequestMapping("/warn/config")
public class WarnConfigController extends BaseController
{
    @Autowired
    private IWarnConfigService warnConfigService;

    /**
     * 查询预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('warn:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(WarnConfig warnConfig)
    {
        startPage();
        List<WarnConfig> list = warnConfigService.selectWarnConfigList(warnConfig);
        return getDataTable(list);
    }

    /**
     * 导出预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('warn:config:export')")
    @Log(title = "预警信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WarnConfig warnConfig)
    {
        List<WarnConfig> list = warnConfigService.selectWarnConfigList(warnConfig);
        ExcelUtil<WarnConfig> util = new ExcelUtil<WarnConfig>(WarnConfig.class);
        util.exportExcel(response, list, "预警信息数据");
    }

    /**
     * 获取预警信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('warn:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(warnConfigService.selectWarnConfigById(id));
    }

    /**
     * 新增预警信息
     */
    @PreAuthorize("@ss.hasPermi('warn:config:add')")
    @Log(title = "预警信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WarnConfig warnConfig)
    {
        return toAjax(warnConfigService.insertWarnConfig(warnConfig));
    }

    /**
     * 修改预警信息
     */
    @PreAuthorize("@ss.hasPermi('warn:config:edit')")
    @Log(title = "预警信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WarnConfig warnConfig)
    {
        return toAjax(warnConfigService.updateWarnConfig(warnConfig));
    }

    /**
     * 删除预警信息
     */
    @PreAuthorize("@ss.hasPermi('warn:config:remove')")
    @Log(title = "预警信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(warnConfigService.deleteWarnConfigByIds(ids));
    }
}
