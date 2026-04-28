package com.dchealth.cro.minprogram.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.base.common.FileCommon;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.form.*;
import com.dchealth.cro.medical.service.MedicalPatientService;
import com.dchealth.cro.question.form.QuestionRecordForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(value = "/min_program/medical/patient")
public class MinMedicalPatientController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(MinMedicalPatientController.class);
    @Autowired
    private MedicalPatientService medicalPatientService;
    @Autowired
    private FileCommon fileCommon;


    /**
     * 调试使用
     *
     * @param followId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "testFrom", method = RequestMethod.POST)
    public String testFrom(Long followId) {
        return buildSuccessMView(medicalPatientService.testFrom(followId));
    }

    /**
     * 保存或更新患者档案
     * @param infoForm
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdatePatientRecord", method = RequestMethod.POST)
    public String saveOrUpdatePatientRecord(@Validated PatientRecordSaveUpdateInfoForm infoForm, BindingResult bind, MultipartHttpServletRequest request) {
            L.info("infoForm:{}",JSON.toJSONString(infoForm));
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

            infoForm.setImageUrls(fileCommon.uploadFiles(infoForm.getImageUrls(), request));
            return buildSuccessMView(medicalPatientService.saveOrUpdatePatientRecord(infoForm));
        } catch (Exception e) {
            L.info("保存或更新患者档案异常{}", JSON.toJSONString(infoForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 档案详情
     * @param recordId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "patientRecordDetail", method = RequestMethod.POST)
    public String patientRecordDetail(Long recordId, @RequestParam(value = "detailType", defaultValue = "1") String detailType, HttpServletRequest request) {
        try {
            if (recordId == null) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(medicalPatientService.patientRecordDetail(recordId,detailType,request));
        } catch (Exception e) {
            L.info("患者档案详情异常{}", JSON.toJSONString(recordId), e);
            return buildExceptionMView();
        }
    }


    /**
     * 绑定患者
     *
     * @param bindPatientForm
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bindPatient", method = RequestMethod.POST)
    public String bindPatient(@Validated BindPatientForm bindPatientForm, BindingResult bind) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            return buildSuccessMView(medicalPatientService.bindPatient(bindPatientForm));
        } catch (Exception e) {
            L.info("绑定患者异常{}", JSON.toJSONString(bindPatientForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 绑定患者详情
     * @param bindPatientForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "bindPatientDetail", method = RequestMethod.POST)
    public String bindPatientDetail(@Validated BindPatientDetailForm bindPatientForm) {
        try {
            return buildSuccessMView(medicalPatientService.bindPatientDetail(bindPatientForm));
        } catch (Exception e) {
            L.info("绑定患者详情异常{}", JSON.toJSONString(bindPatientForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 队列申请【确认按钮】
     *
     * @param applyResearchForm
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "applyResearch", method = RequestMethod.POST)
    public String applyResearch(@Validated ApplyResearchForm applyResearchForm, BindingResult bind) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            return buildSuccessMView(medicalPatientService.applyResearch(applyResearchForm));
        } catch (Exception e) {
            L.info("队列申请异常{}", JSON.toJSONString(applyResearchForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 扫码申请队列
     *
     * @param applyResearchForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scanCodeResearch", method = RequestMethod.POST)
    public String scanCodeResearch(@Validated ApplyResearchForm applyResearchForm) {
        try {
            return buildSuccessMView(medicalPatientService.scanCodeResearch(applyResearchForm));
        } catch (Exception e) {
            L.info("扫码申请队列异常{}", JSON.toJSONString(applyResearchForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 患者基本信息
     *
     * @param patientInfo
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "patientBaseInfo", method = RequestMethod.POST)
    public String patientBaseInfo(Long accountId, HttpServletRequest request) {
        L.info("accountId:{}",accountId);
        try {
            if (accountId == null) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(medicalPatientService.selectPatientBaseInfoByAccountId(accountId, request));
        } catch (Exception e) {
            L.info("患者基本信息获取{},{}", accountId, e);
            return buildExceptionMView();
        }
    }

    /**
     * 患者随访记录
     *
     * @param patientRecord
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "patientRecordList", method = RequestMethod.POST)
    public String patientRecordList(@Validated PatientRecord patientRecord, BindingResult bind) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            return buildSuccessMView(medicalPatientService.selectPatientRecordList(patientRecord));
        } catch (Exception e) {
            L.info("患者基本信息获取{},{}", JSON.toJSONString(patientRecord), e);
            return buildExceptionMView();
        }
    }

  




}
