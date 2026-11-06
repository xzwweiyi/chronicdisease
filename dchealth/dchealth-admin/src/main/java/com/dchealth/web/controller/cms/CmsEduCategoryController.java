package com.dchealth.web.controller.cms;

import com.dchealth.cms.domain.CmsEduCategory;
import com.dchealth.cms.service.ICmsEduCategoryService;
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

import java.util.List;

/**
 * 宣教分类Controller
 *
 * @author dchealth
 * @date 2026-09-02
 */
@RestController
@RequestMapping("/cms/eduCategory")
public class CmsEduCategoryController extends BaseController {

    @Autowired
    private ICmsEduCategoryService cmsEduCategoryService;

    /**
     * 查询宣教分类列表
     */
    @PreAuthorize("@ss.hasPermi('cms:eduCategory:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsEduCategory cmsEduCategory) {
        startPage();
        List<CmsEduCategory> list = cmsEduCategoryService.selectCmsEduCategoryList(cmsEduCategory);
        return getDataTable(list);
    }

    /**
     * 查询全部宣教分类（含文章数量，供宣教知识库页面分类导航使用，不分页）
     */
    @GetMapping("/all")
    public AjaxResult all(CmsEduCategory cmsEduCategory) {
        return AjaxResult.success(cmsEduCategoryService.selectCmsEduCategoryList(cmsEduCategory));
    }

    /**
     * 获取宣教分类详细信息
     */
    @PreAuthorize("@ss.hasPermi('cms:eduCategory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(cmsEduCategoryService.selectCmsEduCategoryById(id));
    }

    /**
     * 新增宣教分类
     */
    @PreAuthorize("@ss.hasPermi('cms:eduCategory:add')")
    @Log(title = "宣教分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsEduCategory cmsEduCategory) {
        cmsEduCategory.setCreateBy(getUsername());
        return toAjax(cmsEduCategoryService.insertCmsEduCategory(cmsEduCategory));
    }

    /**
     * 修改宣教分类
     */
    @PreAuthorize("@ss.hasPermi('cms:eduCategory:edit')")
    @Log(title = "宣教分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsEduCategory cmsEduCategory) {
        cmsEduCategory.setUpdateBy(getUsername());
        return toAjax(cmsEduCategoryService.updateCmsEduCategory(cmsEduCategory));
    }

    /**
     * 删除宣教分类
     */
    @PreAuthorize("@ss.hasPermi('cms:eduCategory:remove')")
    @Log(title = "宣教分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(cmsEduCategoryService.deleteCmsEduCategoryByIds(ids));
    }
}
