package com.dchealth.cro.medical.mapper;

import com.dchealth.cro.medical.domain.PatientRecordFiles;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PatientRecordFilesMapper {

    /**
     * 查询患者档案图片
     *
     * @param id 患者档案图片主键
     * @return 患者档案图片
     */
    public PatientRecordFiles selectPatientRecordFilesById(Long id);

    /**
     * 根据档案ID查询
     * @param recordId
     * @return
     */
    public List<PatientRecordFiles> selectPatientRecordFilesByRecordId(Long recordId);

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
     * 删除患者档案图片
     *
     * @param id 患者档案图片主键
     * @return 结果
     */
    public int deletePatientRecordFilesById(Long id);

    /**
     * 根据档案ID删除
     * @param recordId
     * @return
     */
    public int deletePatientRecordFilesByRecordId(Long recordId);

    /**
     * 批量删除患者档案图片
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordFilesByIds(Long[] ids);

    /**
     * 批量插入
     * @param list
     * @return
     */
    public int batchInsertPatientRecordFiles(List<PatientRecordFiles> list);

    /**
     * 根据题目id查询图片、文件
     * @param propertyId
     * @return
     */
    List<PatientRecordFiles> selectPatientRecordFilesByProId(Long propertyId);
    List<PatientRecordFiles> selectPatientRecordFiles(Long propertyId,Long examRecordId);

    /**
     * 根据题目id删除源文件
     * @param id
     */
    void deletePatientRecordFilesByProId(Long propertyId);

    void deletePatientRecordFiles(Long propertyId,Long examRecordId);

    void deletePatientRecordFilesByHealthId(Long healthId);
}
