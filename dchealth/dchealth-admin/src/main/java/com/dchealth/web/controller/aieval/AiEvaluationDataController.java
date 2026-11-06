package com.dchealth.web.controller.aieval;

import java.util.List;

import com.dchealth.aieval.domain.AiEvaluationData;
import com.dchealth.aieval.service.IAiEvaluationDataService;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI评估数据
 *
 * @author dchealth
 * @date 2026-09-02
 */
@RestController
@RequestMapping("/aieval/data")
public class AiEvaluationDataController extends BaseController {

    @Autowired
    private IAiEvaluationDataService aiEvaluationDataService;

    /**
     * 查询慢病路径管理下已发布的队列列表（AI评估分析主体）
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:list')")
    @GetMapping("/queue/list")
    public TableDataInfo queueList(com.dchealth.aieval.domain.vo.AiEvaluationQueueVo vo) {
        startPage();
        List<com.dchealth.aieval.domain.vo.AiEvaluationQueueVo> list = aiEvaluationDataService.selectPublishedQueueList(vo);
        return getDataTable(list);
    }

    /**
     * 查询AI评估数据列表
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:query')")
    @GetMapping("/list")
    public TableDataInfo list(AiEvaluationData entity) {
        startPage();
        List<AiEvaluationData> list = aiEvaluationDataService.selectAiEvaluationDataList(entity);
        return getDataTable(list);
    }

    /**
     * 获取AI评估数据详情
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(aiEvaluationDataService.selectAiEvaluationDataById(id));
    }

    /**
     * 新增AI评估数据
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:add')")
    @Log(title = "AI评估数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AiEvaluationData entity) {
        entity.setCreateBy(getUsername());
        return toAjax(aiEvaluationDataService.insertAiEvaluationData(entity));
    }

    /**
     * 修改AI评估数据
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:edit')")
    @Log(title = "AI评估数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AiEvaluationData entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(aiEvaluationDataService.updateAiEvaluationData(entity));
    }

    /**
     * 删除AI评估数据
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:remove')")
    @Log(title = "AI评估数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(aiEvaluationDataService.deleteAiEvaluationDataByIds(ids));
    }

    /**
     * AI分析：调用 DeepSeek 大模型输出分析结果与下一步措施
     */
    @PreAuthorize("@ss.hasPermi('ai:evaluation:project:query')")
    @Log(title = "AI评估数据分析", businessType = BusinessType.OTHER)
    @PostMapping("/analyze/{id}")
    public AjaxResult analyze(@PathVariable Long id) {
        return AjaxResult.success(aiEvaluationDataService.analyze(id));
    }
}
