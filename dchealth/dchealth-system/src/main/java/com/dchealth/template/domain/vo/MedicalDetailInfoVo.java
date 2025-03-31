package com.dchealth.template.domain.vo;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.template.domain.FMedicalForm;
import lombok.Data;

import java.util.List;

/**
 * 患者管理详情返回res
 */
@Data
public class MedicalDetailInfoVo {

    /**
     * 患者信息
     */
    private PatientInfo patientInfo;

    /**
     * 模板信息
     */
    private List<FMedicalForm> medicalInfos;

    /**
     * app模板信息
     */
    private List<FMedicalForm> appMedicalInfos;

}
