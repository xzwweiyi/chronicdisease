package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsCollection;
import com.dchealth.cms.service.ICmsCollectionService;
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
 * 用户收藏Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/collection")
public class CmsCollectionController extends BaseController
{
    @Autowired
    private ICmsCollectionService cmsCollectionService;

    /**
     * 查询用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:collection:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsCollection cmsCollection)
    {
        startPage();
        List<CmsCollection> list = cmsCollectionService.selectCmsCollectionList(cmsCollection);
        return getDataTable(list);
    }

    /**
     * 导出用户收藏列表
     */
    @PreAuthorize("@ss.hasPermi('system:collection:export')")
    @Log(title = "用户收藏", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsCollection cmsCollection)
    {
        List<CmsCollection> list = cmsCollectionService.selectCmsCollectionList(cmsCollection);
        ExcelUtil<CmsCollection> util = new ExcelUtil<CmsCollection>(CmsCollection.class);
        util.exportExcel(response, list, "用户收藏数据");
    }

    /**
     * 获取用户收藏详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:collection:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsCollectionService.selectCmsCollectionById(id));
    }

    /**
     * 新增用户收藏
     */
    @PreAuthorize("@ss.hasPermi('system:collection:add')")
    @Log(title = "用户收藏", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsCollection cmsCollection)
    {
        return toAjax(cmsCollectionService.insertCmsCollection(cmsCollection));
    }

    /**
     * 修改用户收藏
     */
    @PreAuthorize("@ss.hasPermi('system:collection:edit')")
    @Log(title = "用户收藏", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsCollection cmsCollection)
    {
        return toAjax(cmsCollectionService.updateCmsCollection(cmsCollection));
    }

    /**
     * 删除用户收藏
     */
    @PreAuthorize("@ss.hasPermi('system:collection:remove')")
    @Log(title = "用户收藏", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsCollectionService.deleteCmsCollectionByIds(ids));
    }
}
