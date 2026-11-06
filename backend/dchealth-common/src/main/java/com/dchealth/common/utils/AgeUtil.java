package com.dchealth.common.utils;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdcardUtil;

import java.util.Date;

/**
 * 年龄工具类
 */
public class AgeUtil extends DateUtil {

    /**
     * 从出生日期获取年龄
     *
     * @param birthday
     * @return
     */
    public static int getAgeYearByBirthday(Date birthday) {
        return DateUtil.ageOfNow(birthday);
    }

    public static int getAgeDayByBirthday(Date birthday) {
        return Integer.parseInt(String.valueOf(DateUtil.between(birthday, DateUtils.getNowDate(), DateUnit.DAY, true)));
    }

    public static int getAgeYearByIdCard(String idCard) throws IllegalArgumentException {
        return IdcardUtil.getAgeByIdCard(idCard);
    }

    public static int getAgeDayByIdCard(String idCard) throws IllegalArgumentException {
        return getAgeDayByBirthday(DateUtils.parseDate(IdcardUtil.getBirth(idCard)));
    }
}
