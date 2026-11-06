package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsCommentTip;
import com.dchealth.cms.service.ICmsCommentTipService;
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
 * 举报Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/tip")
public class CmsCommentTipController extends BaseController
{
    @Autowired
    private ICmsCommentTipService cmsCommentTipService;

    /**
     * 查询举报列表
     */
    @PreAuthorize("@ss.hasPermi('system:tip:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsCommentTip cmsCommentTip)
    {
        startPage();
        List<CmsCommentTip> list = cmsCommentTipService.selectCmsCommentTipList(cmsCommentTip);
        return getDataTable(list);
    }

    /**
     * 导出举报列表
     */
    @PreAuthorize("@ss.hasPermi('system:tip:export')")
    @Log(title = "举报", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsCommentTip cmsCommentTip)
    {
        List<CmsCommentTip> list = cmsCommentTipService.selectCmsCommentTipList(cmsCommentTip);
        ExcelUtil<CmsCommentTip> util = new ExcelUtil<CmsCommentTip>(CmsCommentTip.class);
        util.exportExcel(response, list, "举报数据");
    }

    /**
     * 获取举报详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:tip:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsCommentTipService.selectCmsCommentTipById(id));
    }

    /**
     * 新增举报
     */
    @PreAuthorize("@ss.hasPermi('system:tip:add')")
    @Log(title = "举报", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsCommentTip cmsCommentTip)
    {
        return toAjax(cmsCommentTipService.insertCmsCommentTip(cmsCommentTip));
    }

    /**
     * 修改举报
     */
    @PreAuthorize("@ss.hasPermi('system:tip:edit')")
    @Log(title = "举报", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsCommentTip cmsCommentTip)
    {
        return toAjax(cmsCommentTipService.updateCmsCommentTip(cmsCommentTip));
    }

    /**
     * 删除举报
     */
    @PreAuthorize("@ss.hasPermi('system:tip:remove')")
    @Log(title = "举报", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsCommentTipService.deleteCmsCommentTipByIds(ids));
    }
}
