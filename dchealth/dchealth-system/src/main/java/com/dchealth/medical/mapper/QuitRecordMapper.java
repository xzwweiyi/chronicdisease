package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.QuitRecord;
import com.dchealth.medical.domain.form.QuitRecordForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 退出队列mapper
 * @author zhangxiao
 */
public interface QuitRecordMapper {
    /**
     * 根据查询条件获取退出申请记录列表
     *
     * @param quitRecordForm 查询条件对象，包含查询所需的参数
     * @return 匹配查询条件的记录列表
     */
    List<QuitRecord> findList(@Param("form") QuitRecordForm quitRecordForm);

    /**
     * 更新退出申请记录
     * @param quitRecord 退出申请记录对象
     * @return 更新影响的行数
     */
    Integer updateQuitRecord(@Param("quitRecord") QuitRecord quitRecord);

    /**
     * 更新患者的退出状态
     * @param researchId 研究id
     * @param personId 患者id
     * @return 更新影响的行数
     */
    Integer updatePatientStatus(@Param("researchId") String researchId, @Param("personId") String personId);

    /**
     * 恢复患者的退出状态
     * @param researchId 研究id
     * @param personId 患者id
     * @return 更新影响的行数
     */
    Integer recoverPatientStatus(@Param("researchId") String researchId, @Param("personId") String personId);

    /**
     * 根据研究id和患者id查询患者队列记录
     * @param researchId 研究id
     * @param personId 患者id
     * @return
     */
    List<PatientRecord> findRecord(@Param("researchId")String researchId, @Param("personId")String personId);

    /**
     * 移除随访队列信息
     * @param record 参数
     */
    Integer removeRecordOutbound(@Param("record") PatientRecord record);

    /**
     * 删除当天生成的未填写的随访记录
     * @param record
     */
    Integer removeNowDayRecord(PatientRecord record);
}
