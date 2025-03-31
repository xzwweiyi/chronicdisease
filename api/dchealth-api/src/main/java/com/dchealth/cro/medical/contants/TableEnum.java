package com.dchealth.cro.medical.contants;

public enum TableEnum {
    MP_CT_EXAM("mp_ct_exam", "CT"),
    MP_ULTRASONIC_EXAM("mp_ultrasonic_exam", "超声"),
    MP_NUCLEAR_MAGNETISM_EXAM("mp_nuclear_magnetism_exam", "磁共振"),
    MP_X_RAY_EXAM("mp_x_ray_exam", "X片"),
    MP_ELECTROCARDIOGRAM_EXAM("mp_electrocardiogram_exam", "心电图"),

//    =====================检验=======================
    MP_BLOOD_ROUTINE_COL("mp_blood_routine_col", "血常规"),
    MP_URINE_ROUTINE_COL("mp_urine_routine_col", "尿常规"),
    MP_BLOOD_BIOCHEMISTRY_COL("mp_blood_biochemistry_col", "血气"),
    MP_COAGULATION_FUNCTION_COL2("mp_coagulation_function_col2", "凝血"),
    MP_GLUCOSE_TOLERANCE_COL("mp_glucose_tolerance_col", "血糖"),
    MP_STOOL_ROUTINE_COL("mp_stool_routine_col", "便常规"),
    MP_THYROID_FUNCTION_COL("mp_thyroid_function_col", "甲功,传染,血脂"),
    MP_TUMOR_SCREENING_ITEMS_COL("mp_tumor_screening_items_col", "肿瘤"),
    ;

    private final String tableName;
    private final String type;

    TableEnum(String tableName, String type) {
        this.tableName = tableName;
        this.type = type;
    }
    public String getTableName() {
        return tableName;
    }
    public String getType() {
        return type;
    }
}
