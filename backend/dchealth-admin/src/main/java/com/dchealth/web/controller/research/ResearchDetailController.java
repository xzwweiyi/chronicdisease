package com.dchealth.web.controller.research;

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
import com.dchealth.research.domain.ResearchDetail;
import com.dchealth.research.service.IResearchDetailService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 队列详情Controller
 * 
 * @author lchbu
 * @date 2022-08-22
 */
@RestController
@RequestMapping("/research/detail")
public class ResearchDetailController extends BaseController
{
    @Autowired
    private IResearchDetailService researchDetailService;

    /**
     * 查询队列详情列表
     */
    @PreAuthorize("@ss.hasPermi('research:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(ResearchDetail researchDetail)
    {
        startPage();
        List<ResearchDetail> list = researchDetailService.selectResearchDetailList(researchDetail);
        return getDataTable(list);
    }

    /**
     * 导出队列详情列表
     */
    @PreAuthorize("@ss.hasPermi('research:detail:export')")
    @Log(title = "队列详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchDetail researchDetail)
    {
        List<ResearchDetail> list = researchDetailService.selectResearchDetailList(researchDetail);
        ExcelUtil<ResearchDetail> util = new ExcelUtil<ResearchDetail>(ResearchDetail.class);
        util.exportExcel(response, list, "队列详情数据");
    }

    /**
     * 获取队列详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('research:detail:query')")
    @GetMapping(value = "/{researchId}")
    public AjaxResult getInfo(@PathVariable("researchId") Long researchId)
    {
        return AjaxResult.success(researchDetailService.selectResearchDetailByResearchId(researchId));
    }

    /**
     * 新增队列详情
     */
    @PreAuthorize("@ss.hasPermi('research:detail:add')")
    @Log(title = "队列详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResearchDetail researchDetail)
    {
        return toAjax(researchDetailService.insertResearchDetail(researchDetail));
    }

    /**
     * 修改队列详情
     */
    @PreAuthorize("@ss.hasPermi('research:detail:edit')")
    @Log(title = "队列详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResearchDetail researchDetail)
    {
        return toAjax(researchDetailService.updateResearchDetail(researchDetail));
    }

    /**
     * 删除队列详情
     */
    @PreAuthorize("@ss.hasPermi('research:detail:remove')")
    @Log(title = "队列详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{researchIds}")
    public AjaxResult remove(@PathVariable Long[] researchIds)
    {
        return toAjax(researchDetailService.deleteResearchDetailByResearchIds(researchIds));
    }
}
