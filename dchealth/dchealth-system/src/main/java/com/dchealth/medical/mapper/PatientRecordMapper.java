package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.form.EvaluateForm;
import com.dchealth.medical.domain.form.FollowWorkListCmdForm;
import com.dchealth.medical.domain.form.FollowWorkListForm;
import com.dchealth.medical.domain.vo.EvaluateVO;
import com.dchealth.medical.domain.vo.FollowWorkListVO;
import com.dchealth.medical.domain.vo.PatientRecordVo;
import com.dchealth.statistics.domain.StatisticsFollowRateVo;
import com.dchealth.statistics.domain.StatisticsParam;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 患者档案Mapper接口
 * 
 * @author lchbu
 * @date 2022-09-15
 */
public interface PatientRecordMapper 
{
    /**
     * 查询患者档案
     * 
     * @param recordId 患者档案主键
     * @return 患者档案
     */
    public PatientRecord selectPatientRecordByRecordId(Long recordId);


    /**
     * 根据队列id查询进行中的数据
     * @param researchId
     * @return
     */
    public List<PatientRecord> selectPatientRecordByResearchReleaseIdAndDate(@Param("researchId")Long researchId, @Param("handleDate") String handleDate);

    public List<PatientRecord> selectPatientRecordByResearchReleaseIdAndDate2(@Param("researchId")Long researchId, @Param("handleDate") String handleDate);

    /**
     * 查询课题下已完成的患者记录
     * @param researchId
     * @return
     */
    public List<Long> selectCompletedPatientIdByResearchId(@Param("researchId")Long researchId);


    //批量生成根据队列id查询最新记录
    public List<PatientRecord> selectOnePatientMaxRecordByResearchId(Long researchId);
    //批量生成根据队列id查询最新记录
    public List<PatientRecord> testXxjobGeneratedManually(@Param("researchId")Long researchId,@Param("patientId") Long patientId);
    //批量生成根据队列id查询最新记录
    public PatientRecord followUpTasksAreGeneratedManually(@Param("researchId")Long researchId,@Param("patientId")Long patientId,@Param("followIds")List<Long> followIds);
    public List<PatientRecord> selectMaxRecordByResearchId(Long researchId);


    public List<PatientRecord> selectPatientRecordByFollowId(Long followId);
    public List<PatientRecord> selectOverTimePatientRecordByFollowId(Long followId);


    /**
     * 查询患者档案列表
     * 
     * @param patientRecord 患者档案
     * @return 患者档案集合
     */
    public List<PatientRecord> selectPatientRecordList(PatientRecord patientRecord);

    public List<PatientRecord> selectPatientRecordListNoBase(PatientRecord patientRecord);

    public List<PatientRecord> selectList(PatientRecord patientRecord);

    /**
     * 随访工作站查询列表
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> selectFollowWorkBasePatientRecordList(FollowWorkListForm followWorkListForm);
    public List<FollowWorkListVO> selectFollowWorkPatientRecordList(FollowWorkListForm followWorkListForm);

    public List<FollowWorkListVO> selectFollowWorkPatientRecordList2(FollowWorkListForm followWorkListForm);
    public List<FollowWorkListVO> selectFollowWorkPatientRecordListNew(FollowWorkListForm followWorkListForm);

    /**
     * 评价列表
     * @param evaluateForm
     * @return
     */
    public List<EvaluateVO> selectEvaluateList(EvaluateForm evaluateForm);

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
     * 更新超窗患者档案
     * @param patientRecord 患者档案
     * @return 结果
     */
    public int updateOverTimePatientRecord(PatientRecord patientRecord);

    /**
     * 执行超窗更新
     * @param overDay
     * @return
     */
    public int updatePatientRecordOverTimeStatus(String overDay);

    //查找指定时间切未填写的超窗记录
    public List<PatientRecord> selectListByOverTime(PatientRecord patientRecord);

    /**
     * 批量插入
     * @param list
     * @return
     */
    public int batchInsertPatientRecord(List<PatientRecord> list);

    /**
     * 删除患者档案
     * 
     * @param recordId 患者档案主键
     * @return 结果
     */
    public int deletePatientRecordByRecordId(Long recordId);

    /**
     * 批量删除患者档案
     * 
     * @param recordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordByRecordIds(Long[] recordIds);

    /**
     * 查询最新的随访/基线记录
     * @param researchId
     * @param patientId
     * @return
     */
    PatientRecord selectPatientRecordByResearchIdAndPatientId(@Param("researchId") Long researchId, @Param("patientId") Long patientId);
    PatientRecord selectPatientRecordByResearchIdAndPatientId2(@Param("researchId") Long researchId, @Param("patientId") Long patientId);

    List<PatientRecord> selectPatientRecordByPatientId(@Param("patientId") Long patientId);

    /**
     * edc待处理任务
     * @return
     */
    List<PatientRecord> selectPatientRecordTodo();

    /**
     * dec任务列表
     * @param vo
     * @return
     */
    List<PatientRecordVo>  selectEcdPatientRecordVoList(PatientRecordVo vo);

    /**
     * 更新edc状态
     * @param recordId
     * @param edcStatus
     * @return
     */
    int updateEdcStatus(@Param("recordId") Long recordId, @Param("edcStatus") String edcStatus);

    List<PatientRecord> selectPatientRecordsByResearchIdAndPatientId(@Param("researchId") Long researchId, @Param("patientId") Long patientId);


    public PatientRecord selectOldByPatientId(Long patientId);

    List<PatientRecord> selectPatientRecordListForStatistics(StatisticsParam statisticsParam);


    List<PatientRecord> selectPatientRecordListForStatistics2(StatisticsParam statisticsParam);

    List<PatientRecord> selectPatientRecordListForStatistics3(StatisticsParam statisticsParam);

    int deletePatientRecord(FollowWorkListCmdForm followWorkListCmdForm);

    /**
     * 更新随访时间
     * @param id recordId
     * @param date 随访时间
     */
    void updateVisitTimeByResearchId(@Param("id") Long id, @Param("visitTime") Date date);

    /**
     * 根据recordId查询患者档案 只查询已经填充的
     * @param recordIds 患者档案主键列表
     * @return 档案列表
     */
    List<PatientRecord> selectByRecordIds(@Param("recordIds") List<Long> recordIds);

    /**
     * 查询患者档案总数
     * @param statisticsParam 查询参数
     * @return  患者档案总数
     */
    Integer selectRecordCount(StatisticsParam statisticsParam);

    /**
     * 查询超窗患者档案总数
     * @param statisticsParam 查询参数
     * @return 超窗患者档案总数
     */
    Integer selectOverRecordCount(StatisticsParam statisticsParam);

    List<PatientRecord> selectPatientRecordByResearchId(Long id);

    /**
     * 查询已完成随访患者数
     * @param statisticsParam
     * @return
     */
    List<StatisticsFollowRateVo> selectFollowListBySuccess(StatisticsFollowRateVo statisticsParam);

    PatientRecord selectNoFollowupOnePatientRecordByResearchId(@Param("researchId")Long researchId,@Param("id") Long patientId);

    void updateManuallyFollowUpList();
}
