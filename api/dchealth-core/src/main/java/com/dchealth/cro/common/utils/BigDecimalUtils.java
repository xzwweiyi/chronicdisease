package com.dchealth.cro.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalUtils {
	private static final BigDecimal BASE_NUM = new BigDecimal(100);
    /**
            * 将数据库值转为字符串
     * @param num 
     * @return num/100
     */
	public static String formatNumber(Integer num) {
		if(num == null) {
			return null;
		}
		BigDecimal numDecimal = new BigDecimal(num).divide(BASE_NUM);
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(numDecimal);
	}
	
	public static String formatNumber(	BigDecimal numDecimal) {
		if(numDecimal == null) {
			return null;
		}
		DecimalFormat df = new DecimalFormat("#");
		return df.format(numDecimal.setScale(0, BigDecimal.ROUND_HALF_UP));
	}
	/**
	 * 将数据X100 入库
	 * @param num
	 * @return
	 */
	public static Integer formatNumber(Double num) {
		if(num == null) {
			return null;
		}
		BigDecimal numDecimal = new BigDecimal(num).multiply(BASE_NUM);
		return numDecimal.intValue();
	}
	
	
	/**
	 * 将数据X100 入库
	 * @param num
	 * @return
	 */
	public static Integer formatNumber(String num) {
		if(!StringUtils.isNotNullOrEmpty(num)) {
			return null;
		}
		BigDecimal numDecimal = new BigDecimal(num).multiply(BASE_NUM);
		return numDecimal.intValue();
	}
	
	
}
