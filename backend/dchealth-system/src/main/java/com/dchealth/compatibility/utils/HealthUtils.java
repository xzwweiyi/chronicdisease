package com.dchealth.compatibility.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.dchealth.common.utils.DateUtils;


public class HealthUtils {
	 /**
     *  根据身份证号获取年龄
     * @param cardNo
     * @return
     */
    public static int IdNOToAge(String IdNO){
        int leh = IdNO.length();
        String dates="";
        if (leh == 18) {
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year=df.format(new Date());
            int u=Integer.parseInt(year)-Integer.parseInt(dates);
            return u;
        }else{
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }

    }
    
    
    /**
     *  根据身份证号判断用户性别
     * @param cardNo
     * @return
     */
    public static Integer getSex(String cardNo) {
        String sexStr = "0";
        if (cardNo.length() == 15) {
            sexStr = cardNo.substring(14, 15);
        } else if (cardNo.length() == 18) {
            sexStr = cardNo.substring(16,17);
        }
         int sexNo = Integer.parseInt(sexStr);
          return sexNo % 2 == 0 ? 2 : 1;
        }
    public static String getSexName(String cardNo) {
    	String sexStr = "0";
    	if (cardNo.length() == 15) {
    		sexStr = cardNo.substring(14, 15);
    	} else if (cardNo.length() == 18) {
    		sexStr = cardNo.substring(16,17);
    	}
    	int sexNo = Integer.parseInt(sexStr);
    	return sexNo % 2 == 0 ? "女" : "男";
    }
    
    
    /**
     * 获取出生日期
     *
     * @return 返回字符串类型
     */
    public  static  String getBirthFromIdCard(String idCard) {
        if (idCard.length() != 18 && idCard.length() != 15) {
            return "请输入正确的身份证号码";
        }
        if (idCard.length() == 18) {
            String year = idCard.substring(6).substring(0, 4);// 得到年份
            String month = idCard.substring(10).substring(0, 2);// 得到月份
            String day = idCard.substring(12).substring(0, 2);// 得到日
            return (year + "-" + month + "-" + day);
        } else if (idCard.length() == 15) {
            String year = "19" + idCard.substring(6, 8);// 年份
            String month = idCard.substring(8, 10);// 月份
            String day = idCard.substring(10, 12);// 得到日
            return (year + "-" + month + "-" + day);
        }
        return null;
    }
   public static int getAge(Date date2,String birthDate) {
	  Date date1= DateUtils.parseDate(birthDate);
	   
	   Calendar cal1 = Calendar.getInstance();
	    cal1.setTime(date1);
	    int year1 = cal1.get(Calendar.YEAR);

	    Calendar cal2 = Calendar.getInstance();
	    cal2.setTime(date2);
	    int year2 = cal2.get(Calendar.YEAR);

	    int yearDiff = year2 - year1;
	    return yearDiff;
   } 
    

}
