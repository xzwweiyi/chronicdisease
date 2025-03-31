package com.dchealth.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.concurrent.*;

/**
 * 工具类.
 * 
 */
public class CommunalUtils{
    private static final Logger logger = LoggerFactory.getLogger(CommunalUtils.class);

    /**
     * 根据身份证号获取年龄
     */
    public static int getAge(String idNumber){
        String birthYeat = idNumber.substring(6,10);
        int currentYeat = LocalDate.now().getYear();
        return currentYeat - Integer.parseInt(birthYeat);
    }

    public static String getBirthday(String idNumber){
        return idNumber.substring(6,10)+"-"+idNumber.substring(10,12)+"-"+idNumber.substring(12,14);
    }


    public static String getSex(String idNumber){
        int genderDigit = Integer.parseInt(idNumber.substring(16, 17));
        return  (genderDigit % 2 == 0) ? "2" : "1";
    }


}
