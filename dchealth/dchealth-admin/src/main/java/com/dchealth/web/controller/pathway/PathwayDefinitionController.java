package com.dchealth.web.controller.pathway;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.pathway.service.IPathwayService;
import com.dchealth.research.domain.ResearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理路径定义Controller
 *
 * @author xuzw
 * @date 2026-06-30
 */
@RestController
@RequestMapping("/pathway/definition")
public class PathwayDefinitionController extends BaseController
{
    @Autowired
    private IPathwayService pathwayService;

    /**
     * 查询管理路径列表
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchInfo pathway)
    {
        startPage();
        List<ResearchInfo> list = pathwayService.selectPathwayList(pathway);
        return getDataTable(list);
    }

    /**
     * 获取管理路径详细信息
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pathwayService.selectPathwayById(id));
    }

    /**
     * 新增管理路径
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:add')")
    @Log(title = "管理路径", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchInfo pathway)
    {
        Long id = pathwayService.insertPathway(pathway);
        return AjaxResult.success(id);
    }

    /**
     * 修改管理路径
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:edit')")
    @Log(title = "管理路径", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchInfo pathway)
    {
        return toAjax(pathwayService.updatePathway(pathway));
    }

    /**
     * 删除管理路径
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:remove')")
    @Log(title = "管理路径", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id)
    {
        return toAjax(pathwayService.deletePathwayById(id));
    }

    /**
     * 更新路径状态（发布/关闭）
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:edit')")
    @Log(title = "管理路径状态", businessType = BusinessType.UPDATE)
    @PutMapping("/status/{id}")
    public AjaxResult updateStatus(@PathVariable Long id, @RequestParam String pathwayStatus)
    {
        return toAjax(pathwayService.updatePathwayStatus(id, pathwayStatus));
    }

    /**
     * 复制路径
     */
    @PreAuthorize("@ss.hasPermi('pathway:definition:add')")
    @Log(title = "管理路径复制", businessType = BusinessType.INSERT)
    @PostMapping("/copy/{sourceId}")
    public AjaxResult copy(@PathVariable Long sourceId)
    {
        Long newId = pathwayService.copyPathway(sourceId);
        return AjaxResult.success(newId);
    }
}
