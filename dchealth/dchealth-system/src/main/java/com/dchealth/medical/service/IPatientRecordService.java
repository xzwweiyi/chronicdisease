package com.dchealth.medical.service;

import java.util.List;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.form.PatientRecordDetailForm;
import com.dchealth.medical.domain.vo.FollowMedicalFromTree;

import javax.servlet.http.HttpServletResponse;

/**
 * 患者档案Service接口
 * 
 * @author lchbu
 * @date 2022-09-15
 */
public interface IPatientRecordService 
{
    /**
     * 查询患者档案
     * 
     * @param recordId 患者档案主键
     * @return 患者档案
     */
    public PatientRecord selectPatientRecordByRecordId(Long recordId);

    /**
     * 查询患者档案列表
     * 
     * @param patientRecord 患者档案
     * @return 患者档案集合
     */
    public List<PatientRecord> selectPatientRecordList(PatientRecord patientRecord);

    /**
     * 新增患者档案
     * 
     * @param patientRecord 患者档案
     * @return 结果
     */
    public int insertPatientRecord(PatientRecord patientRecord);

    /**
     * 修改患者档案
     * 
     * @param patientRecord 患者档案
     * @return 结果
     */
    public int updatePatientRecord(PatientRecord patientRecord);

    /**
     * 批量删除患者档案
     * 
     * @param recordIds 需要删除的患者档案主键集合
     * @return 结果
     */
    public int deletePatientRecordByRecordIds(Long[] recordIds);

    /**
     * 删除患者档案信息
     * 
     * @param recordId 患者档案主键
     * @return 结果
     */
    public int deletePatientRecordByRecordId(Long recordId);

    /**
     * 更新edc状态（edc状态，1待提交，2待质检，3质疑待回复，4已锁库）
     * @param recordId
     * @param edcStatus
     * @return
     */

    List<FollowMedicalFromTree> leftDetailTree(PatientRecord patientRecord);

    public  List<PatientRecordDetailForm> patientRecordDetail(List<PatientRecord> patientRecords);


    public String batchRecordByResearchId(Long researchId);

    void exportData(HttpServletResponse response, Long researchId, List<Long> patientId);

    void edcExportData(HttpServletResponse response, Long researchId, List<Long> patientId);


}
