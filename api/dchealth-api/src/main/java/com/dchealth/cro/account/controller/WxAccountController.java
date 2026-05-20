package com.dchealth.cro.account.controller;


import com.dchealth.cro.account.form.req.UpdateUserInfoReq;
import com.dchealth.cro.account.service.WxAccountService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.medical.form.MobileForm;
import com.dchealth.cro.medical.service.MedicalPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        if(updateUserInfoReq.getAccountId() !=null){
            updateUserInfoReq.setAccountId(Long.valueOf(rsaUtil.decryptValue(updateUserInfoReq.getAccountId().toString())));
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
        if(updateUserInfoReq.getAccountId() !=null){
            updateUserInfoReq.setAccountId(Long.valueOf(rsaUtil.decryptValue(updateUserInfoReq.getAccountId().toString())));
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
    public String isJoinQueue(Long accountId, @RequestAttribute("channel") String channel) {
        boolean b = notEmpty(accountId);
        if (!b) {
            return buildErrorParamMView();
        }
        if(accountId !=null){
            accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
        }
        try {
            return buildSuccessMView(accountService.isJoinQueue(accountId, channel));
        } catch (Exception e) {
            return buildErrorParamMView(e);
        }
    }}
