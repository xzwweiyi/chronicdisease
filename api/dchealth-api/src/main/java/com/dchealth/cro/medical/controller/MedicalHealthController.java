package com.dchealth.cro.medical.controller;


import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import com.dchealth.cro.medical.form.PatientRecordSaveUpdateInfoForm;
import com.dchealth.cro.medical.service.MedicalPatientService;
import com.dchealth.cro.question.form.QuestionRecordForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/6/18 15:26
 * @Version: 1.0
 */
@Slf4j
@Controller
@RequestMapping(value = "/medical/health")
public class MedicalHealthController extends ApiBaseController {

    @Resource
    private MedicalPatientService medicalPatientService;

    /**
     * 获取健康管理列表
     * @param patientRecord
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "healthManageList", method = RequestMethod.POST)
    public String healthManageList(@Validated PatientRecord patientRecord, BindingResult bind) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if (-1L==patientRecord.getPatientId()) {
                DchealthCloudResponseEntity<?> result = new DchealthCloudResponseEntity<>();
                result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
                return JSON.toJSONString(result);
            }
            return buildSuccessMView(medicalPatientService.selectHealthManageList(patientRecord));
        } catch (Exception e) {
            log.info("患者基本信息获取{},{}", JSON.toJSONString(patientRecord), e.getMessage());
            return buildExceptionMView();
        }
    }

    /**
     * 保存或更新患者健康管理信息
     * @param infoForm
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdatePatientRecordHealth", method = RequestMethod.POST)
    public String saveOrUpdatePatientRecordHealth(@Validated PatientRecordSaveUpdateInfoForm infoForm, BindingResult bind) {
        log.info("infoForm:{}",JSON.toJSONString(infoForm));
        List<Integer> submitTypes = Arrays.asList(0, 1);
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(!submitTypes.contains(infoForm.getIsSubmit())){
                DchealthCloudResponseEntity<?> result = new DchealthCloudResponseEntity<>();
                result.setCode(CroApiCodeConstant.WRONG_TYPE_OF_SUBMISSION.getCode());
                result.setMsg(CroApiCodeConstant.WRONG_TYPE_OF_SUBMISSION.getMsg());
                return JSON.toJSONString(result);
            }

            if (StringUtils.isNotBlankAndNotEmpty(infoForm.getImageUrlsJson())){
                List<PatientRecordFilesForm> list = JSON.parseArray(infoForm.getImageUrlsJson(), PatientRecordFilesForm.class);
                infoForm.setImageUrls(list);
            }
            if (StringUtils.isNotBlankAndNotEmpty(infoForm.getPropertyFormsJson())){
                List<FMedicalPropertyForm> list = JSON.parseArray(infoForm.getPropertyFormsJson(), FMedicalPropertyForm.class);
                infoForm.setPropertyForms(list);
            }

            if (StringUtils.isNotBlankAndNotEmpty(infoForm.getQuestionRecordJson())){
                QuestionRecordForm questionRecordForm = JSON.parseObject(infoForm.getQuestionRecordJson(), QuestionRecordForm.class);
                infoForm.setQuestionRecordForm(questionRecordForm);
            }

            return buildSuccessMView(medicalPatientService.saveOrUpdatePatientRecord(infoForm));
        } catch (Exception e) {
            log.info("保存或更新患者档案异常{}", JSON.toJSONString(infoForm), e);
            return buildExceptionMView();
        }
    }

}
