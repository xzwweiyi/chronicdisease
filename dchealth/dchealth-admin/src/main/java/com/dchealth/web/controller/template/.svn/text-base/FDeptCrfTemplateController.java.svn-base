package com.dchealth.web.controller.template;


import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.template.domain.FDeptCrfTemplate;
import com.dchealth.template.service.IFDeptCrfTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/template/fDeptTemplate")
public class FDeptCrfTemplateController {

    @Autowired
    private IFDeptCrfTemplateService ifDeptCrfTemplateService;

    /**
     * 加入科室库
     */
    @PreAuthorize("@ss.hasPermi('template:fDeptTemplate:add')")
    @Log(title = "CRF模板加入科室库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FDeptCrfTemplate fDeptCrfTemplate){
        return AjaxResult.success(ifDeptCrfTemplateService.insertDeptCrfTemplate(fDeptCrfTemplate));
    }
}
