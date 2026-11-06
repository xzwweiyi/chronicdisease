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
import com.dchealth.contract.domain.ContractPackage;
import com.dchealth.contract.service.IContractPackageService;

/**
 * 服务包
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/contract/package")
public class ContractPackageController extends BaseController {

    @Autowired
    private IContractPackageService service;

    @PreAuthorize("@ss.hasPermi('contract:package:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractPackage entity) {
        startPage();
        List<ContractPackage> list = service.selectContractPackageList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('contract:package:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectContractPackageById(id));
    }

    @PreAuthorize("@ss.hasPermi('contract:package:add')")
    @Log(title = "服务包", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractPackage entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertContractPackage(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:package:edit')")
    @Log(title = "服务包", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractPackage entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateContractPackage(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:package:remove')")
    @Log(title = "服务包", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteContractPackageByIds(ids));
    }
}
