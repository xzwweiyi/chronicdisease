package com.dchealth.web.controller.research;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.research.domain.SysPopNotice;
import com.dchealth.research.service.ISysPopNoticeService;
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
 * 消息弹框Controller
 * 
 * @author lchbu
 * @date 2024-01-23
 */
@RestController
@RequestMapping("/system/notice2")
public class SysPopNoticeController extends BaseController
{
    @Autowired
    private ISysPopNoticeService sysPopNoticeService;

    /**
     * 查询消息弹框列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPopNotice sysPopNotice)
    {
        startPage();
        List<SysPopNotice> list = sysPopNoticeService.selectSysPopNoticeList(sysPopNotice);
        return getDataTable(list);
    }

    /**
     * 导出消息弹框列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:export')")
    @Log(title = "消息弹框", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPopNotice sysPopNotice)
    {
        List<SysPopNotice> list = sysPopNoticeService.selectSysPopNoticeList(sysPopNotice);
        ExcelUtil<SysPopNotice> util = new ExcelUtil<SysPopNotice>(SysPopNotice.class);
        util.exportExcel(response, list, "消息弹框数据");
    }

    /**
     * 获取消息弹框详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(sysPopNoticeService.selectSysPopNoticeById(id));
    }

    /**
     * 新增消息弹框
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "消息弹框", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPopNotice sysPopNotice)
    {
        return toAjax(sysPopNoticeService.insertSysPopNotice(sysPopNotice));
    }

    /**
     * 修改消息弹框
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "消息弹框", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPopNotice sysPopNotice)
    {
        return toAjax(sysPopNoticeService.updateSysPopNotice(sysPopNotice));
    }

    /**
     * 删除消息弹框
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "消息弹框", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysPopNoticeService.deleteSysPopNoticeByIds(ids));
    }
}
