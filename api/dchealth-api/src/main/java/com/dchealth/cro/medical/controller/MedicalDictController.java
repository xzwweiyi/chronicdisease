package com.dchealth.cro.medical.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.medical.service.MedicalDictService;

/**
 * 字典控制层
 */
@Controller
@RequestMapping(value = "/dict")
public class MedicalDictController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(MedicalDictController.class);
    @Autowired
    private MedicalDictService medicalDictService;

    /**
     * 队列申请字典接口
     * @param deptId
     * @param diseasesId
     * @param researchId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "researchApply", method = RequestMethod.POST)
    public String researchApply(Long deptId, Long diseasesId, Long researchId) {
        try{
            return buildSuccessMView(medicalDictService.researchApply(deptId, diseasesId, researchId));
        }catch (Exception e){
            L.info("队列申请字典接口异常科室ID{},病种ID{},队列ID{}", deptId,diseasesId,researchId, e);
            return buildExceptionMView();
        }

    }

}
