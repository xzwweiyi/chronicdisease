package com.dchealth.cro.medical.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.form.domain.form.FMedicalPropertyForm;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.domain.PatientRecordHealth;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import com.dchealth.cro.medical.form.PatientRecordSaveUpdateInfoForm;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.mapper.PatientRecordMapper;
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

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("/medical/healthManage")
public class PatientRecordHealthController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(PatientRecordHealthController.class);

    @Autowired
    private PatientRecordHealthService patientRecordHealthService;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private PatientAccountMapper patientAccountMapper;

    /**
     * 权限校验：验证 recordId 是否属于当前登录用户
     */
    private boolean validateRecordIdAccess(Long recordId, HttpServletRequest request) {
        String deviceToken = request.getHeader("deviceToken");
        if (!StringUtils.isNotBlankAndNotEmpty(deviceToken)) {
            return true; // 无deviceToken时跳过校验（兼容旧逻辑）
        }
        PatientAccount currentAccount = patientAccountMapper.selectPatientAccountByOpenId(deviceToken);
        if (currentAccount == null) {
            L.warn("越权访问尝试: deviceToken={}, 用户不存在", deviceToken);
            return false;
        }
        // 通过 recordId 获取 patientId
        PatientRecord patientRecord = patientRecordMapper.selectPatientRecordByRecordId(recordId);
        if (patientRecord == null) {
            return false;
        }
        // 通过 patientId 获取 accountId
        PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(patientRecord.getPatientId());
        if (patientInfo == null || !currentAccount.getAccountId().equals(patientInfo.getAccountId())) {
            L.warn("越权访问尝试: deviceToken={}, 请求recordId={}, 实际accountId不匹配",
                deviceToken, recordId);
            return false;
        }
        return true;
    }

    /**
     * 构建权限拒绝响应
     */
    private String buildAccessDeniedResponse() {
        DchealthCloudResponseEntity<Object> result = new DchealthCloudResponseEntity<>();
        result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
        result.setMsg("无权访问该用户信息");
        return JSON.toJSONString(result);
    }

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
    public String healthList(PatientRecordHealthReq healthListReq, HttpServletRequest request) {
        // 权限校验
        if (healthListReq.getRecordId() != null && !validateRecordIdAccess(healthListReq.getRecordId(), request)) {
            return buildAccessDeniedResponse();
        }

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
    public String healthInfo(PatientRecordHealthReq healthListReq, HttpServletRequest request) {

        try {
            if (healthListReq.getRecordId() == null && healthListReq.getHealthManageType() == null) {
                return buildErrorParamMView();
            }
            // 权限校验
            if (healthListReq.getRecordId() != null && !validateRecordIdAccess(healthListReq.getRecordId(), request)) {
                return buildAccessDeniedResponse();
            }
            return buildSuccessMView(patientRecordHealthService.patientRecordHealthDetail(healthListReq));
        } catch (Exception e) {
            L.info("患者档案详情异常{}", JSON.toJSONString(healthListReq), e);
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @RequestMapping(value = "healthType", method = RequestMethod.POST)
    public String healthType(PatientRecordHealthReq healthListReq) {

        try {
            if (healthListReq.getFollowId() == null) {
                return buildErrorParamMView();
            }
            return buildSuccessMView(patientRecordHealthService.healthType(healthListReq));
        } catch (Exception e) {
            L.info("患者档案详情异常{}", JSON.toJSONString(healthListReq), e);
            return buildExceptionMView();
        }
    }


}