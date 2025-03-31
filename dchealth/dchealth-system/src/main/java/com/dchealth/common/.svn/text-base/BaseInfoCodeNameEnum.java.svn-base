package com.dchealth.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @Auther: cuitao
 * @Date: 2023/4/6 - 04 - 06 - 15:00
 * @Description: com.dchealth.cro.core
 */
public enum BaseInfoCodeNameEnum {

    NAME("registerInputwxuz", "姓名"),
    INFORMATION_COLLECTION_TIME("registerDatec0hc", "信息采集时间"),
    DATE_OF_ONSET("registerYEARMONTHDAY0enr", "发病日期"),
    SEX("registerSelect2ips","性别"),
    DATE_OF_BIRTH("registerYEARMONTHDAYdzxo", "出生日期或就诊年龄"),
    SUBMIT_DATE("registerYEARMONTHD1vfg8", "随访提交时间"),
    FOLLOW_NAME("registernameYEARMONTHD1vfg8", "随访名称"),
    REGISTER_CAPTION("registerCaption1vf8", ""),
    MOBILE1("registerInput9chp","手机号码-1"),
    MOBILE2("registerInput9xeq","手机号码-2"),
    LANDLINE_NUMBER("registerInputo8pg","座机号码"),
    PROVINCES_CITIES_COUNTIES("registerprovincecbsa","省市县"),
    ADDRESS("registerInputlj79","详细住址-街道"),
    NATION("registerSelecta8qf","民族"),
    ID_CARD("registerInputl8gu", "身份证号码"),
    CASE_NUMBER("registerInputy66q","病例编号"),
    REGISTER_CAPTIONTIT("registerCaptionTit1vf8",""),
    EDUCATIONAL_LEVEL("registerSelecthnd1","文化程度"),
    MARITAL_STATUS("registerSelectai3d","婚姻状况"),
    MEDICAL_INSURANCE("registerSelect9lo0","所享受的医保制度"),
    MEDICAL_INSURANCE_REMARK("registerInputm8rb","其他医保制度备注"),
    OCCUPATION("registerSelectoson","职业"),
    OTHER_OCCUPATION("registerInputauqd","其他职业"),
    EMPLOYMENT_SITUATION("registerSelectfvss","就业情况"),
    AVERAGE_MONTHLY_SPEND("registerSelectbmen","患者月均医疗费用支出"),
    ANNUAL_INCOME("registerSelectpvfm","家庭年收入（包括各种来源）"),
    ENROLLMENT_TIME("registerEnrollTimeabc", "入组时间");

    // 下面的暂时不用
   /* AGE("fieldInputkkod", "年龄"),
    PATIENT_IDENTIFICATION_NUMBER("fieldInputg548", "病案号/ID"),
    MOBILE("fieldInput6f2n", "联系方式");*/
    private String code;
    private String name;

    private BaseInfoCodeNameEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        for (BaseInfoCodeNameEnum baseInfoCodeNameEnum : BaseInfoCodeNameEnum.values()) {
            if (StringUtils.equals(baseInfoCodeNameEnum.getCode(), code)) {
                return baseInfoCodeNameEnum.name;
            }
        }
        return null;
    }
    public static BaseInfoCodeNameEnum getBaseInfoEnumByCode(String code) {
        for (BaseInfoCodeNameEnum baseInfoCodeNameEnum : BaseInfoCodeNameEnum.values()) {
            if (baseInfoCodeNameEnum.getCode().equals(code)) {
                return baseInfoCodeNameEnum;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
