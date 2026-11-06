package com.dchealth.web.controller.base;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.base.domain.AppBanner;
import com.dchealth.base.service.IAppBannerService;
import com.dchealth.common.core.text.Convert;
import com.dchealth.common.utils.poi.ExcelUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 轮播管理Controller
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@RestController
@RequestMapping("/cms/banner")
public class AppBannerController extends BaseController
{
    @Autowired
    private IAppBannerService appBannerService;

    /**
     * 查询轮播管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppBanner appBanner)
    {
        startPage();
//        appBanner.setDeptId(getLoginUser().getDeptId());
        List<AppBanner> list = appBannerService.selectAppBannerList(appBanner);
        return getDataTable(list);
    }

    /**
     * 导出轮播管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:banner:export')")
    @Log(title = "轮播管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppBanner appBanner)
    {
        List<AppBanner> list = appBannerService.selectAppBannerList(appBanner);
        ExcelUtil<AppBanner> util = new ExcelUtil<AppBanner>(AppBanner.class);
        util.exportExcel(response, list, "轮播管理数据");
    }

    /**
     * 获取轮播管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:banner:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(appBannerService.selectAppBannerById(id));
    }

    /**
     * 新增轮播管理
     */
    @PreAuthorize("@ss.hasPermi('system:banner:add')")
    @Log(title = "轮播管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AppBanner appBanner)
    {
        return toAjax(appBannerService.insertAppBanner(appBanner));
    }

    /**
     * 修改轮播管理
     */
    @PreAuthorize("@ss.hasPermi('system:banner:edit')")
    @Log(title = "轮播管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppBanner appBanner)
    {
        return toAjax(appBannerService.updateAppBanner(appBanner));
    }

    /**
     * 删除轮播管理
     */
    @PreAuthorize("@ss.hasPermi('system:banner:remove')")
    @Log(title = "轮播管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(appBannerService.deleteAppBannerByIds(ids));
    }

    /**
     * 保存首页APP推荐专家
     */
    @Log(title = "保存首页APP推荐资讯", businessType = BusinessType.INSERT)
    @PostMapping("/saveArticleAppBanner")
    @ResponseBody
    public AjaxResult importTableSave(String ids, String tenantId, String positionType) {
        String[] articleIds = Convert.toStrArray(ids);
        // 查询表信息
        appBannerService.saveArticleAppBanner(articleIds, tenantId, positionType);
        return AjaxResult.success();
    }
}
