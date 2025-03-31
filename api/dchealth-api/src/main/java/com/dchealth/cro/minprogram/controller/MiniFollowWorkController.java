package com.dchealth.cro.minprogram.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.medical.form.FollowWorkEnrollmentHandleForm;
import com.dchealth.cro.medical.service.FollowWorkService;

/**
 * 随访工作站
 */
@RestController
@RequestMapping("/min_program/follow/work")
public class MiniFollowWorkController extends ApiBaseController {

    private static final Logger L = LoggerFactory.getLogger(MiniFollowWorkController.class);
    @Autowired
    private FollowWorkService followWorkService;



    // 小程序 todo 真正的确认入组或者取消入组
    /**
     * 确认入组/修改入组/待分配入组
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enrollment/handle", method = RequestMethod.POST)
    public String enrollmentHandle(FollowWorkEnrollmentHandleForm workEnrollmentHandleForm)
    {
        try {
            return buildSuccessMView(followWorkService.enrollmentHandle(workEnrollmentHandleForm));
        } catch (Exception e) {
            L.info("入组操作异常{}", JSON.toJSONString(workEnrollmentHandleForm), e);
            return buildErrorParamMView(e);
        }

    }

}
