package com.dchealth.cro.account.service;


import cn.hutool.core.util.ObjUtil;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.form.req.UpdateUserInfoReq;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/12/5 11:44
 * @Version: 1.0
 */
@Service
public class WxAccountService {

    @Resource
    private PatientAccountMapper patientAccountMapper;

    public Boolean updateLoginPhone(UpdateUserInfoReq updateUserInfoReq, String channel) {


        return true;
    }

    public Object updatePatientInfo(UpdateUserInfoReq updateUserInfoReq, String channel) {

        return null;
    }

    public Boolean isJoinQueue(Long accountId, String channel) {
        // 查询patientInfo表
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(accountId);
        if (ObjUtil.isNotNull(patientAccount)) {
            throw new RuntimeException("用户不存在");
        }




        return null;
    }
}
