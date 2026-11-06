package com.dchealth.web.controller.contract;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.contract.domain.ContractServiceItem;
import com.dchealth.contract.service.IContractServiceItemService;

/**
 * 服务项目
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/contract/serviceItem")
public class ContractServiceItemController extends BaseController {

    @Autowired
    private IContractServiceItemService service;

    @PreAuthorize("@ss.hasPermi('contract:service:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractServiceItem entity) {
        startPage();
        List<ContractServiceItem> list = service.selectContractServiceItemList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('contract:service:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectContractServiceItemById(id));
    }

    @PreAuthorize("@ss.hasPermi('contract:service:add')")
    @Log(title = "服务项目", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractServiceItem entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertContractServiceItem(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:service:edit')")
    @Log(title = "服务项目", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractServiceItem entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateContractServiceItem(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:service:remove')")
    @Log(title = "服务项目", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteContractServiceItemByIds(ids));
    }
}
