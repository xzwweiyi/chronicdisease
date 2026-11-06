package com.dchealth.web.controller.cms;

import com.dchealth.cms.domain.CmsArticleSetEvaluation;
import com.dchealth.cms.service.ICmsArticleSetEvaluationService;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 患教设置评测结果Controller
 * 
 * @author lchbu
 * @date 2023-04-07
 */
@RestController
@RequestMapping("/cms/dept/cms_article_set_evaluation")
public class CmsArticleSetEvaluationDeptController extends BaseController
{
    @Autowired
    private ICmsArticleSetEvaluationService cmsArticleSetEvaluationService;

    /**
     * 查询患教设置评测结果列表
     */
    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsArticleSetEvaluation cmsArticleSetEvaluation)
    {
        startPage();
        List<CmsArticleSetEvaluation> list = cmsArticleSetEvaluationService.selectCmsArticleSetEvaluationList(cmsArticleSetEvaluation);
        return getDataTable(list);
    }

    /**
     * 导出患教设置评测结果列表
     */
    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:export')")
//    @Log(title = "患教设置评测结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsArticleSetEvaluation cmsArticleSetEvaluation)
    {
        List<CmsArticleSetEvaluation> list = cmsArticleSetEvaluationService.selectCmsArticleSetEvaluationList(cmsArticleSetEvaluation);
        ExcelUtil<CmsArticleSetEvaluation> util = new ExcelUtil<CmsArticleSetEvaluation>(CmsArticleSetEvaluation.class);
        util.exportExcel(response, list, "患教设置评测结果数据");
    }

    /**
     * 获取患教设置评测结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsArticleSetEvaluationService.selectCmsArticleSetEvaluationById(id));
    }

    /**
     * 新增患教设置评测结果
     */
    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:add')")
//    @Log(title = "患教设置评测结果", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CmsArticleSetEvaluation cmsArticleSetEvaluation){
        cmsArticleSetEvaluation.setCreateBy(getUsername());
        return AjaxResult.error("科室不允许新增患教设置评测结果");
    }

    /**
     * 修改患教设置评测结果
     */
    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:edit')")
//    @Log(title = "患教设置评测结果", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody CmsArticleSetEvaluation cmsArticleSetEvaluation){
        return AjaxResult.error("科室不允许修改患教设置评测结果");
    }

    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:delete')")
//    @Log(title = "患教设置评测结果", businessType = BusinessType.DELETE)
    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return  AjaxResult.error("科室不允许删除患教设置评测结果");
    }


    /**
     * 删除患教设置评测结果
     */
    @PreAuthorize("@ss.hasPermi('cmsDept:cms_article_set_evaluation:remove')")
//    @Log(title = "患教设置评测结果", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return AjaxResult.error("科室不允许删除患教设置评测结果");
    }
}
