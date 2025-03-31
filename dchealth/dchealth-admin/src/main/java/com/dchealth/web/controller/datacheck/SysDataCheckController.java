package com.dchealth.web.controller.datacheck;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.datacheck.domain.SysDataCheck;
import com.dchealth.datacheck.service.ISysDataCheckService;
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
 * 数据检查Controller
 * 
 * @author lchbu
 * @date 2024-05-14
 */
@RestController
@RequestMapping("/tool/check")
public class SysDataCheckController extends BaseController
{
    @Autowired
    private ISysDataCheckService sysDataCheckService;

    /**
     * 查询数据检查列表
     */
    @PreAuthorize("@ss.hasPermi('system:check:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDataCheck sysDataCheck)
    {
        startPage();
        List<SysDataCheck> list = sysDataCheckService.selectSysDataCheckList(sysDataCheck);
        return getDataTable(list);
    }

    /**
     * 导出数据检查列表
     */
    @PreAuthorize("@ss.hasPermi('system:check:export')")
    @Log(title = "数据检查", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDataCheck sysDataCheck)
    {
        List<SysDataCheck> list = sysDataCheckService.selectSysDataCheckList(sysDataCheck);
        ExcelUtil<SysDataCheck> util = new ExcelUtil<SysDataCheck>(SysDataCheck.class);
        util.exportExcel(response, list, "数据检查数据");
    }

    /**
     * 获取数据检查详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:check:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysDataCheckService.selectSysDataCheckById(id));
    }

    /**
     * 新增数据检查
     */
    @PreAuthorize("@ss.hasPermi('system:check:add')")
    @Log(title = "数据检查", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDataCheck sysDataCheck)
    {
        return toAjax(sysDataCheckService.insertSysDataCheck(sysDataCheck));
    }

    /**
     * 修改数据检查
     */
    @PreAuthorize("@ss.hasPermi('system:check:edit')")
    @Log(title = "数据检查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDataCheck sysDataCheck)
    {
        return toAjax(sysDataCheckService.updateSysDataCheck(sysDataCheck));
    }

    /**
     * 删除数据检查
     */
    @PreAuthorize("@ss.hasPermi('system:check:remove')")
    @Log(title = "数据检查", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysDataCheckService.deleteSysDataCheckByIds(ids));
    }
}
