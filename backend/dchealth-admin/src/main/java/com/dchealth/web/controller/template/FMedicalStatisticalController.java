package com.dchealth.web.controller.template;


import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.vo.ExamStatisticVo;
import com.dchealth.template.domain.vo.PageVo;
import com.dchealth.template.service.IFMedicalStatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/template/f_medical_statistical")
public class FMedicalStatisticalController  {

    @Autowired
    private IFMedicalStatisticalService fMedicalStatisticalService;


    @PreAuthorize("@ss.hasPermi('template:medical:list')")
    @PostMapping("/staticalData")
    @ResponseBody
    public PageVo statistic(FMedicalExamSubject fMedicalExamSubject) {
        PageVo pageVo = new PageVo();
        int pageSize = fMedicalExamSubject.getPageSize();
        int pageNum = fMedicalExamSubject.getPageNum();
        fMedicalExamSubject.setPageSize(Integer.MAX_VALUE);
        List<ExamStatisticVo> list = null;
        try {
            list = fMedicalStatisticalService.queryStatisticalData(fMedicalExamSubject);
        } catch (Exception e) {
            pageVo.setErrorMessage(e.getMessage());
            return pageVo;
        }

        pageVo.setTotal(list.size());
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());
        List<ExamStatisticVo> handleList =list.subList(startIndex,endIndex);
        pageVo.setRows(handleList);
        return pageVo;
    }

    @PreAuthorize("@ss.hasPermi('template:medical:list')")
    @PostMapping("/analysis")
    @ResponseBody
    public AjaxResult analysis(ExamStatisticVo examStatisticVo) {
        return AjaxResult.success(fMedicalStatisticalService.queryAnalysisData(examStatisticVo.getMedicalFormId(), examStatisticVo));
    }

}
