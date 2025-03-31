package com.dchealth.web.controller.cms;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.cms.domain.CmsStat;
import com.dchealth.cms.service.ICmsStatService;
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
 * 资讯统计Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/stat")
public class CmsStatController extends BaseController
{
    @Autowired
    private ICmsStatService cmsStatService;

    /**
     * 查询资讯统计列表
     */
    @PreAuthorize("@ss.hasPermi('system:stat:list')")
    @GetMapping("/list")
    public TableDataInfo list(CmsStat cmsStat)
    {
        startPage();
        List<CmsStat> list = cmsStatService.selectCmsStatList(cmsStat);
        return getDataTable(list);
    }

    /**
     * 导出资讯统计列表
     */
    @PreAuthorize("@ss.hasPermi('system:stat:export')")
    @Log(title = "资讯统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CmsStat cmsStat)
    {
        List<CmsStat> list = cmsStatService.selectCmsStatList(cmsStat);
        ExcelUtil<CmsStat> util = new ExcelUtil<CmsStat>(CmsStat.class);
        util.exportExcel(response, list, "资讯统计数据");
    }

    /**
     * 获取资讯统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stat:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cmsStatService.selectCmsStatById(id));
    }

    /**
     * 新增资讯统计
     */
    @PreAuthorize("@ss.hasPermi('system:stat:add')")
    @Log(title = "资讯统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CmsStat cmsStat)
    {
        return toAjax(cmsStatService.insertCmsStat(cmsStat));
    }

    /**
     * 修改资讯统计
     */
    @PreAuthorize("@ss.hasPermi('system:stat:edit')")
    @Log(title = "资讯统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CmsStat cmsStat)
    {
        return toAjax(cmsStatService.updateCmsStat(cmsStat));
    }

    /**
     * 删除资讯统计
     */
    @PreAuthorize("@ss.hasPermi('system:stat:remove')")
    @Log(title = "资讯统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cmsStatService.deleteCmsStatByIds(ids));
    }
}
