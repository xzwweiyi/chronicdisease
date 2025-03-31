package com.dchealth.cro.medical.vo;

import com.dchealth.cro.medical.domain.PatientRecord;
import lombok.Data;

import java.util.List;

/**
 * 患者详情
 */
@Data
public class PatientRecordListVo {

    private List<PatientRecord> list;
    /** 患者ID */
    private Long patientId;
    /**
     * 就诊ID
     */
    private String personId;

    /** 患者姓名 */
    private String patientName;
    /** 队列名称 */
    private String researchName;


}
