package com.dchealth.compatibility.constants;

import java.util.HashMap;
import java.util.Map;
@SuppressWarnings("serial")
public class HealthConstant {
	public static final String JK = "1";
	public static final String TJ = "2";
	
	public static final Map<String,String>  HEALTH_TYPE= new HashMap<String,String>(){
        {
            put("1", "健康档案");
            put("2", "体检报告");
           
        } 
        };
        public static final Map<String,String>  VISIT_TYPE= new HashMap<String,String>(){
        	{
        		put("1", "门诊");
        		put("2", "急诊");
        		put("3", "住院");
        		put("4", "体检");
        		put("99", "其他");
        		
        	} 
        };
        public static final Map<String,String>  MARITAL_STATUS= new HashMap<String,String>(){
        	{
        		put("10", "未婚");
        		put("20", "已婚");
        		put("21", "初婚");
        		put("22", "再婚");
        		put("23", "复婚");
        		put("30", "丧偶");
        		put("40", "离婚");
        		put("90", "未说明的婚姻状况");
        		
        	} 
        };
        public static final Map<String,String>  DEGREE_OF_EDUCATION= new HashMap<String,String>(){
        	{
        		put("10", "研究生教育");
        		put("20", "大学本科");
        		put("30", "大学专科");
        		put("40", "中等职业教育");
        		put("60", "普通高等中学教育");
        		put("71", "初中毕业");
        		put("80", "小学教育");
        		put("90", "其他");
        		
        	} 
        };
}
