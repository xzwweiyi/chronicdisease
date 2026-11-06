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
import com.dchealth.contract.domain.ContractTeam;
import com.dchealth.contract.service.IContractTeamService;

/**
 * 签约团队
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/contract/team")
public class ContractTeamController extends BaseController {

    @Autowired
    private IContractTeamService service;

    @PreAuthorize("@ss.hasPermi('contract:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(ContractTeam entity) {
        startPage();
        List<ContractTeam> list = service.selectContractTeamList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('contract:team:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectContractTeamById(id));
    }

    @PreAuthorize("@ss.hasPermi('contract:team:add')")
    @Log(title = "签约团队", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractTeam entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertContractTeam(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:team:edit')")
    @Log(title = "签约团队", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractTeam entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateContractTeam(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:team:remove')")
    @Log(title = "签约团队", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteContractTeamByIds(ids));
    }
}
