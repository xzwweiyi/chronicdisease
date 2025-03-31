package com.dchealth.medical.service.impl;

import java.util.List;

import com.dchealth.medical.domain.MpPatientAccount;
import com.dchealth.medical.mapper.MpPatientAccountMapper;
import com.dchealth.medical.service.IMpPatientAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @date 2023-11-08
 */
@Service
public class MpPatientAccountServiceImpl implements IMpPatientAccountService{
    @Autowired
    private MpPatientAccountMapper mpPatientAccountMapper;

    /**
     * 查询【请填写功能名称】
     * 
     * @param personId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public MpPatientAccount selectMpPatientAccountByPersonId(String personId)
    {
        return mpPatientAccountMapper.selectMpPatientAccountByPersonId(personId);
    }

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param mpPatientAccount 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<MpPatientAccount> selectMpPatientAccountList(MpPatientAccount mpPatientAccount)
    {
        return mpPatientAccountMapper.selectMpPatientAccountList(mpPatientAccount);
    }

    /**
     * 新增【请填写功能名称】
     * 
     * @param mpPatientAccount 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertMpPatientAccount(MpPatientAccount mpPatientAccount)
    {
        return mpPatientAccountMapper.insertMpPatientAccount(mpPatientAccount);
    }

    /**
     * 修改【请填写功能名称】
     * 
     * @param mpPatientAccount 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateMpPatientAccount(MpPatientAccount mpPatientAccount)
    {
        return mpPatientAccountMapper.updateMpPatientAccount(mpPatientAccount);
    }

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param personIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMpPatientAccountByPersonIds(String[] personIds)
    {
        return mpPatientAccountMapper.deleteMpPatientAccountByPersonIds(personIds);
    }

    /**
     * 删除【请填写功能名称】信息
     * 
     * @param personId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteMpPatientAccountByPersonId(String personId)
    {
        return mpPatientAccountMapper.deleteMpPatientAccountByPersonId(personId);
    }
}
