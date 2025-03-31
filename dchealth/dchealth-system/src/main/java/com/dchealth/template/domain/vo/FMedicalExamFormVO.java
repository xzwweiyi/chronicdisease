package com.dchealth.template.domain.vo;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.template.domain.FMedicalExamForm;
import lombok.Data;

/**
 * @Author lzx
 * @Date 2023/4/21
 * @Description:
 */
@Data
public class FMedicalExamFormVO {

    private FMedicalExamForm fMedicalExamForm;

    private PatientInfo patientInfo;
}
