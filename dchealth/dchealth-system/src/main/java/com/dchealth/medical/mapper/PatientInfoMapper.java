package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.form.FollowWorkListForm;
import com.dchealth.medical.domain.vo.FollowWorkListVO;
import com.dchealth.medical.domain.vo.PatientEnrollmentVo;
import com.dchealth.medical.domain.vo.ResearchPatientVO;
import com.dchealth.statistics.domain.PatientDistribute;
import com.dchealth.statistics.domain.PatientDistributeVo;
import com.dchealth.statistics.domain.StatisticsFollowRateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 患者信息Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-21
 */
public interface PatientInfoMapper 
{
    /**
     * 查询患者信息
     * 
     * @param patientId 患者信息主键
     * @return 患者信息
     */
    public PatientInfo selectPatientInfoByPatientId(Long patientId);

    public List<PatientInfo> selectPatientInfoByPatientIdList(@Param("patientIdList") List<Long> patientIdList);
    /**
     * 查询患者信息
     *
     * @param oldPatientId
     * @return 患者信息
     */

    public PatientInfo selectPatientInfoByOldId(Long oldPatientId);

    /**
     * 档案ID获取患者信息
     * @param recordId
     * @return
     */
    public PatientInfo selectPatientInfoByRecordId(Long recordId);

    /**
     * 查询患者信息列表
     * 
     * @param patientInfo 患者信息
     * @return 患者信息集合
     */
    public List<PatientInfo> selectPatientInfoList(PatientInfo patientInfo);

    public List<PatientInfo> selectPatientInfoListByAuth(PatientInfo patientInfo);

    public List<PatientInfo> selectPatientInfoListLess(PatientInfo patientInfo);

    public List<PatientInfo> selectPatientInfoListLess2(PatientInfo patientInfo);

    /**
     * 入组列表数据
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> enrollmentList(FollowWorkListForm followWorkListForm);

    public List<FollowWorkListVO> enrollmentList2(FollowWorkListForm followWorkListForm);

    public List<FollowWorkListVO> enrollmentListNew(FollowWorkListForm followWorkListForm);

    /**
     * 待分配
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> allocatedList(FollowWorkListForm followWorkListForm);

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
     * 删除患者信息
     * 
     * @param patientId 患者信息主键
     * @return 结果
     */
    public int deletePatientInfoByPatientId(Long patientId);

    /**
     * 批量删除患者信息
     * 
     * @param patientIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientInfoByPatientIds(Long[] patientIds);

    /**
     * 批量插入患者信息
     * @param list
     * @return
     */
    int saveBatachPatientInfo(List<PatientInfo> list);

    /**
     * 查询患者详细信息
     * @param patientId
     * @return
     */
    PatientInfo selectPatientDetailInfoByPatientId(Long patientId);

    /**
     * 根据患者姓名查询患者信息
     * @param patientName
     * @return
     */
    List<PatientInfo> selectPatientInfoByPatientName(String patientName);

    List<PatientInfo> selectPatientInfoByPersonId(@Param("personId")String personId);

    List<PatientEnrollmentVo> selectEnrollmentPatientInfoList(PatientInfo patientInfo);

    List<PatientEnrollmentVo> selectExcelEnrollmentPatientInfoList(PatientInfo patientInfo);

    List<PatientEnrollmentVo> selectEnrollmentPatientInfoListLess(PatientInfo patientInfo);


    /**
     * 查询BaseIds
     * @param patientInfo
     * @return
     */
    List<String> queryPatientInfoBaseIds(PatientInfo patientInfo);

    List<PatientDistributeVo> queryPatientDistribute(PatientDistribute patientDistribute);

    List<PatientDistributeVo> queryPatientDistributeNew(PatientDistribute patientDistribute);

    List<ResearchPatientVO> selectResearchPatientInfoList(PatientInfo patientInfo);

    List<PatientInfo> selectPatientInfoList2(PatientInfo patientQuery);

    /**
     * 查询申请退出审核列表
     * @return
     */
    List<FollowWorkListVO> findAuditList();

    /**
     * 更新患者手机
     * @param personId 患者id
     * @param mobile 手机号
     * @return 结果
     */
    Integer updatePatientMobile(@Param("personId") String personId, @Param("mobile") String mobile);

    /**
     * 查询指定时间内入组患者信息
     * @param statisticsParam
     * @return
     */
    List<StatisticsFollowRateVo> selectFollowEnrollmentList(StatisticsFollowRateVo statisticsParam);
}
