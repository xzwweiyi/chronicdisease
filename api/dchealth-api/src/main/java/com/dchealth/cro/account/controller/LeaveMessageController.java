package com.dchealth.cro.account.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.form.req.UserMessageListReq;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.account.service.LeaveMessageService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.health.domain.MpPatientAccount;
import com.dchealth.cro.health.mapper.MpPatientAccountMapper;
import com.dchealth.cro.medical.form.LeaveMessageForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @Author: wangpf
 * @Description: TODO 患者留言管理
 * @Date: 2025/6/19 9:55
 * @Version: 1.0
 */

@Slf4j
@Controller
@RequestMapping(value = "/leaveMessage")
public class LeaveMessageController extends ApiBaseController {

    @Autowired
    private LeaveMessageService leaveMessageService;

    @Autowired
    private PatientAccountMapper patientAccountMapper;

    @Autowired
    private MpPatientAccountMapper mpPatientAccountMapper;
    @Autowired
    private RsaUtil rsaUtil;

    /**
     * 权限校验：验证 personId 和 accountId 是否属于当前登录用户
     */
    private boolean validateAccess(String personId, Long accountId, HttpServletRequest request) {
        String deviceToken = request.getHeader("deviceToken");
        if (!StringUtils.isNotBlankAndNotEmpty(deviceToken)) {
            return true; // 无deviceToken时跳过校验（兼容旧逻辑）
        }
        PatientAccount currentAccount = patientAccountMapper.selectPatientAccountByOpenId(deviceToken);
        if (currentAccount == null) {
            log.warn("越权访问尝试: deviceToken={}, 用户不存在", deviceToken);
            return false;
        }
        // 验证 accountId
        if (accountId != null && !currentAccount.getAccountId().equals(accountId)) {
            log.warn("越权访问尝试: deviceToken={}, 请求accountId={}, 实际accountId={}",
                deviceToken, accountId, currentAccount.getAccountId());
            return false;
        }
        // 验证 personId
        if (StringUtils.isNotBlankAndNotEmpty(personId)) {
            MpPatientAccount mpPatientAccount = mpPatientAccountMapper.selectMpPatientAccountByAccountId(currentAccount.getAccountId());
            if (mpPatientAccount == null || !personId.equals(mpPatientAccount.getPersonId())) {
                log.warn("越权访问尝试: deviceToken={}, 请求personId={}, 实际personId={}",
                    deviceToken, personId, mpPatientAccount != null ? mpPatientAccount.getPersonId() : "null");
                return false;
            }
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

    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public String list(UserMessageListReq messageListReq, HttpServletRequest request) {
        try {
            if (ObjectUtil.isNull(messageListReq.getPersonId())) {
                return buildSuccessMView(new ArrayList<>());
            }
            // 权限校验
            if (!validateAccess(messageListReq.getPersonId(), messageListReq.getFromUserId(), request)) {
                return buildAccessDeniedResponse();
            }
            if(StringUtils.isNotEmpty(messageListReq.getPersonId())){
                messageListReq.setPersonId(rsaUtil.decryptValue(messageListReq.getPersonId()));
            }
            return buildSuccessMView(leaveMessageService.list(messageListReq));
        } catch (Exception e) {
            log.info("获取留言信息列表异常{},{}", JSON.toJSONString(messageListReq), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @PostMapping(value = "send")
    public String send(LeaveMessageForm messageForm, HttpServletRequest request) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<>();
                result.setMsg(CroApiCodeConstant.SEND_MESSAGE_ERROR.getMsg());
                result.setCode(CroApiCodeConstant.SEND_MESSAGE_ERROR.getCode());
                return JSON.toJSONString(result);
            }
            if(StringUtils.isNotEmpty(messageForm.getPersonId())){
                messageForm.setPersonId(rsaUtil.decryptValue(messageForm.getPersonId()));
            }
            if(messageForm.getAccountId() != null){
                messageForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageForm.getAccountId().toString())));
            }
            // 权限校验
            if (!validateAccess(messageForm.getPersonId(), messageForm.getAccountId(), request)) {
                return buildAccessDeniedResponse();
            }

            return buildSuccessMView(leaveMessageService.saveLeaveMessage(messageForm));
        } catch (Exception e) {
            log.info("发送留言信息失败异常{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }

    @ResponseBody
    @RequestMapping(value = "reminder", method = RequestMethod.POST)
    public String reminder(LeaveMessageForm messageForm, HttpServletRequest request) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                log.info("personId为空");
                return buildSuccessMView(false);
            }
            if(StringUtils.isNotEmpty(messageForm.getPersonId())){
                messageForm.setPersonId(rsaUtil.decryptValue(messageForm.getPersonId()));
            }
            if(messageForm.getAccountId() != null){
                messageForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageForm.getAccountId().toString())));
            }
            // 权限校验
            if (!validateAccess(messageForm.getPersonId(), messageForm.getAccountId(), request)) {
                return buildAccessDeniedResponse();
            }
            return buildSuccessMView(leaveMessageService.reminder(messageForm));
        } catch (Exception e) {
            log.info("获取留言消息提示失败{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    public String updateStatus(LeaveMessageForm messageForm, HttpServletRequest request) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                log.info("更新状态，personId为空");
                return buildSuccessMView(false);
            }
            if(StringUtils.isNotEmpty(messageForm.getPersonId())){
                messageForm.setPersonId(rsaUtil.decryptValue(messageForm.getPersonId()));
            }
            if(messageForm.getAccountId() != null){
                messageForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageForm.getAccountId().toString())));
            }
            // 权限校验
            if (!validateAccess(messageForm.getPersonId(), messageForm.getAccountId(), request)) {
                return buildAccessDeniedResponse();
            }
            return buildSuccessMView(leaveMessageService.updateStatus(messageForm));
        } catch (Exception e) {
            log.info("更新留言状态失败{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }

    @ResponseBody
    @RequestMapping(value = "clear", method = RequestMethod.POST)
    public String clear(LeaveMessageForm messageForm, HttpServletRequest request) {
        try {
            if(StringUtils.isNotEmpty(messageForm.getPersonId())){
                messageForm.setPersonId(rsaUtil.decryptValue(messageForm.getPersonId()));
            }
            if(messageForm.getAccountId() != null){
                messageForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageForm.getAccountId().toString())));
            }
            // 权限校验
            if (!validateAccess(messageForm.getPersonId(), messageForm.getAccountId(), request)) {
                return buildAccessDeniedResponse();
            }
            return buildSuccessMView(leaveMessageService.clear(messageForm));
        } catch (Exception e) {
            log.info("清空留言失败{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @RequestMapping(value = "beachClear", method = RequestMethod.POST)
    public String beachClear(LeaveMessageForm messageForm, HttpServletRequest request) {
        try {
            if(StringUtils.isNotEmpty(messageForm.getPersonId())){
                messageForm.setPersonId(rsaUtil.decryptValue(messageForm.getPersonId()));
            }
            if(messageForm.getAccountId() != null){
                messageForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(messageForm.getAccountId().toString())));
            }
            // 权限校验
            if (!validateAccess(messageForm.getPersonId(), messageForm.getAccountId(), request)) {
                return buildAccessDeniedResponse();
            }
            return buildSuccessMView(leaveMessageService.beachClear(messageForm));
        } catch (Exception e) {
            log.info("批量清空留言{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }

}