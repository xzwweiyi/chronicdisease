package com.dchealth.medical.service;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.vo.*;

import java.util.List;

/**
 * 患者信息Service接口
 * 
 * @author ruoyi
 * @date 2022-03-21
 */
public interface IPatientInfoService 
{
    /**
     * 查询患者信息
     * 
     * @param patientId 患者信息主键
     * @return 患者信息
     */
    public PatientInfo selectPatientInfoByPatientId(Long patientId);

    /**
     * 查询患者信息列表
     * 
     * @param patientInfo 患者信息
     * @return 患者信息集合
     */
    public List<PatientInfo> selectPatientInfoList(PatientInfo patientInfo);

    public List<PatientInfo> selectPatientInfoListByAuth(PatientInfo patientInfo);

    /**
     * 新增患者信息
     * 
     * @param patientInfo 患者信息
     * @return 结果
     */
    public int insertPatientInfo(PatientInfo patientInfo);

    /**
     * 修改患者信息
     * 
     * @param patientInfo 患者信息
     * @return 结果
     */
    public int updatePatientInfo(PatientInfo patientInfo);

    /**
     * 批量删除患者信息
     * 
     * @param patientIds 需要删除的患者信息主键集合
     * @return 结果
     */
    public int deletePatientInfoByPatientIds(Long[] patientIds);

    /**
     * 删除患者信息信息
     * 
     * @param patientId 患者信息主键
     * @return 结果
     */
    public int deletePatientInfoByPatientId(Long patientId);

    List<DiseasesResearchTree> selectPatientInfoTreeList(PatientInfo patientInfo);

    List<PatientEnrollmentVo> selectEnrollmentPatientInfoList(PatientInfo patientInfo);

    List<PatientEnrollmentVo> selectNoEnrollmentPatientInfoList(PatientInfo patientInfo);

    List<PatientInfo> selectPatientInfoByPersonId(String personId);

    public AjaxResult confirmEnrollment(PatientInfo patientInfo);

    public int importEnrollment(List<MpPerson> patientBaseInfo,String userName);

    public int deleteEnrollment(Long[] patientId);

    /**
     * 队列列表查询
     *
     * @param researchId
     * @param patientId
     * @param patientName
     * @return
     */
    public List<ResearchPatientVO> selectResearchPatientList(Long researchId, String patientId, String patientName);

    boolean canDelete(Long userId, Long[] patientIds);

    boolean canDeleteById(Long userId, Long id);

    int moveEnrollment(MoveEnrollmentVo moveEnrollmentVo);


    List<EnrollmentListVo> enrollmentList(MoveEnrollmentVo moveEnrollmentVo);
}
