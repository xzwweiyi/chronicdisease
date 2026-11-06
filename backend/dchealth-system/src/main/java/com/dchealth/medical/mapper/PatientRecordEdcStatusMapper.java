package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientRecordEdcStatus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 随访edc模板状态Mapper接口
 * 
 * @date 2023-11-16
 */
public interface PatientRecordEdcStatusMapper{
    /**
     * 查询随访edc模板状态
     * 
     * @param id 随访edc模板状态主键
     * @return 随访edc模板状态
     */
    public PatientRecordEdcStatus selectPatientRecordEdcStatusById(Long id);

    /**
     * 查询随访edc模板状态列表
     * 
     * @param patientRecordEdcStatus 随访edc模板状态
     * @return 随访edc模板状态集合
     */
    public List<PatientRecordEdcStatus> selectPatientRecordEdcStatusList(PatientRecordEdcStatus patientRecordEdcStatus);

    /**
     * 新增随访edc模板状态
     * 
     * @param patientRecordEdcStatus 随访edc模板状态
     * @return 结果
     */
    public int insertPatientRecordEdcStatus(PatientRecordEdcStatus patientRecordEdcStatus);

    /**
     * 修改随访edc模板状态
     * 
     * @param patientRecordEdcStatus 随访edc模板状态
     * @return 结果
     */
    public int updatePatientRecordEdcStatus(PatientRecordEdcStatus patientRecordEdcStatus);

    /**
     * 删除随访edc模板状态
     * 
     * @param id 随访edc模板状态主键
     * @return 结果
     */
    public int deletePatientRecordEdcStatusById(Long id);

    /**
     * 批量删除随访edc模板状态
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientRecordEdcStatusByIds(Long[] ids);


    public PatientRecordEdcStatus selectByTwoId(@Param("recordId") Long recordId, @Param("medicalFormId")Long medicalFormId);


    public Integer selectMinStatusByRecordId(Long recordId);

}
