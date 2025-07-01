package com.dchealth.cro.medical.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.medical.domain.PatientRecordHealth;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import com.dchealth.cro.medical.form.PatientRecordSaveUpdateInfoForm;
import com.dchealth.cro.medical.service.PatientRecordHealthService;
import com.dchealth.cro.medical.vo.health.PatientRecordHealthReq;
import com.dchealth.cro.question.form.QuestionRecordForm;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 患者档案健康管理Controller
 *
 * @author lchbu
 * @date 2025-06-24
 */
@Slf4j
@RestController
@RequestMapping("/system/health")
public class PatientRecordHealthController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(PatientRecordHealthController.class);

    @Autowired
    private PatientRecordHealthService patientRecordHealthService;

    /**
     * 保存或更新患者档案
     *
     * @param infoForm
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdatePatientRecordHealth", method = RequestMethod.POST)
    public String saveOrUpdatePatientRecordHealth(@Validated PatientRecordSaveUpdateInfoForm infoForm, BindingResult bind) {
        log.info("infoForm:{}", JSON.toJSONString(infoForm));
        List<Integer> submitTypes = Arrays.asList(0, 1);
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if (!submitTypes.contains(infoForm.getIsSubmit())) {
                DchealthCloudResponseEntity<?> result = new DchealthCloudResponseEntity<>();
                result.setCode(CroApiCodeConstant.WRONG_TYPE_OF_SUBMISSION.getCode());
                result.setMsg(CroApiCodeConstant.WRONG_TYPE_OF_SUBMISSION.getMsg());
                return JSON.toJSONString(result);
            }

            if (StringUtils.isNotBlankAndNotEmpty(infoForm.getImageUrlsJson())) {
                List<PatientRecordFilesForm> list = JSON.parseArray(infoForm.getImageUrlsJson(), PatientRecordFilesForm.class);
                infoForm.setImageUrls(list);
            }
            if (StringUtils.isNotBlankAndNotEmpty(infoForm.getPropertyFormsJson())) {
                List<FMedicalPropertyForm> list = JSON.parseArray(infoForm.getPropertyFormsJson(), FMedicalPropertyForm.class);
                infoForm.setPropertyForms(list);
            }

            if (StringUtils.isNotBlankAndNotEmpty(infoForm.getQuestionRecordJson())) {
                QuestionRecordForm questionRecordForm = JSON.parseObject(infoForm.getQuestionRecordJson(), QuestionRecordForm.class);
                infoForm.setQuestionRecordForm(questionRecordForm);
            }

            return buildSuccessMView(patientRecordHealthService.saveOrUpdatePatientRecordHealth(infoForm));
        } catch (Exception e) {
            log.info("保存或更新患者档案异常{}, {}", JSON.toJSONString(infoForm), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @RequestMapping(value = "healthList", method = RequestMethod.POST)
    public String healthList(PatientRecordHealthReq healthListReq) {
        // 通过recordId获取到对应的健康管理的档案信息
        DchealthCloudResponseEntity<List<PatientRecordHealth>> result = new DchealthCloudResponseEntity<>();
        PatientRecordHealth patientRecordHealth = new PatientRecordHealth();
        patientRecordHealth.setRecordId(healthListReq.getRecordId());
        List<PatientRecordHealth> healthList = patientRecordHealthService.selectPatientRecordHealthList(patientRecordHealth);
        if (ObjectUtil.isEmpty(healthList)) {
            return JSON.toJSONString(result);
        }
        result.setData(healthList);
        return buildSuccessMView(result);
    }


    @ResponseBody
    @RequestMapping(value = "healthInfo", method = RequestMethod.POST)
    public String healthInfo(PatientRecordHealthReq healthListReq) {

        try {
            if (healthListReq.getRecordId() == null) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(patientRecordHealthService.patientRecordHealthDetail(healthListReq));
        } catch (Exception e) {
            L.info("患者档案详情异常{}", JSON.toJSONString(healthListReq), e);
            return buildExceptionMView();
        }
    }


}
