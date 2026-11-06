package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.service.IPatientRecordFilesService;

/**
 * 患者档案图片Service业务层处理
 * 
 * @author lchbu
 * @date 2022-09-15
 */
@Service
public class PatientRecordFilesServiceImpl implements IPatientRecordFilesService 
{
    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;

    /**
     * 查询患者档案图片
     * 
     * @param id 患者档案图片主键
     * @return 患者档案图片
     */
    @Override
    public PatientRecordFiles selectPatientRecordFilesById(Long id)
    {
        return patientRecordFilesMapper.selectPatientRecordFilesById(id);
    }

    /**
     * 查询患者档案图片列表
     * 
     * @param patientRecordFiles 患者档案图片
     * @return 患者档案图片
     */
    @Override
    public List<PatientRecordFiles> selectPatientRecordFilesList(PatientRecordFiles patientRecordFiles)
    {
        return patientRecordFilesMapper.selectPatientRecordFilesList(patientRecordFiles);
    }

    /**
     * 新增患者档案图片
     * 
     * @param patientRecordFiles 患者档案图片
     * @return 结果
     */
    @Override
    public int insertPatientRecordFiles(PatientRecordFiles patientRecordFiles)
    {
        patientRecordFiles.setCreateTime(DateUtils.getNowDate());
        return patientRecordFilesMapper.insertPatientRecordFiles(patientRecordFiles);
    }

    /**
     * 修改患者档案图片
     * 
     * @param patientRecordFiles 患者档案图片
     * @return 结果
     */
    @Override
    public int updatePatientRecordFiles(PatientRecordFiles patientRecordFiles)
    {
        return patientRecordFilesMapper.updatePatientRecordFiles(patientRecordFiles);
    }

    /**
     * 批量删除患者档案图片
     * 
     * @param ids 需要删除的患者档案图片主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordFilesByIds(Long[] ids)
    {
        return patientRecordFilesMapper.deletePatientRecordFilesByIds(ids);
    }

    /**
     * 删除患者档案图片信息
     * 
     * @param id 患者档案图片主键
     * @return 结果
     */
    @Override
    public int deletePatientRecordFilesById(Long id)
    {
        return patientRecordFilesMapper.deletePatientRecordFilesById(id);
    }
}
