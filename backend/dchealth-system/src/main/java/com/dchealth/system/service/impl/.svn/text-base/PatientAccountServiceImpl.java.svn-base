package com.dchealth.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.system.domain.PatientAccount;
import com.dchealth.system.mapper.PatientAccountMapper;
import com.dchealth.system.service.IPatientAccountService;

/**
 * 患者账号表Service业务层处理
 *
 * @author lchbu
 * @date 2022-09-14
 */
@Service
public class PatientAccountServiceImpl implements IPatientAccountService
{
    @Autowired
    private PatientAccountMapper patientAccountMapper;

    @Autowired
    private MpPersonMapper mpPersonMapper;

    /**
     * 查询患者账号表
     *
     * @param accountId 患者账号表主键
     * @return 患者账号表
     */
    @Override
    public PatientAccount selectPatientAccountByAccountId(Long accountId)
    {
        return patientAccountMapper.selectPatientAccountByAccountId(accountId);
    }

    /**
     * 查询患者账号表列表
     *
     * @param patientAccount 患者账号表
     * @return 患者账号表
     */
    @Override
    public List<PatientAccount> selectPatientAccountList(PatientAccount patientAccount)
    {
        return patientAccountMapper.selectPatientAccountList(patientAccount);
    }

    /**
     * 新增患者账号表
     *
     * @param patientAccount 患者账号表
     * @return 结果
     */
    @Override
    public int insertPatientAccount(PatientAccount patientAccount)
    {
        patientAccount.setCreateTime(DateUtils.getNowDate());
        return patientAccountMapper.insertPatientAccount(patientAccount);
    }

    /**
     * 修改患者账号表
     *
     * @param patientAccount 患者账号表
     * @return 结果
     */
    @Override
    public int updatePatientAccount(PatientAccount patientAccount)
    {
        //修改病案号  为什么能修改病案号？？？？
        if(null != patientAccount.getId()){
        	MpPerson  mpPerson = mpPersonMapper.selectPersonByAccountId(patientAccount.getId());
        	mpPerson.setPersonId(String.valueOf(patientAccount.getRegistrySigns()));
        	mpPersonMapper.updateMpPerson(mpPerson);
        }
        return patientAccountMapper.updatePatientAccount(patientAccount);
    }

    /**
     * 批量删除患者账号表
     *
     * @param accountIds 需要删除的患者账号表主键
     * @return 结果
     */
    @Override
    public int deletePatientAccountByAccountIds(Long[] accountIds)
    {
        return patientAccountMapper.deletePatientAccountByAccountIds(accountIds);
    }

    /**
     * 删除患者账号表信息
     *
     * @param accountId 患者账号表主键
     * @return 结果
     */
    @Override
    public int deletePatientAccountByAccountId(Long accountId)
    {
        return patientAccountMapper.deletePatientAccountByAccountId(accountId);
    }
}
