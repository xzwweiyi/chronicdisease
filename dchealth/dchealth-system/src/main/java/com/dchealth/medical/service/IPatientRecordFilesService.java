package com.dchealth.medical.service;

import java.util.List;
import com.dchealth.medical.domain.PatientRecordFiles;

/**
 * 患者档案图片Service接口
 * 
 * @author lchbu
 * @date 2022-09-15
 */
public interface IPatientRecordFilesService 
{
    /**
     * 查询患者档案图片
     * 
     * @param id 患者档案图片主键
     * @return 患者档案图片
     */
    public PatientRecordFiles selectPatientRecordFilesById(Long id);

    /**
     * 查询患者档案图片列表
     * 
     * @param patientRecordFiles 患者档案图片
     * @return 患者档案图片集合
     */
    public List<PatientRecordFiles> selectPatientRecordFilesList(PatientRecordFiles patientRecordFiles);

    /**
     * 新增患者档案图片
     * 
     * @param patientRecordFiles 患者档案图片
     * @return 结果
     */
    public int insertPatientRecordFiles(PatientRecordFiles patientRecordFiles);

    /**
     * 修改患者档案图片
     * 
     * @param patientRecordFiles 患者档案图片
     * @return 结果
     */
    public int updatePatientRecordFiles(PatientRecordFiles patientRecordFiles);

    /**
     * 批量删除患者档案图片
     * 
     * @param ids 需要删除的患者档案图片主键集合
     * @return 结果
     */
    public int deletePatientRecordFilesByIds(Long[] ids);

    /**
     * 删除患者档案图片信息
     * 
     * @param id 患者档案图片主键
     * @return 结果
     */
    public int deletePatientRecordFilesById(Long id);
}
