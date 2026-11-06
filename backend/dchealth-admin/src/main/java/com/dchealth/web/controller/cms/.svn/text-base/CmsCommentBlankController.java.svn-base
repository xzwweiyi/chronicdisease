package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsCommentBlank;
import com.dchealth.cms.service.ICmsCommentBlankService;
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
 * 评价黑名单Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/blank")
public class CmsCommentBlankController extends BaseController
{
    @Autowired
    private ICmsCommentBlankService cmsCommentBlankService;

    /**
     * 查询评价黑名单列表
     */
    @PreAuthorize("@ss.hasPermi('system:blank:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsCommentBlank cmsCommentBlank)
    {
        startPage();
        List<CmsCommentBlank> list = cmsCommentBlankService.selectCmsCommentBlankList(cmsCommentBlank);
        return getDataTable(list);
    }

    /**
     * 导出评价黑名单列表
     */
    @PreAuthorize("@ss.hasPermi('system:blank:export')")
    @Log(title = "评价黑名单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsCommentBlank cmsCommentBlank)
    {
        List<CmsCommentBlank> list = cmsCommentBlankService.selectCmsCommentBlankList(cmsCommentBlank);
        ExcelUtil<CmsCommentBlank> util = new ExcelUtil<CmsCommentBlank>(CmsCommentBlank.class);
        util.exportExcel(response, list, "评价黑名单数据");
    }

    /**
     * 获取评价黑名单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:blank:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsCommentBlankService.selectCmsCommentBlankById(id));
    }

    /**
     * 新增评价黑名单
     */
    @PreAuthorize("@ss.hasPermi('system:blank:add')")
    @Log(title = "评价黑名单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsCommentBlank cmsCommentBlank)
    {
        return toAjax(cmsCommentBlankService.insertCmsCommentBlank(cmsCommentBlank));
    }

    /**
     * 修改评价黑名单
     */
    @PreAuthorize("@ss.hasPermi('system:blank:edit')")
    @Log(title = "评价黑名单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsCommentBlank cmsCommentBlank)
    {
        return toAjax(cmsCommentBlankService.updateCmsCommentBlank(cmsCommentBlank));
    }

    /**
     * 删除评价黑名单
     */
    @PreAuthorize("@ss.hasPermi('system:blank:remove')")
    @Log(title = "评价黑名单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsCommentBlankService.deleteCmsCommentBlankByIds(ids));
    }
}
