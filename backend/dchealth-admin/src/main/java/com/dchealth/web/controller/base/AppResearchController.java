package com.dchealth.web.controller.base;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.base.domain.AppResearch;
import com.dchealth.base.service.IAppResearchService;
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
 * APP与队列关系Controller
 *
 * @author ruoyi
 * @date 2022-06-23
 */
@RestController
@RequestMapping("/app/research")
public class AppResearchController extends BaseController
{
    @Autowired
    private IAppResearchService appResearchService;

    /**
     * 查询APP 与队列关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:research:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppResearch appResearch)
    {
        startPage();
        List<AppResearch> list = appResearchService.selectAppResearchList(appResearch);
        return getDataTable(list);
    }

    /**
     * 导出APP 与队列关系列表
     */
    @PreAuthorize("@ss.hasPermi('system:research:export')")
    @Log(title = "APP 与队列关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppResearch appResearch)
    {
        List<AppResearch> list = appResearchService.selectAppResearchList(appResearch);
        ExcelUtil<AppResearch> util = new ExcelUtil<AppResearch>(AppResearch.class);
        util.exportExcel(response, list, "APP 与队列关系数据");
    }

    /**
     * 获取APP 与队列关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:research:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(appResearchService.selectAppResearchById(id));
    }

    /**
     * 新增APP 与队列关系
     */
    @PreAuthorize("@ss.hasPermi('system:research:add')")
    @Log(title = "APP与队列关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppResearch appResearch)
    {
        return toAjax(appResearchService.insertAppResearch(appResearch));
    }

    /**
     * 修改APP 与队列关系
     */
    @PreAuthorize("@ss.hasPermi('system:research:edit')")
    @Log(title = "APP与队列关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppResearch appResearch)
    {
        return toAjax(appResearchService.updateAppResearch(appResearch));
    }

    /**
     * 删除APP 与队列关系
     */
    @PreAuthorize("@ss.hasPermi('system:research:remove')")
    @Log(title = "APP 与队列关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(appResearchService.deleteAppResearchByIds(ids));
    }
}

