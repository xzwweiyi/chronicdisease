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
import com.dchealth.template.domain.FTemplateWarnConfig;
import com.dchealth.template.service.IFTemplateWarnConfigService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 模板预警信息Controller
 * 
 * @author lchbu
 * @date 2022-08-31
 */
@RestController
@RequestMapping("/template/config")
public class FTemplateWarnConfigController extends BaseController
{
    @Autowired
    private IFTemplateWarnConfigService fTemplateWarnConfigService;

    /**
     * 查询模板预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(FTemplateWarnConfig fTemplateWarnConfig)
    {
        startPage();
        List<FTemplateWarnConfig> list = fTemplateWarnConfigService.selectFTemplateWarnConfigList(fTemplateWarnConfig);
        return getDataTable(list);
    }

    /**
     * 导出模板预警信息列表
     */
    @PreAuthorize("@ss.hasPermi('base:template:export')")
    @Log(title = "模板预警信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FTemplateWarnConfig fTemplateWarnConfig)
    {
        List<FTemplateWarnConfig> list = fTemplateWarnConfigService.selectFTemplateWarnConfigList(fTemplateWarnConfig);
        ExcelUtil<FTemplateWarnConfig> util = new ExcelUtil<FTemplateWarnConfig>(FTemplateWarnConfig.class);
        util.exportExcel(response, list, "模板预警信息数据");
    }

    /**
     * 获取模板预警信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('base:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(fTemplateWarnConfigService.selectFTemplateWarnConfigById(id));
    }

    /**
     * 新增模板预警信息
     */
    @PreAuthorize("@ss.hasPermi('base:template:add')")
    @Log(title = "模板预警信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FTemplateWarnConfig fTemplateWarnConfig)
    {
        return toAjax(fTemplateWarnConfigService.insertFTemplateWarnConfig(fTemplateWarnConfig));
    }

    /**
     * 修改模板预警信息
     */
    @PreAuthorize("@ss.hasPermi('base:template:edit')")
    @Log(title = "模板预警信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FTemplateWarnConfig fTemplateWarnConfig)
    {
        return toAjax(fTemplateWarnConfigService.updateFTemplateWarnConfig(fTemplateWarnConfig));
    }

    /**
     * 删除模板预警信息
     */
    @PreAuthorize("@ss.hasPermi('base:template:remove')")
    @Log(title = "模板预警信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(fTemplateWarnConfigService.deleteFTemplateWarnConfigByIds(ids));
    }
}
