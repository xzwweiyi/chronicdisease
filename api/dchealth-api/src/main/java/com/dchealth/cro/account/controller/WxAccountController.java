package com.dchealth.cro.account.controller;


import cn.hutool.core.net.URLDecoder;
import com.dchealth.cro.account.form.req.UpdateUserInfoReq;
import com.dchealth.cro.account.service.WxAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.medical.form.MobileForm;
import com.dchealth.cro.medical.service.MedicalPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/12/5 11:42
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/wxAccount")
public class WxAccountController extends ApiBaseController {

    @Autowired
    private WxAccountService accountService;
    @Autowired
    private RsaUtil rsaUtil;

    /**
     * 编辑登录手机号，personId
     */
    @ResponseBody
    @RequestMapping(value = "updateLoginPhone", method = RequestMethod.POST)
    public String updateLoginPhone(UpdateUserInfoReq updateUserInfoReq, @RequestAttribute("channel") String channel) {
        boolean b = notEmpty(updateUserInfoReq);
        if (!b) {
            return buildErrorParamMView();
        }
        if(StringUtils.isNotEmpty(updateUserInfoReq.getAccountId())){
            updateUserInfoReq.setAccountIds(Long.valueOf(rsaUtil.decryptValue(updateUserInfoReq.getAccountId())));
        }
        try {
            return buildSuccessMView(accountService.updateLoginPhone(updateUserInfoReq, channel));
        } catch (Exception e) {
            return buildErrorParamMView(e);
        }
    }


    /**
     * 修改绑定的患者身份证号和患者id
     */
    @ResponseBody
    @RequestMapping(value = "updatePatientInfo", method = RequestMethod.POST)
    public String updatePatientInfo(UpdateUserInfoReq updateUserInfoReq, @RequestAttribute("channel") String channel) {
        boolean b = notEmpty(updateUserInfoReq);
        if (!b) {
            return buildErrorParamMView();
        }
        if(StringUtils.isNotEmpty(updateUserInfoReq.getAccountId())){
            updateUserInfoReq.setAccountIds(Long.valueOf(rsaUtil.decryptValue(updateUserInfoReq.getAccountId())));
        }
        if(StringUtils.isNotEmpty(updateUserInfoReq.getPersonId())){
            updateUserInfoReq.setPersonId(rsaUtil.decryptValue(updateUserInfoReq.getPersonId()));
        }
        try {
            return buildSuccessMView(accountService.updatePatientInfo(updateUserInfoReq, channel));
        } catch (Exception e) {
            return buildErrorParamMView(e);
        }
    }


    /**
     * 判断患者是否加入队列
     */
    @ResponseBody
    @RequestMapping(value = "isJoinQueue", method = RequestMethod.POST)
    public String isJoinQueue(String accountId, @RequestAttribute("channel") String channel) {
        boolean b = notEmpty(accountId);
        if (!b) {
            return buildErrorParamMView();
        }
        Long accountIds = null;
        if(StringUtils.isNotEmpty(accountId)){
            accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
        }
        try {
            return buildSuccessMView(accountService.isJoinQueue(accountIds, channel));
        } catch (Exception e) {
            return buildErrorParamMView(e);
        }
    }}
