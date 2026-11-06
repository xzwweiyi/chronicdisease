package com.dchealth.web.controller.medical;

import com.alibaba.fastjson2.JSONObject;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.medical.domain.PatientEnrollmentCondition;
import com.dchealth.medical.domain.PatientEnrollmentConditionTree;
import com.dchealth.medical.service.IPatientEnrollmentConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medical/enrollmentCondition")
public class PatientEnrollmentConditionController extends BaseController {
    @Autowired
    private IPatientEnrollmentConditionService patientEnrollmentConditionService;
    /**
     * 新增纳排条件
     */
    @PreAuthorize("@ss.hasPermi('medical:enrollmentCondition:add')")
    @Log(title = "新增入组条件", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody PatientEnrollmentCondition patientEnrollmentCondition) {
        try {
            logger.info("PatientEnrollmentCondition add:"+ JSONObject.toJSONString(patientEnrollmentCondition));
            return toAjax(patientEnrollmentConditionService.insertPatientEnrollmentCondition(patientEnrollmentCondition));
        } catch (ServiceException exception) {
            return AjaxResult.error(exception.getMessage());
        }
    }

    /**
     * 纳排条件查询
     */
    @PreAuthorize("@ss.hasPermi('medical:enrollmentCondition:select')")
    @Log(title = "纳排条件查询", businessType = BusinessType.INSERT)
    @PostMapping("/search")
    public AjaxResult search(@RequestBody PatientEnrollmentCondition patientEnrollmentCondition) throws IOException {
        return patientEnrollmentConditionService.conditionSearch(patientEnrollmentCondition);
    }

    /**
     * 纳排条件查询
     */
    @PreAuthorize("@ss.hasPermi('medical:enrollmentCondition:select')")
    @GetMapping("/detail")
    public PatientEnrollmentCondition conditionDetail(Long researchId) {
        return patientEnrollmentConditionService.conditionDetail(researchId);
    }

    /**
     * 纳排条件树
     */
    @PreAuthorize("@ss.hasPermi('medical:enrollmentCondition:select')")
    @GetMapping("/searchTree")
    public List<PatientEnrollmentConditionTree> searchTree() {
        return patientEnrollmentConditionService.searchTree();
    }

}
