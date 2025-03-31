package com.dchealth.compatibility.constants;

/**
 * @Description: 检查枚举-健康档案
 */
public enum HealthPatientExamEnum {

    //检查
    ELECTROCARDIOGRAM(1, "心电图报告"),
    CHEST_RADIOGRAPH_REPORT(2, "胸片报告"),
    X_LINE(3, "X线"),
    CT_CHECK(4, "CT检查"),
    MRI(5, "MRI检查"),

    PET_CT_CHECK(6, "PET-CT检查"),
    AUX_EXAM_UL_CARDIAC_ULTRASOUND(7, "心脏超声"),
    AUX_EXAM_UL_ABDOMINAL_ULTRASOUND(8, "腹部超声"),
    MEAL_OR_FEMALE(9, "专科超声(男性，女性)"),
    AUX_EXAM_UL_THYROID_ULTRASOUND(10,"甲状腺超声"),
    AUX_EXAM_UL_CAROTID_ULTRASOUND(11,"颈动脉超声"),
    AUX_EXAM_UL_VASCULAR_DOPPLER_ULTRASOUND(12,"血管多普勒超声"),
    DIAGNOSI(12, "诊断")
    ;

    private final Integer code;
    private final String value;

    HealthPatientExamEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
