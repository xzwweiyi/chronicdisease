package com.dchealth.medical.contants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zjl
 * @since 2023/4/21
 */
public class SearchTypeConstants {

    /*
     * 纳排时间类型 默认、门诊时间、入院时间 、出院时间
     */
    public static String clinic="门诊时间";
    public static String admissionHospital="入院时间";
    public static String discharge="出院时间";

    public static String contain="包含";
    public static String notContain="不包含";
    public static String range="区间";
    public static String rangeOut="区间外";
    public static String equal="等于";
    public static String notEqual="不等于";
    public static String gte="大于等于";
    public static String lte="小于等于";
    public static String gt="大于";
    public static String lt="小于";

    public static List buliderRangeType = new ArrayList(){
        {
            add("区间");
            add("区间外");
            add("大于等于");
            add("小于等于");
            add("大于");
            add("小于");
        }
    };

}
