package com.dchealth.cro.medical.mapper;

import com.dchealth.cro.medical.domain.PatientRecord;
import com.dchealth.cro.medical.dto.PatientResearchDto;
import com.dchealth.cro.medical.form.FollowWorkListCmdForm;
import com.dchealth.cro.medical.form.FollowWorkListForm;
import com.dchealth.cro.medical.vo.FollowWorkListVO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PatientRecordMapper {

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
     * 随访工作站查询列表
     * @param followWorkListForm
     * @return
     */
    public List<FollowWorkListVO> selectFollowWorkPatientRecordListByPage(FollowWorkListForm followWorkListForm);
    public List<FollowWorkListVO> selectFollowWorkPatientRecordLis(FollowWorkListForm followWorkListForm);
    public List<FollowWorkListVO> selectFollowWorkPatientRecordsByPage(FollowWorkListForm followWorkListForm);
    public List<FollowWorkListVO> selectFollowWorkList(FollowWorkListForm followWorkListForm);

    public Integer selectFollowWorkListCount(FollowWorkListForm followWorkListForm);

    public List<FollowWorkListVO> selectFollowWorkPatientRecordList(FollowWorkListForm followWorkListForm);


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
    public int deletePatientRecord(FollowWorkListCmdForm followWorkListCmdForm);

    public int selectFollowWorkPatientRecordCount(FollowWorkListForm followWorkListForm);
    public int selectFollowWorkPatientRecordCounts(FollowWorkListForm followWorkListForm);

    public List<FollowWorkListVO> selectFollowWorkWarnpatientList(FollowWorkListForm followWorkListForm);

    public int selectFollowWorkWarnpatientCount(FollowWorkListForm followWorkListForm);

    public List<PatientRecord> selectPatientRecordFollowStatusGroupInfoByResearchId(Long id);
    public List<PatientRecord> selectPatientRecordOverTimeGroupInfoByResearchId(Long id);
    public List<PatientRecord> selectPatientRecordWarnGroupInfoByResearchId(Long id);


    PatientResearchDto selectPatientAndResearchNameByRecordId(Long id);


    public PatientRecord selectOldByPatientId(Long patientId);

}
