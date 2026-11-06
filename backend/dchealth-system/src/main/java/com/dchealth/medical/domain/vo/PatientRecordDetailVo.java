package com.dchealth.medical.domain.vo;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.warn.domain.WarnEarlyRecord;
import lombok.Data;

import java.util.List;

@Data
public class PatientRecordDetailVo {
    /**
     * 患者信息
     */
    private PatientInfo patientInfo;
    /**
     * 表单信息
     */
    private List<MedicalFormVO> medicalFormList;
    /**
     * 预警
     */
    private List<WarnEarlyRecord> warnEarlyRecordList;

}
