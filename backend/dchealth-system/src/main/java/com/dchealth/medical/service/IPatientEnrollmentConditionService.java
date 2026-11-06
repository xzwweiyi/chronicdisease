package com.dchealth.medical.service;

import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.medical.domain.PatientEnrollmentCondition;
import com.dchealth.medical.domain.PatientEnrollmentConditionTree;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IPatientEnrollmentConditionService {

    int insertPatientEnrollmentCondition(PatientEnrollmentCondition patientEnrollmentCondition) throws ServiceException;

    AjaxResult conditionSearch(PatientEnrollmentCondition patientEnrollmentCondition) throws IOException;

    PatientEnrollmentCondition conditionDetail(Long researchId);

    List<PatientEnrollmentConditionTree> searchTree();

    Map conditionSearchByES(PatientEnrollmentCondition pec,boolean searchTextFlag) throws IOException;

    Boolean clearIndexData(String indexName);

}
