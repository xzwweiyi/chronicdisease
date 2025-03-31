package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.PatientEnrollmentCondition;
import com.dchealth.medical.domain.vo.PatientEnrollmentSearchVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PatientEnrollmentConditionMapper {
    int insertPatientEnrollmentCondition(PatientEnrollmentCondition patientEnrollmentCondition);

    PatientEnrollmentCondition selectPatientEnrollmentConditionById(Long id);
    PatientEnrollmentCondition selectPatientEnrollmentConditionByResearchId(Long id);
    List<PatientEnrollmentCondition> selectPatientEnrollmentConditionList(PatientEnrollmentCondition patientEnrollmentCondition);

    List<PatientEnrollmentSearchVo> selectDiagnosisDesc(@Param("diagnosisDesc") String diagnosisDesc);

    List<PatientEnrollmentSearchVo> selectPatientBaseInfo(@Param("patientParam") String patientParam);

    int updatePatientEnrollmentCondition(PatientEnrollmentCondition patientEnrollmentCondition1);
    int updatePatientEnrollmentConditionByResearchId(PatientEnrollmentCondition patientEnrollmentCondition1);

    /**
     * 按队列状态查询患者检索条件
     * @param researchStatus
     * @return
     */
    List<PatientEnrollmentCondition> queryPatientEnrollmentConditionList(String researchStatus);

}
