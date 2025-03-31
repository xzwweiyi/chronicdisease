package com.dchealth.web.controller.research;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.dchealth.research.domain.ResearchInfoArticle;
import com.dchealth.research.service.IResearchInfoArticleService;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 队列-患教关系Controller
 * 
 * @author lchbu
 * @date 2022-08-25
 */
@RestController
@RequestMapping("/research/article")
public class ResearchInfoArticleController extends BaseController
{
    @Autowired
    private IResearchInfoArticleService researchInfoArticleService;

    /**
     * 查询队列-患教关系列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ResearchInfoArticle researchInfoArticle)
    {
        startPage();
        List<ResearchInfoArticle> list = researchInfoArticleService.selectResearchInfoArticleList(researchInfoArticle);
        return getDataTable(list);
    }

    /**
     * 导出队列-患教关系列表
     */
    @Log(title = "队列-患教关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ResearchInfoArticle researchInfoArticle)
    {
        List<ResearchInfoArticle> list = researchInfoArticleService.selectResearchInfoArticleList(researchInfoArticle);
        ExcelUtil<ResearchInfoArticle> util = new ExcelUtil<ResearchInfoArticle>(ResearchInfoArticle.class);
        util.exportExcel(response, list, "队列-患教关系数据");
    }

    /**
     * 获取队列-患教关系
     */
    @GetMapping(value = "/{researchId}")
    public AjaxResult getInfo(@PathVariable("researchId") Long researchId)
    {
        return AjaxResult.success(researchInfoArticleService.selectResearchInfoArticleByResearchId(researchId));
    }

    /**
     * 新增队列-患教关系
     */
    @Log(title = "队列-患教关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody List<ResearchInfoArticle> researchInfoArticles)
    {
        if(researchInfoArticles != null){
            List<Long> list = researchInfoArticles.stream().map(art -> art.getArticleId()).collect(Collectors.toList());
            if(list == null || list.size() == 0){
                return AjaxResult.error("未分配患教内容，请添加内容后在保存");
            }
        } else {
            return AjaxResult.error("未分配患教内容，请添加内容后在保存");
        }
        return toAjax(researchInfoArticleService.insertResearchInfoArticle(researchInfoArticles));
    }

    /**
     * 修改队列-患教关系
     */
    @Log(title = "队列-患教关系", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody ResearchInfoArticle researchInfoArticle)
    {
        return toAjax(researchInfoArticleService.updateResearchInfoArticle(researchInfoArticle));
    }

    /**
     * 删除队列-患教关系
     */
    @Log(title = "队列-患教关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{researchIds}")
    public AjaxResult remove(@PathVariable Long[] researchIds)
    {
        return toAjax(researchInfoArticleService.deleteResearchInfoArticleByResearchIds(researchIds));
    }

    /**
     * 队列-分类下患教列表展示
     */
    @GetMapping("/listArticle")
    public AjaxResult listArticle(ResearchInfoArticle researchInfoArticle)
    {
        return AjaxResult.success(researchInfoArticleService.selectListArticle(researchInfoArticle));
    }


    /**
     * 首页位设置
     */
    @PreAuthorize("@ss.hasPermi('research:article:indexPosition')")
    @Log(title = "首页位设置", businessType = BusinessType.UPDATE)
    @PutMapping("/indexPosition")
    public AjaxResult indexPosition(@RequestBody ResearchInfoArticle researchInfoArticle)
    {
        return toAjax(researchInfoArticleService.updateResearchArticleType(researchInfoArticle));
    }


    /**
     * 移除患教关系
     */
    @Log(title = "队列-患教关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/removeArticle")
    public AjaxResult removeArticle(ResearchInfoArticle researchInfoArticle)
    {
        if(researchInfoArticle.getResearchId() == null){
            return AjaxResult.error("队列id不能为空");
        }
        if(researchInfoArticle.getCategoryId() == null){
            return AjaxResult.error("分类id不能为空");
        }
        if(researchInfoArticle.getArticleId() == null){
            return AjaxResult.error("患教id不能为空");
        }
        return toAjax(researchInfoArticleService.removeArticle(researchInfoArticle));
    }
}
