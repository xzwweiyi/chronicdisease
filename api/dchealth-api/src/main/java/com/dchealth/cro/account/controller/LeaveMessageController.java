package com.dchealth.cro.account.controller;


import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.form.req.UserMessageListReq;
import com.dchealth.cro.account.service.LeaveMessageService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.medical.form.LeaveMessageForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public String list(UserMessageListReq messageListReq) {
        try {
            if (ObjectUtil.isNull(messageListReq.getPersonId())) {
                return buildSuccessMView(new ArrayList<>());
            }
            return buildSuccessMView(leaveMessageService.list(messageListReq));
        } catch (Exception e) {
            log.info("获取留言信息列表异常{},{}", JSON.toJSONString(messageListReq), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @PostMapping(value = "send")
    public String send(LeaveMessageForm messageForm) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<>();
                result.setMsg(CroApiCodeConstant.SEND_MESSAGE_ERROR.getMsg());
                result.setCode(CroApiCodeConstant.SEND_MESSAGE_ERROR.getCode());
                return JSON.toJSONString(result);
            }
            return buildSuccessMView(leaveMessageService.saveLeaveMessage(messageForm));
        } catch (Exception e) {
            log.info("发送留言信息失败异常{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }

    @ResponseBody
    @RequestMapping(value = "reminder", method = RequestMethod.POST)
    public String reminder(LeaveMessageForm messageForm) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                log.info("personId为空");
                return buildSuccessMView(false);
            }
            return buildSuccessMView(leaveMessageService.reminder(messageForm));
        } catch (Exception e) {
            log.info("获取留言消息提示失败{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    public String updateStatus(LeaveMessageForm messageForm) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                log.info("更新状态，personId为空");
                return buildSuccessMView(false);
            }
            return buildSuccessMView(leaveMessageService.updateStatus(messageForm));
        } catch (Exception e) {
            log.info("更新留言状态失败{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }

    @ResponseBody
    @RequestMapping(value = "clear", method = RequestMethod.POST)
    public String clear(LeaveMessageForm messageForm) {
        try {
            return buildSuccessMView(leaveMessageService.clear(messageForm));
        } catch (Exception e) {
            log.info("清空留言失败{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }


    @ResponseBody
    @RequestMapping(value = "beachClear", method = RequestMethod.POST)
    public String beachClear(LeaveMessageForm messageForm) {
        try {
            if (ObjectUtil.isNull(messageForm.getPersonId())) {
                log.info("批量清空留言，personId为空");
                return buildSuccessMView(0);
            }
            return buildSuccessMView(leaveMessageService.beachClear(messageForm));
        } catch (Exception e) {
            log.info("批量清空留言{}, {}", JSON.toJSONString(messageForm), e.getMessage());
            return buildExceptionMView();
        }
    }

}
