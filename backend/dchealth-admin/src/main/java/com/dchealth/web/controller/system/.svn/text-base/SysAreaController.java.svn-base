package com.dchealth.web.controller.system;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.system.domain.SysArea;
import com.dchealth.system.service.ISysAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 地区码Controller
 *
 * @author ruoyi
 * @date 2022-03-18
 */
@RestController
@RequestMapping("/system/area")
public class SysAreaController extends BaseController {
    @Autowired
    private ISysAreaService sysAreaService;

    /**
     * 查询地区码列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysArea sysArea) {
        startPage();
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        return getDataTable(list);
    }

    /**
     * 导出地区码列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:export')")
    @Log(title = "地区码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysArea sysArea) {
        List<SysArea> list = sysAreaService.selectSysAreaList(sysArea);
        ExcelUtil<SysArea> util = new ExcelUtil<SysArea>(SysArea.class);
        util.exportExcel(response, list, "地区码数据");
    }

    /**
     * 获取地区码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:area:query')")
    @GetMapping(value = "/{areaId}")
    public AjaxResult getInfo(@PathVariable("areaId") Long areaId) {
        return AjaxResult.success(sysAreaService.selectSysAreaByAreaId(areaId));
    }

    /**
     * 新增地区码
     */
    @PreAuthorize("@ss.hasPermi('system:area:add')")
    @Log(title = "地区码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysArea sysArea) {
        return toAjax(sysAreaService.insertSysArea(sysArea));
    }

    /**
     * 修改地区码
     */
    @PreAuthorize("@ss.hasPermi('system:area:edit')")
    @Log(title = "地区码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysArea sysArea) {
        return toAjax(sysAreaService.updateSysArea(sysArea));
    }

    /**
     * 删除地区码
     */
    @PreAuthorize("@ss.hasPermi('system:area:remove')")
    @Log(title = "地区码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{areaIds}")
    public AjaxResult remove(@PathVariable Long[] areaIds) {
        return toAjax(sysAreaService.deleteSysAreaByAreaIds(areaIds));
    }

    /***
     *
     * 说明：动态查询省市区--二三级数据查询
     * @return java.util.List
     */
    @PostMapping("/findAreaListByParentId")
    public List<SysArea> findAreaListByParentId(String areaId) {
        if (null == areaId || "".equals(areaId)) {
            return sysAreaService.findAreaListByParentId(1L);
        }
        return sysAreaService.findAreaListByParentId(Long.valueOf(areaId));
    }

    /**
     * 加载地区码树列表
     */
    @PreAuthorize("@ss.hasPermi('system:area:list')")
    @GetMapping("/regionTreeData")
    @ResponseBody
    public AjaxResult regionTreeData() {
        List<SysArea> ztrees = sysAreaService.selectSysAreaTree();
        return AjaxResult.success(ztrees);
    }
}
