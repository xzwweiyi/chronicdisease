package com.dchealth.medical;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author lzx
 * @Date 2023/3/30
 * @Description: 体检报告检验项目
 */
public enum TdPatientCheckTestEnum {

    //检验
    BLOOD_ROUTINE(1, "血常规分析-五分类"),
    URINE_ROUTINE(2, "尿常规分析"),
    STOOL_ROUTINE(3, "粪便常规分析"),
    LIVER_FUNCTION(4, "肝功能十一项"),
    RENAL_FUNCTION(5, "肾功能五项"),
    HEPATITS_B_ITEMS_ELISA(6, "乙肝五项（ELISA法）"),
    HEPATITS_B_ITEMS(7, "乙肝五项（化学发光法）"),
    BLOOD_FAT_THREE(8, "血脂三项"),
    BLOOD_FAT_FOUR(9, "血脂四项"),
    BLOOD_FAT_SIX(10, "血脂六项"),

    COAGULATION_FOUR(11, "凝血四项"),
    COAGULATION(12, "凝血系列检查-1"),
    TUMOR_SCREENING_MEAL(13, "肿瘤标志物（门男）"),
    TUMOR_SCREENING_FEMAL(14, "肿瘤标志物（门女）"),
    TORCH(15, "Torch四项"),
    MYOCARDIA_ENZYME(16, "心肌酶谱测定"),
    TRACE_ELEMENT(17, "微量元素(铅铜锌钙铁镁)测定"),
    ;

    private final Integer code;
    private final String value;

    TdPatientCheckTestEnum(Integer code, String value) {
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
        TdPatientCheckTestEnum[] tdPatientTestEnums = TdPatientCheckTestEnum.values();
        for (TdPatientCheckTestEnum tdPatientTestEnum : tdPatientTestEnums) {
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
        TdPatientCheckTestEnum[] values = values();
        for (TdPatientCheckTestEnum tdPatientTestEnum : values){
            if (value.equals(tdPatientTestEnum.getValue())){
                code=tdPatientTestEnum.getCode();
            }
        }
        return code;
    }
}
