package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsGuestImages;
import com.dchealth.cms.service.ICmsGuestImagesService;
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
 * 资讯图片Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/images")
public class CmsGuestImagesController extends BaseController
{
    @Autowired
    private ICmsGuestImagesService cmsGuestImagesService;

    /**
     * 查询资讯图片列表
     */
    @PreAuthorize("@ss.hasPermi('system:images:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsGuestImages cmsGuestImages)
    {
        startPage();
        List<CmsGuestImages> list = cmsGuestImagesService.selectCmsGuestImagesList(cmsGuestImages);
        return getDataTable(list);
    }

    /**
     * 导出资讯图片列表
     */
    @PreAuthorize("@ss.hasPermi('system:images:export')")
    @Log(title = "资讯图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsGuestImages cmsGuestImages)
    {
        List<CmsGuestImages> list = cmsGuestImagesService.selectCmsGuestImagesList(cmsGuestImages);
        ExcelUtil<CmsGuestImages> util = new ExcelUtil<CmsGuestImages>(CmsGuestImages.class);
        util.exportExcel(response, list, "资讯图片数据");
    }

    /**
     * 获取资讯图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:images:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsGuestImagesService.selectCmsGuestImagesById(id));
    }

    /**
     * 新增资讯图片
     */
    @PreAuthorize("@ss.hasPermi('system:images:add')")
    @Log(title = "资讯图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsGuestImages cmsGuestImages)
    {
        return toAjax(cmsGuestImagesService.insertCmsGuestImages(cmsGuestImages));
    }

    /**
     * 修改资讯图片
     */
    @PreAuthorize("@ss.hasPermi('system:images:edit')")
    @Log(title = "资讯图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsGuestImages cmsGuestImages)
    {
        return toAjax(cmsGuestImagesService.updateCmsGuestImages(cmsGuestImages));
    }

    /**
     * 删除资讯图片
     */
    @PreAuthorize("@ss.hasPermi('system:images:remove')")
    @Log(title = "资讯图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsGuestImagesService.deleteCmsGuestImagesByIds(ids));
    }
}
