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
import com.dchealth.aieval.domain.AiEvaluationRule;
import com.dchealth.aieval.service.IAiEvaluationRuleService;

/**
 * AI评估规则
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/aieval/rule")
public class AiEvaluationRuleController extends BaseController {

    @Autowired
    private IAiEvaluationRuleService service;

    @PreAuthorize("@ss.hasPermi('ai:evaluation:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(AiEvaluationRule entity) {
        startPage();
        List<AiEvaluationRule> list = service.selectAiEvaluationRuleList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:rule:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectAiEvaluationRuleById(id));
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:rule:add')")
    @Log(title = "AI评估规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiEvaluationRule entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertAiEvaluationRule(entity));
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:rule:edit')")
    @Log(title = "AI评估规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiEvaluationRule entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateAiEvaluationRule(entity));
    }

    @PreAuthorize("@ss.hasPermi('ai:evaluation:rule:remove')")
    @Log(title = "AI评估规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteAiEvaluationRuleByIds(ids));
    }
}
