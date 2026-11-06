package com.dchealth.web.controller.account;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.account.domain.AccountNotice;
import com.dchealth.account.service.IAccountNoticeService;
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


@RestController
@RequestMapping("/account/notice")
public class AccountNoticeController extends BaseController{
    @Autowired
    private IAccountNoticeService accountNoticeService;

    /**
     * 查询消息通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccountNotice accountNotice){
        startPage();
        List<AccountNotice> list = accountNoticeService.selectAccountNoticeList(accountNotice);
        return getDataTable(list);
    }

    /**
     * 导出消息通知列表
     */
    @PreAuthorize("@ss.hasPermi('system:notice:export')")
    @Log(title = "消息通知", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccountNotice accountNotice){
        List<AccountNotice> list = accountNoticeService.selectAccountNoticeList(accountNotice);
        ExcelUtil<AccountNotice> util = new ExcelUtil<AccountNotice>(AccountNotice.class);
        util.exportExcel(response, list, "消息通知数据");
    }

    /**
     * 获取消息通知详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id){
        return AjaxResult.success(accountNoticeService.selectAccountNoticeById(id));
    }

    /**
     * 新增消息通知
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "消息通知", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccountNotice accountNotice){
        return toAjax(accountNoticeService.insertAccountNotice(accountNotice));
    }

    /**
     * 修改消息通知
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "消息通知", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccountNotice accountNotice){
        return toAjax(accountNoticeService.updateAccountNotice(accountNotice));
    }

    /**
     * 删除消息通知
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "消息通知", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(accountNoticeService.deleteAccountNoticeByIds(ids));
    }
}
