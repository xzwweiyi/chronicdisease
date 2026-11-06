package com.dchealth.web.controller.pathway;

import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.pathway.service.IPathwayNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理路径节点Controller
 *
 * @author xuzw
 * @date 2026-06-30
 */
@RestController
@RequestMapping("/pathway/node")
public class PathwayNodeController extends BaseController
{
    @Autowired
    private IPathwayNodeService pathwayNodeService;

    /**
     * 查询路径的所有节点
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:list')")
    @GetMapping("/list/{researchId}")
    public AjaxResult list(@PathVariable Long researchId)
    {
        List<PatientRecordTemplate> nodes = pathwayNodeService.selectNodesByPathwayId(researchId);
        return AjaxResult.success(nodes);
    }

    /**
     * 新增节点
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:add')")
    @Log(title = "路径节点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PatientRecordTemplate node)
    {
        return toAjax(pathwayNodeService.insertNode(node));
    }

    /**
     * 修改节点
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:edit')")
    @Log(title = "路径节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PatientRecordTemplate node)
    {
        return toAjax(pathwayNodeService.updateNode(node));
    }

    /**
     * 删除节点
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:remove')")
    @Log(title = "路径节点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{followId}")
    public AjaxResult remove(@PathVariable Long followId)
    {
        return toAjax(pathwayNodeService.deleteNode(followId));
    }

    /**
     * 节点排序
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:edit')")
    @Log(title = "路径节点排序", businessType = BusinessType.UPDATE)
    @PutMapping("/reorder/{researchId}")
    public AjaxResult reorder(@PathVariable Long researchId, @RequestBody List<Long> orderedFollowIds)
    {
        return toAjax(pathwayNodeService.reorderNodes(researchId, orderedFollowIds));
    }

    /**
     * 保存节点CRF表单绑定
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:edit')")
    @Log(title = "路径节点表单", businessType = BusinessType.UPDATE)
    @PutMapping("/forms/{followId}")
    public AjaxResult saveForms(@PathVariable Long followId, @RequestBody Map<String, String> params)
    {
        String requiredForms = params.get("requiredForms");
        String optionalForms = params.get("optionalForms");
        return toAjax(pathwayNodeService.saveNodeForms(followId, requiredForms, optionalForms));
    }

    /**
     * 获取基线节点
     */
    @PreAuthorize("@ss.hasPermi('pathway:node:list')")
    @GetMapping("/baseline/{researchId}")
    public AjaxResult getBaseline(@PathVariable Long researchId)
    {
        PatientRecordTemplate baseline = pathwayNodeService.selectBaselineNode(researchId);
        return AjaxResult.success(baseline);
    }
}
