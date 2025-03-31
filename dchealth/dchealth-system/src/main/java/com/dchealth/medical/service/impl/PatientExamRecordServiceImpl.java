package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.medical.mapper.PatientExamRecordMapper;
import com.dchealth.medical.domain.PatientExamRecord;
import com.dchealth.medical.service.IPatientExamRecordService;

/**
 * 患者问卷填报关系Service业务层处理
 * 
 * @author lchbu
 * @date 2022-11-17
 */
@Service
public class PatientExamRecordServiceImpl implements IPatientExamRecordService{
    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;

    /**
     * 查询患者问卷填报关系
     * 
     * @param id 患者问卷填报关系主键
     * @return 患者问卷填报关系
     */
    @Override
    public PatientExamRecord selectPatientExamRecordById(Long id)
    {
        return patientExamRecordMapper.selectPatientExamRecordById(id);
    }

    /**
     * 查询患者问卷填报关系列表
     * 
     * @param patientExamRecord 患者问卷填报关系
     * @return 患者问卷填报关系
     */
    @Override
    public List<PatientExamRecord> selectPatientExamRecordList(PatientExamRecord patientExamRecord)
    {
        return patientExamRecordMapper.selectPatientExamRecordList(patientExamRecord);
    }

    /**
     * 新增患者问卷填报关系
     * 
     * @param patientExamRecord 患者问卷填报关系
     * @return 结果
     */
    @Override
    public int insertPatientExamRecord(PatientExamRecord patientExamRecord)
    {
        patientExamRecord.setCreateTime(DateUtils.getNowDate());
        return patientExamRecordMapper.insertPatientExamRecord(patientExamRecord);
    }

    /**
     * 修改患者问卷填报关系
     * 
     * @param patientExamRecord 患者问卷填报关系
     * @return 结果
     */
    @Override
    public int updatePatientExamRecord(PatientExamRecord patientExamRecord)
    {
        return patientExamRecordMapper.updatePatientExamRecord(patientExamRecord);
    }

    /**
     * 批量删除患者问卷填报关系
     * 
     * @param ids 需要删除的患者问卷填报关系主键
     * @return 结果
     */
    @Override
    public int deletePatientExamRecordByIds(Long[] ids)
    {
        return patientExamRecordMapper.deletePatientExamRecordByIds(ids);
    }

    /**
     * 删除患者问卷填报关系信息
     * 
     * @param id 患者问卷填报关系主键
     * @return 结果
     */
    @Override
    public int deletePatientExamRecordById(Long id)
    {
        return patientExamRecordMapper.deletePatientExamRecordById(id);
    }
}
