package com.dchealth.cro.common.enums;

/**
 * @Author lzx
 * @Date 2023/3/30
 * @Description: 体检报告检查项目
 */
public enum TdPatientCheckExamEnum {

    //检查
    GENERAL_EXAM(1, "一般检查"),
    INTERNAL_MEDICINE_EXAM(2, "内科检查"),
    SURGICAL_EXAM(3, "外科检查"),

    OTOLARYNGOLOGY(4, "耳鼻喉检查"),
    OPHTHALMOLOGICAL_EXAM(5, "眼科检查"),
    ORAL(6, "口腔科检查"),
    ECG(7, "心电图"),
    GYNECOLOGIC_EXAM(8, "妇科检查"),
    CONVENTIONAL_ENTEROSCOPE(9, "普通肠镜"),
    PAINLESS_GASTROSCOPE(10, "无痛胃镜"),
    PAINLESS_GASTROENTEROSCOPY(11, "无痛胃镜"),
    ;

    private final Integer code;
    private final String value;

    TdPatientCheckExamEnum(Integer code, String value) {
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
