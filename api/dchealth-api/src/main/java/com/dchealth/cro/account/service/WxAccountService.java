package com.dchealth.cro.account.service;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.form.req.UpdateUserInfoReq;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.exception.base.ServiceException;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.health.domain.MpPatientAccount;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPatientAccountMapper;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.form.MobileForm;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.medical.service.MedicalPatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MpPersonMapper mpPersonMapper;

    @Autowired
    private MedicalPatientService medicalPatientService;

    public Boolean updateLoginPhone(UpdateUserInfoReq updateUserInfoReq, String channel) {

        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(updateUserInfoReq.getAccountId());
        if (ObjUtil.isNull(patientAccount)) {
            throw new RuntimeException("用户不存在");
        }

        patientAccount.setMobile(updateUserInfoReq.getLoginMobile());
        patientAccountMapper.updatePatientAccount(patientAccount);

        log.info("更新用户手机号成功");
        return true;
    }

    @Transactional
    public DchealthCloudResponseEntity<Boolean> updatePatientInfo(UpdateUserInfoReq updateUserInfoReq, String channel) {

        DchealthCloudResponseEntity<Boolean> result = new DchealthCloudResponseEntity<>();
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(updateUserInfoReq.getAccountId());
        if (ObjUtil.isNull(patientAccount)) {
            throw new ServiceException("用户不存在");
        }

        // 判断用户是否加入队列，如果加入队列则不允许修改
        Boolean joinQueue = isJoinQueue(updateUserInfoReq.getAccountId(), channel);
        if (!joinQueue) {
            // 判断登录手机号是否发生变化，发生变化则修改手机号，否则不允许修改
            if (patientAccount.getMobile().equals(updateUserInfoReq.getLoginMobile())) {
                throw new ServiceException("手机号未发生改变");
            } else {

                PatientAccount account = patientAccountMapper.selectPatientAccountByMobile(updateUserInfoReq.getLoginMobile());
                if (account != null) {
                    // 如果修改的手机号账号存在则看是否绑定
                    if (account.getBindStatus().equals("0") && StrUtil.isEmpty(account.getRegistrySigns()) && StrUtil.isEmpty(account.getIdCard())) {
                        patientAccountMobileUpdate(updateUserInfoReq, account);
                        result.setData(true);
                        result.setMsg("更新用户手机号成功");
                        return result;
                    } else {
                        throw new ServiceException("修改手机号已存在");
                    }

                } else {
                    patientAccount.setMobile(updateUserInfoReq.getLoginMobile());
                    patientAccountMapper.updatePatientAccount(patientAccount);
                    log.info("更新用户手机号成功");
                    result.setData(true);
                    result.setMsg("更新用户手机号成功");
                    return result;
                }
            }
        }

        if (!patientAccount.getMobile().equals(updateUserInfoReq.getLoginMobile())) {
            PatientAccount account = patientAccountMapper.selectPatientAccountByMobile(updateUserInfoReq.getLoginMobile());
            if (account != null) {
                // 如果修改的手机号账号存在则看是否绑定
                if (account.getBindStatus().equals("1") && StrUtil.isEmpty(account.getRegistrySigns()) && StrUtil.isEmpty(account.getIdCard())) {
                    patientAccountMobileUpdate(updateUserInfoReq, account);
                } else {
                    throw new ServiceException("修改手机号已存在");
                }
            } else {
                patientAccount.setMobile(updateUserInfoReq.getLoginMobile());
                patientAccountMapper.updatePatientAccount(patientAccount);
                log.info("更新用户手机号成功");
            }
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

        MpPerson mpPerson = mpPersonMapper.selectMpPersonByPersonIdCardId(updateUserInfoReq.getPersonId(), updateUserInfoReq.getIdCard());
        if (mpPerson == null) {
            throw new ServiceException("请核对就诊号信息或身份证号信息是否正确");
        }

        if (!patientAccount.getRegistrySigns().equals(updateUserInfoReq.getPersonId())) {
            PatientAccount account = new PatientAccount();
            account.setRegistrySigns(updateUserInfoReq.getPersonId());
            List<PatientAccount> patientAccounts = patientAccountMapper.selectPatientAccountList(patientAccount);
            if (ObjUtil.isNotEmpty(patientAccounts)) {
                throw new ServiceException("患者就诊号已存在,使用手机号" + patientAccounts.get(0).getMobile());
            }
            patientAccount.setRegistrySigns(updateUserInfoReq.getPersonId());
        }
        if (!patientAccount.getIdCard().equals(updateUserInfoReq.getIdCard())) {

            PatientAccount account = new PatientAccount();
            account.setIdCard(updateUserInfoReq.getIdCard());
            List<PatientAccount> patientAccounts = patientAccountMapper.selectPatientAccountList(account);
            if (ObjUtil.isNotEmpty(patientAccounts)) {
                throw new ServiceException("修改身份证号已存在,使用手机号" + patientAccounts.get(0).getMobile());
            }
            patientAccount.setIdCard(updateUserInfoReq.getIdCard());
        }
        patientAccountMapper.updatePatientAccount(patientAccount);
        MobileForm mobileForm = new MobileForm();
        mobileForm.setMobile(updateUserInfoReq.getMobile());
        mobileForm.setPersonId(updateUserInfoReq.getPersonId());
        medicalPatientService.updateMobile(mobileForm);
        result.setData(true);
        result.setMsg("更新用户信息成功");
        return result;
    }

    private void patientAccountMobileUpdate(UpdateUserInfoReq updateUserInfoReq, PatientAccount account) {
        patientAccountMapper.deletePatientAccountByAccountId(account.getAccountId());
        PatientAccount account1 = new PatientAccount();
        account1.setMobile(updateUserInfoReq.getLoginMobile());
        account1.setRegistrySigns(updateUserInfoReq.getPersonId());
        account1.setStatus("0");
        account1.setBindStatus("1");
        account1.setCreateTime(DateUtils.getNowDate());
        account1.setIdCard(updateUserInfoReq.getIdCard());
        patientAccountMapper.insertPatientAccount(account1);
        log.info("更新用户手机号成功");
    }

    public Boolean isJoinQueue(Long accountId, String channel) {
        // 查询patientInfo表
        PatientAccount patientAccount = patientAccountMapper.selectPatientAccountByAccountId(accountId);
        if (ObjUtil.isNull(patientAccount)) {
            throw new ServiceException("用户不存在");
        }

        // 查看patientInfo表
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setPersonId(patientAccount.getRegistrySigns());
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfo);
        return ObjUtil.isEmpty(patientInfos);
    }
}
