package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsArticleData;
import com.dchealth.cms.service.ICmsArticleDataService;
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
 * 资讯详情Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/data")
public class CmsArticleDataController extends BaseController
{
    @Autowired
    private ICmsArticleDataService cmsArticleDataService;

    /**
     * 查询资讯详情列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsArticleData cmsArticleData)
    {
        startPage();
        List<CmsArticleData> list = cmsArticleDataService.selectCmsArticleDataList(cmsArticleData);
        return getDataTable(list);
    }

    /**
     * 导出资讯详情列表
     */
    @PreAuthorize("@ss.hasPermi('system:data:export')")
    @Log(title = "资讯详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsArticleData cmsArticleData)
    {
        List<CmsArticleData> list = cmsArticleDataService.selectCmsArticleDataList(cmsArticleData);
        ExcelUtil<CmsArticleData> util = new ExcelUtil<CmsArticleData>(CmsArticleData.class);
        util.exportExcel(response, list, "资讯详情数据");
    }

    /**
     * 获取资讯详情详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsArticleDataService.selectCmsArticleDataById(id));
    }

    /**
     * 新增资讯详情
     */
    @PreAuthorize("@ss.hasPermi('system:data:add')")
    @Log(title = "资讯详情", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsArticleData cmsArticleData)
    {
        return toAjax(cmsArticleDataService.insertCmsArticleData(cmsArticleData));
    }

    /**
     * 修改资讯详情
     */
    @PreAuthorize("@ss.hasPermi('system:data:edit')")
    @Log(title = "资讯详情", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsArticleData cmsArticleData)
    {
        return toAjax(cmsArticleDataService.updateCmsArticleData(cmsArticleData));
    }

    /**
     * 删除资讯详情
     */
    @PreAuthorize("@ss.hasPermi('system:data:remove')")
    @Log(title = "资讯详情", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsArticleDataService.deleteCmsArticleDataByIds(ids));
    }
}
