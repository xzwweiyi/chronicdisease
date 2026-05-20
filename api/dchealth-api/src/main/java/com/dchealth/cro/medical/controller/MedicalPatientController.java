package com.dchealth.cro.medical.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.medical.form.*;
import com.dchealth.cro.research.domain.ReSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.base.common.FileCommon;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.service.MedicalPatientService;
import com.dchealth.cro.question.form.QuestionRecordForm;

@Controller
@RequestMapping(value = "/medical/patient")
public class MedicalPatientController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(MedicalPatientController.class);
    @Autowired
    private MedicalPatientService medicalPatientService;
    @Autowired
    private FileCommon fileCommon;
    @Autowired
    private RsaUtil rsaUtil;

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
     * 保存或更新患者档案
     * @param infoForm
     * @param bind
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdatePatientRecordMin", method = RequestMethod.POST)
    public String saveOrUpdatePatientRecordMin(@Validated PatientRecordSaveUpdateInfoForm infoForm, BindingResult bind) {
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
        L.info("bindPatient:{}",JSON.toJSONString(bindPatientForm));
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(bindPatientForm.getAccountId() !=null){
                bindPatientForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(bindPatientForm.getAccountId().toString())));
            }
            return buildSuccessMessageView(medicalPatientService.bindPatient(bindPatientForm));
        } catch (Exception e) {
            L.info("绑定患者异常{}", JSON.toJSONString(bindPatientForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 更新用户的手机号码
     *
     * @param form 经过验证的表单对象，包含要更新的手机号码信息
     * @param bind 用于检查表单验证是否有错误的BindingResult对象
     * @return 返回更新手机号码的结果字符串
     */
    @ResponseBody
    @PostMapping(value = "updateMobile")
    public String updateMobile(@Validated MobileForm form, BindingResult bind) {
        L.info("updateMobile:{}",JSON.toJSONString(form));
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(form.getPersonId())){
                form.setPersonId(rsaUtil.decryptValue(form.getPersonId()));
            }
            String message = medicalPatientService.updateMobile(form);
            DchealthCloudResponseEntity<?> result = new DchealthCloudResponseEntity<>();
            result.setMsg(message);
            result.setCode(CroApiCodeConstant.SUCCESS.getCode());
            return buildSuccessMView(result);
        } catch (Exception e) {
            L.info("绑定患者异常{}", JSON.toJSONString(form), e);
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
            if(bindPatientForm.getAccountId() !=null){
                bindPatientForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(bindPatientForm.getAccountId().toString())));
            }
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
            if(applyResearchForm.getAccountId() !=null){
                applyResearchForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(applyResearchForm.getAccountId().toString())));
            }
            if(StringUtils.isNotEmpty(applyResearchForm.getPersonId())){
                applyResearchForm.setPersonId(rsaUtil.decryptValue(applyResearchForm.getPersonId()));
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
            if(accountId !=null){
                accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
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
    public String patientRecordList(@Validated PatientRecord patientRecord, BindingResult bind, HttpServletRequest request) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(patientRecord.getAccountId() !=null){
                patientRecord.setAccountId(Long.valueOf(rsaUtil.decryptValue(patientRecord.getAccountId().toString())));
            }
            if (-1L==patientRecord.getPatientId()) {
//                    result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
//                    return result;
                DchealthCloudResponseEntity<?> result = new DchealthCloudResponseEntity<>();
//                result.setMsg(error.getDefaultMessage());
                result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
                return JSON.toJSONString(result);
            }
            return buildSuccessMView(medicalPatientService.selectPatientRecordList(patientRecord, request));
        } catch (Exception e) {
            L.info("患者基本信息获取{},{}", JSON.toJSONString(patientRecord), e);
            return buildExceptionMView();
        }
    }


    /**
     * 查询状态
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkResearchStatus", method = RequestMethod.POST)
    public String checkResearchStatus(ReSearchForm reSearchForm) {
        try {
            if (Objects.isNull(reSearchForm.getResearchId())||reSearchForm.getResearchId()==-1) {
                return buildErrorMView(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
            }
            if(reSearchForm.getAccountId() !=null){
                reSearchForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(reSearchForm.getAccountId().toString())));
            }
            return buildSuccessMView(medicalPatientService.researchStatusInfo(reSearchForm));
        } catch (Exception e) {
            L.info("扫码申请队列异常{}", JSON.toJSONString(reSearchForm), e);
            return buildExceptionMView();
        }
    }



}
