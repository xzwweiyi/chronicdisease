package com.dchealth.web.controller.patient;

import com.dchealth.cms.service.ICmsArticleService;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.medical.service.IPatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Author jiaorui
 * @Date 2023/9/19
 * @Description:
 */
@RestController
@RequestMapping("/patient/manage")
public class PatientDataManageController {

    @Autowired
    private IPatientRecordService patientRecordService;

    @Autowired
    private ICmsArticleService cmsArticleService;

    /**
     * 患者详细信息导出
     *
     * @param response
     * @param researchId 队列id，必填
     * @param patientIds 患者id，选择导入必填
     */
    @PreAuthorize("@ss.hasPermi('patient:manage:exportData')")
    @PostMapping("/exportData")
    public void exportData(HttpServletResponse response, Long researchId, Long[] patientIds) {
        List patientIdList = null;
        if (Objects.nonNull(patientIds)){
            patientIdList = Arrays.asList(patientIds);
        }
        patientRecordService.exportData(response, researchId, patientIdList);
    }


    /**
     * 患者详细信息导出
     *
     * @param response
     * @param researchId 队列id，必填
     * @param patientIds 患者id，选择导入必填
     */
    @PreAuthorize("@ss.hasPermi('edc:manage:exportData')")
    @PostMapping("/edcExportData")
    public void edcExportData(HttpServletResponse response, Long researchId, Long[] patientIds) {
        List patientIdList = null;
        if (Objects.nonNull(patientIds)){
            patientIdList = Arrays.asList(patientIds);
        }
        patientRecordService.edcExportData(response, researchId, patientIdList);
    }

    /**
     * 导出资讯管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:article:exportArticle')")
    @Log(title = "问卷导出", businessType = BusinessType.EXPORT)
    @PostMapping("/exportArticle")
    public void exportArticle(HttpServletResponse response,Long[] examRecordIds) {
        if (Objects.isNull(examRecordIds)) {
            cmsArticleService.edcExportData(response, new ArrayList<>());
        }else {
            cmsArticleService.edcExportData(response, Arrays.asList(examRecordIds));
        }
    }
}
