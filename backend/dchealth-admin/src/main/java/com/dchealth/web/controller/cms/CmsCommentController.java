package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsComment;
import com.dchealth.cms.service.ICmsCommentService;
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
 * 资讯评论Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/comment")
public class CmsCommentController extends BaseController
{
    @Autowired
    private ICmsCommentService cmsCommentService;

    /**
     * 查询资讯评论列表
     */
    @PreAuthorize("@ss.hasPermi('system:comment:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsComment cmsComment)
    {
        startPage();
        List<CmsComment> list = cmsCommentService.selectCmsCommentList(cmsComment);
        return getDataTable(list);
    }

    /**
     * 导出资讯评论列表
     */
    @PreAuthorize("@ss.hasPermi('system:comment:export')")
    @Log(title = "资讯评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsComment cmsComment)
    {
        List<CmsComment> list = cmsCommentService.selectCmsCommentList(cmsComment);
        ExcelUtil<CmsComment> util = new ExcelUtil<CmsComment>(CmsComment.class);
        util.exportExcel(response, list, "资讯评论数据");
    }

    /**
     * 获取资讯评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:comment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsCommentService.selectCmsCommentById(id));
    }

    /**
     * 新增资讯评论
     */
    @PreAuthorize("@ss.hasPermi('system:comment:add')")
    @Log(title = "资讯评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsComment cmsComment)
    {
        return toAjax(cmsCommentService.insertCmsComment(cmsComment));
    }

    /**
     * 修改资讯评论
     */
    @PreAuthorize("@ss.hasPermi('system:comment:edit')")
    @Log(title = "资讯评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsComment cmsComment)
    {
        return toAjax(cmsCommentService.updateCmsComment(cmsComment));
    }

    /**
     * 删除资讯评论
     */
    @PreAuthorize("@ss.hasPermi('system:comment:remove')")
    @Log(title = "资讯评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsCommentService.deleteCmsCommentByIds(ids));
    }
}
