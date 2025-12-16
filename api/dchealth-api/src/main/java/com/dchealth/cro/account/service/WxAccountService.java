package com.dchealth.cro.account.service;


import cn.hutool.core.util.ObjUtil;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.form.req.UpdateUserInfoReq;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.health.domain.MpPatientAccount;
import com.dchealth.cro.health.mapper.MpPatientAccountMapper;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/12/5 11:44
 * @Version: 1.0
 */
@Slf4j
@Service
public class WxAccountService {

    @Resource
    private PatientAccountMapper patientAccountMapper;
    @Autowired
    private PatientInfoMapper patientInfoMapper;
    @Autowired
    private MpPatientAccountMapper mpPatientAccountMapper;

    public Boolean updateLoginPhone(UpdateUserInfoReq updateUserInfoReq, String channel) {

        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(updateUserInfoReq.getAccountId());
        if (ObjUtil.isNull(patientAccount)) {
            throw new RuntimeException("用户不存在");
        }

        patientAccount.setMobile(updateUserInfoReq.getLoginPhone());
        patientAccountMapper.updatePatientAccount(patientAccount);

        log.info("更新用户手机号成功");
        return true;
    }

    public Object updatePatientInfo(UpdateUserInfoReq updateUserInfoReq, String channel) {

        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(updateUserInfoReq.getAccountId());
        if (ObjUtil.isNull(patientAccount)) {
            throw new RuntimeException("用户不存在");
        }

        // 判断用户是否加入队列，如果加入队列则不允许修改
        Boolean joinQueue = isJoinQueue(updateUserInfoReq.getAccountId(), channel);
        if (joinQueue) {
            throw new RuntimeException("用户已加入队列，不允许修改");
        }

        //mp_patient_account
        //patient_account   personId、 idCard
        MpPatientAccount mpPatientAccount = mpPatientAccountMapper.selectMpPatientAccountBypersonId(patientAccount.getRegistrySigns());
        if (mpPatientAccount == null) {
            MpPatientAccount mp = new MpPatientAccount();
            mp.setAccountId(updateUserInfoReq.getAccountId());
            mp.setPersonId(updateUserInfoReq.getPersonId());
            mpPatientAccountMapper.insertMpPatientAccount(mp);
        } else {
            if (!mpPatientAccount.getPersonId().equals(updateUserInfoReq.getPersonId())) {
                mpPatientAccount.setAccountId(updateUserInfoReq.getAccountId());
                mpPatientAccount.setPersonId(updateUserInfoReq.getPersonId());
                mpPatientAccountMapper.deleteMpPatientAccountByPatientId(mpPatientAccount.getPersonId());
                mpPatientAccountMapper.insertMpPatientAccount(mpPatientAccount);
            }
        }

        if (!patientAccount.getRegistrySigns().equals(updateUserInfoReq.getPersonId())) {
            patientAccount.setRegistrySigns(updateUserInfoReq.getPersonId());
        }
        if (!patientAccount.getIdCard().equals(updateUserInfoReq.getCardNo())) {
            patientAccount.setIdCard(updateUserInfoReq.getCardNo());
        }
        patientAccountMapper.updatePatientAccount(patientAccount);

        return null;
    }

    public Boolean isJoinQueue(Long accountId, String channel) {
        // 查询patientInfo表
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(accountId);
        if (ObjUtil.isNotNull(patientAccount)) {
            throw new RuntimeException("用户不存在");
        }

        // 查看patientInfo表
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPersonId(patientAccount.getRegistrySigns());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfo);
        return ObjUtil.isEmpty(patientInfos);
    }
}
