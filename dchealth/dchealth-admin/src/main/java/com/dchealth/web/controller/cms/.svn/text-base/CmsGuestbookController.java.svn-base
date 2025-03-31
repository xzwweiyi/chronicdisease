package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsGuestbook;
import com.dchealth.cms.service.ICmsGuestbookService;
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
 * 反馈管理Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/guestbook")
public class CmsGuestbookController extends BaseController
{
    @Autowired
    private ICmsGuestbookService cmsGuestbookService;

    /**
     * 查询反馈管理列表
     */
    @PreAuthorize("@ss.hasPermi('cms:guestbook:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsGuestbook cmsGuestbook)
    {
        startPage();
        List<CmsGuestbook> list = cmsGuestbookService.selectCmsGuestbookList(cmsGuestbook);
        return getDataTable(list);
    }

    /**
     * 导出反馈管理列表
     */
    @PreAuthorize("@ss.hasPermi('cms:guestbook:export')")
    @Log(title = "反馈管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsGuestbook cmsGuestbook)
    {
        List<CmsGuestbook> list = cmsGuestbookService.selectCmsGuestbookList(cmsGuestbook);
        ExcelUtil<CmsGuestbook> util = new ExcelUtil<CmsGuestbook>(CmsGuestbook.class);
        util.exportExcel(response, list, "反馈管理数据");
    }

    /**
     * 获取反馈管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('cms:guestbook:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsGuestbookService.selectCmsGuestbookById(id));
    }

    /**
     * 新增反馈管理
     */
    @PreAuthorize("@ss.hasPermi('cms:guestbook:add')")
    @Log(title = "反馈管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsGuestbook cmsGuestbook)
    {
        return toAjax(cmsGuestbookService.insertCmsGuestbook(cmsGuestbook));
    }

    /**
     * 修改反馈管理
     */
    @PreAuthorize("@ss.hasPermi('cms:guestbook:edit')")
    @Log(title = "反馈管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsGuestbook cmsGuestbook)
    {
        return toAjax(cmsGuestbookService.updateCmsGuestbook(cmsGuestbook));
    }

    /**
     * 删除反馈管理
     */
    @PreAuthorize("@ss.hasPermi('cms:guestbook:remove')")
    @Log(title = "反馈管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsGuestbookService.deleteCmsGuestbookByIds(ids));
    }
}
