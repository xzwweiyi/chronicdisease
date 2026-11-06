package com.dchealth.medical;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 获取所有的value值
     *
     * @return
     */
    public static List<String> getValues() {
        List<String> values = new ArrayList<>();
        TdPatientCheckExamEnum[] tdPatientCheckExamEnums = TdPatientCheckExamEnum.values();
        for (TdPatientCheckExamEnum tdPatientTestEnum : tdPatientCheckExamEnums) {
            values.add(tdPatientTestEnum.getValue());
        }
        return values;
    }

    /**
     * 通过value获取code
     *
     * @param value
     * @return
     */
    public static Integer getCodeByValue(String value) {

        Integer code=null;
        TdPatientCheckExamEnum[] values = values();
        for (TdPatientCheckExamEnum tdPatientTestEnum : values){
            if (value.equals(tdPatientTestEnum.getValue())){
                code=tdPatientTestEnum.getCode();
            }
        }
        return code;
    }
}
