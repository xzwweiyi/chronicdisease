package com.dchealth.cro.researchPatient.controller;

import cn.hutool.core.net.URLDecoder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.health.domain.MpPatientAccount;
import com.dchealth.cro.health.mapper.MpPatientAccountMapper;
import com.dchealth.cro.researchPatient.domain.MyQueue;
import com.dchealth.cro.researchPatient.domain.QuitRecord;
import com.dchealth.cro.researchPatient.service.MyQueueService;
import com.dchealth.cro.researchPatient.service.QuitRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 我的队列
 * @author zhangxiao
 */
@RestController
@RequestMapping(value = "/myQueue")
public class MyQueueController extends ApiBaseController {
    private static final Logger log = LoggerFactory.getLogger(MyQueueController.class);

    @Autowired
    private MyQueueService queueService;

    @Autowired
    private QuitRecordService quitRecordService;

    @Autowired
    private PatientAccountMapper patientAccountMapper;

    @Autowired
    private MpPatientAccountMapper mpPatientAccountMapper;
    @Autowired
    private RsaUtil rsaUtil;
    /**
     * 权限校验：验证 personId 是否属于当前登录用户
     */
    private boolean validatePersonIdAccess(String personId, HttpServletRequest request) {
        String deviceToken = request.getHeader("deviceToken");
        if (!StringUtils.isNotBlankAndNotEmpty(deviceToken)) {
            return true; // 无deviceToken时跳过校验（兼容旧逻辑）
        }
        PatientAccount currentAccount = patientAccountMapper.selectPatientAccountByOpenId(deviceToken);
        if (currentAccount == null) {
            log.warn("越权访问尝试: deviceToken={}, 请求personId={}, 用户不存在", deviceToken, personId);
            return false;
        }
        MpPatientAccount mpPatientAccount = mpPatientAccountMapper.selectMpPatientAccountByAccountId(currentAccount.getAccountId());
        if (mpPatientAccount == null || !personId.equals(mpPatientAccount.getPersonId())) {
            log.warn("越权访问尝试: deviceToken={}, 请求personId={}, 实际personId={}",
                deviceToken, personId, mpPatientAccount != null ? mpPatientAccount.getPersonId() : "null");
            return false;
        }
        return true;
    }

    /**
     * 根据personId查询列表信息
     */
    @GetMapping("findList")
    public String findList(@RequestParam(value = "personId", required = true) String personId, HttpServletRequest request) {
        DchealthCloudResponseEntity<List<MyQueue>> result = new DchealthCloudResponseEntity<>();
        Long personIds = null;
        if(StringUtils.isNotEmpty(personId)){
            personIds = Long.valueOf(rsaUtil.decryptValue(personId));
        }
        // 权限校验
        if (!validatePersonIdAccess(String.valueOf(personIds), request)) {
            result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
            result.setMsg("无权访问该用户信息");
            return buildSuccessMView(result);
        }

        List<MyQueue> list = queueService.findListByPersonId(personIds);
        result.setData(list);
        return buildSuccessMView(result);
    }

    /**
     * 处理保存退出队列的提交请求
     */
    @PostMapping("applyQuitQueue")
    public String applyQuitQueue(@RequestBody QuitRecord quitRecord) {
        DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<>();
        try {
            if(StringUtils.isNotEmpty(quitRecord.getPersonId())){
                quitRecord.setPersonId(rsaUtil.decryptValue(quitRecord.getPersonId()));
            }
            boolean save = quitRecordService.saveInfo(quitRecord);
            if(save) {
                result.setCode(CroApiCodeConstant.APPLY_SUCCESS.getCode());
                return buildSuccessMView(result);
            } else {
                return buildErrorMView(CroApiCodeConstant.FAIL.getCode());
            }
        } catch (Exception e) {
            return buildErrorParamMView(e);
        }
    }

    /**
     * 根据个人ID和队列ID查找退出记录列表
     */
    @GetMapping("findQuitList")
    public String findQuitList(@RequestParam(value = "personId", required = true) String personId,
                               @RequestParam(value = "queueId", required = true) Long queueId,
                               HttpServletRequest request) {
        DchealthCloudResponseEntity<List<QuitRecord>> result = new DchealthCloudResponseEntity<>();
        Long personIds = null;
        if(StringUtils.isNotEmpty(personId)){
            personIds = Long.valueOf(rsaUtil.decryptValue(personId));
        }
        // 权限校验
        if (!validatePersonIdAccess(String.valueOf(personIds), request)) {
            result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
            result.setMsg("无权访问该用户信息");
            return buildSuccessMView(result);
        }

        LambdaQueryWrapper<QuitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuitRecord::getPersonId, personIds);
        wrapper.eq(QuitRecord::getQueueId, queueId);
        List<QuitRecord> quitRecords = quitRecordService.list(wrapper);
        result.setData(quitRecords);
        return buildSuccessMView(result);
    }

}