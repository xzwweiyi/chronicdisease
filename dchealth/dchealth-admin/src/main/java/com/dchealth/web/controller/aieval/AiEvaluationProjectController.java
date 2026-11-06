package com.dchealth.web.controller.aieval;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.aieval.domain.AiEvaluationProject;
import com.dchealth.aieval.service.IAiEvaluationProjectService;

/**
 * AI评估项目
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/aieval/project")
public class AiEvaluationProjectController extends BaseController {

    @Autowired
    private IAiEvaluationProjectService service;

    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiEvaluationProject entity) {
        startPage();
        List<AiEvaluationProject> list = service.selectAiEvaluationProjectList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectAiEvaluationProjectById(id));
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:add')")
    @Log(title = "AI评估项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiEvaluationProject entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertAiEvaluationProject(entity));
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:edit')")
    @Log(title = "AI评估项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiEvaluationProject entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateAiEvaluationProject(entity));
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:remove')")
    @Log(title = "AI评估项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteAiEvaluationProjectByIds(ids));
    }
}
