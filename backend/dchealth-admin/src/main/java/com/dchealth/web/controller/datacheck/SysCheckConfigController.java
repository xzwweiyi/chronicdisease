package com.dchealth.web.controller.datacheck;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.datacheck.domain.SysCheckConfig;
import com.dchealth.datacheck.service.ISysCheckConfigService;
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
 * 数据检查配置Controller
 * 
 * @author lchbu
 * @date 2024-05-14
 */
@RestController
@RequestMapping("/tool/config")
public class SysCheckConfigController extends BaseController
{
    @Autowired
    private ISysCheckConfigService sysCheckConfigService;

    /**
     * 查询数据检查配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCheckConfig sysCheckConfig)
    {
        startPage();
        List<SysCheckConfig> list = sysCheckConfigService.selectSysCheckConfigList(sysCheckConfig);
        return getDataTable(list);
    }

    /**
     * 导出数据检查配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @Log(title = "数据检查配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCheckConfig sysCheckConfig)
    {
        List<SysCheckConfig> list = sysCheckConfigService.selectSysCheckConfigList(sysCheckConfig);
        ExcelUtil<SysCheckConfig> util = new ExcelUtil<SysCheckConfig>(SysCheckConfig.class);
        util.exportExcel(response, list, "数据检查配置数据");
    }

    /**
     * 获取数据检查配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysCheckConfigService.selectSysCheckConfigById(id));
    }

    /**
     * 新增数据检查配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "数据检查配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCheckConfig sysCheckConfig)
    {
        return toAjax(sysCheckConfigService.insertSysCheckConfig(sysCheckConfig));
    }

    /**
     * 修改数据检查配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "数据检查配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCheckConfig sysCheckConfig)
    {
        return toAjax(sysCheckConfigService.updateSysCheckConfig(sysCheckConfig));
    }

    /**
     * 删除数据检查配置
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "数据检查配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysCheckConfigService.deleteSysCheckConfigByIds(ids));
    }
}
