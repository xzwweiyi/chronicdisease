package com.dchealth.web.controller.referral;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.referral.domain.ReferralRule;
import com.dchealth.referral.service.IReferralRuleService;

/**
 * 转诊规则
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/referral/rule")
public class ReferralRuleController extends BaseController {

    @Autowired
    private IReferralRuleService service;

    @PreAuthorize("@ss.hasPermi('referral:rule:list')")
    @GetMapping("/list")
    public TableDataInfo list(ReferralRule entity) {
        startPage();
        List<ReferralRule> list = service.selectReferralRuleList(entity);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('referral:rule:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectReferralRuleById(id));
    }

    @PreAuthorize("@ss.hasPermi('referral:rule:add')")
    @Log(title = "转诊规则", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReferralRule entity) {
        entity.setCreateBy(getUsername());
        return toAjax(service.insertReferralRule(entity));
    }

    @PreAuthorize("@ss.hasPermi('referral:rule:edit')")
    @Log(title = "转诊规则", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReferralRule entity) {
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateReferralRule(entity));
    }

    @PreAuthorize("@ss.hasPermi('referral:rule:remove')")
    @Log(title = "转诊规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteReferralRuleByIds(ids));
    }
}
