package com.dchealth.cro.common.enums;

/**
 * @Author lzx
 * @Date 2023/3/30
 * @Description: 检验枚举类--健康档案
 */
public enum TdPatientTestEnum {

    //检验
    BLOOD_ROUTINE(1, "血常规"),
    URINE_ROUTINE(2, "尿常规"),
    STOOL_ROUTINE(3, "便常规"),
    MYOCARDIA_ENZYME(4, "心肌酶"),
    TUMOR_SCREENING(5, "肿瘤十二项"),
    THYROID_FUNCTION(6, "甲功能五项"),
    HORMONE_ITEMS(7, "激素六项"),
    BLOOD_BIOCHEMISTRY(8, "血液深化"),

    ;

    private final Integer code;
    private final String value;

    TdPatientTestEnum(Integer code, String value) {
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
