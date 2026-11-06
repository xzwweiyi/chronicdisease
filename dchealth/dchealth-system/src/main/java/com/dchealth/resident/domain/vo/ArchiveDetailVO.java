package com.dchealth.resident.domain.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 档案详情页完整数据VO
 */
public class ArchiveDetailVO {
    private ArchivedPatientVO basicInfo;
    private List<Map<String, Object>> diagnosisList;
    private List<Map<String, Object>> medicationList;
    private List<Map<String, Object>> visitList;
    private Map<String, Object> healthIndex;
    private List<Map<String, Object>> followupList;
    private List<Map<String, Object>> gradingList;
    private Map<String, Object> contractInfo;
    private List<Map<String, Object>> interventionList;

    public ArchivedPatientVO getBasicInfo() { return basicInfo; }
    public void setBasicInfo(ArchivedPatientVO basicInfo) { this.basicInfo = basicInfo; }
    public List<Map<String, Object>> getDiagnosisList() { return diagnosisList; }
    public void setDiagnosisList(List<Map<String, Object>> diagnosisList) { this.diagnosisList = diagnosisList; }
    public List<Map<String, Object>> getMedicationList() { return medicationList; }
    public void setMedicationList(List<Map<String, Object>> medicationList) { this.medicationList = medicationList; }
    public List<Map<String, Object>> getVisitList() { return visitList; }
    public void setVisitList(List<Map<String, Object>> visitList) { this.visitList = visitList; }
    public Map<String, Object> getHealthIndex() { return healthIndex; }
    public void setHealthIndex(Map<String, Object> healthIndex) { this.healthIndex = healthIndex; }
    public List<Map<String, Object>> getFollowupList() { return followupList; }
    public void setFollowupList(List<Map<String, Object>> followupList) { this.followupList = followupList; }
    public List<Map<String, Object>> getGradingList() { return gradingList; }
    public void setGradingList(List<Map<String, Object>> gradingList) { this.gradingList = gradingList; }
    public Map<String, Object> getContractInfo() { return contractInfo; }
    public void setContractInfo(Map<String, Object> contractInfo) { this.contractInfo = contractInfo; }
    public List<Map<String, Object>> getInterventionList() { return interventionList; }
    public void setInterventionList(List<Map<String, Object>> interventionList) { this.interventionList = interventionList; }
}
