package com.dchealth.health.vo.req;


import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.template.domain.FMedicalForm;
import lombok.Data;

import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/4 14:48
 * @Version: 1.0
 */
@Data
public class RecordHealthResp {

    /**
     * 患者信息
     */
    private PatientInfo patientInfo;

    /**
     * 模板信息
     */
    private List<FMedicalForm> medicalInfos;


}
