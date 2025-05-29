package com.dchealth.medical.contants;

import java.util.HashMap;
import java.util.Map;

public class MedicalContants {



	
	
	

	private  static final Map<Integer,String>  RECORDMAPUPPERCASE= initRecordCase();
	
	private static final Map<String,String>  RECORDMAPKV= initRecordMapKV();
	

	public static Map<String, String> getRecordmapkv() {
		return RECORDMAPKV;
	}

	private static Map<String,String> initRecordMapKV(){
		Map<String,String>  maps = new HashMap<String,String>();
		maps.put("1", "基线病例");
		maps.put("2", "治疗随访");
		maps.put("3", "AE");
		maps.put("4", "SAE");
		maps.put("6", "提前终止");
	
		return maps;
	}
	
	public static Map<Integer, String> getRecordmapuppercase() {
		return RECORDMAPUPPERCASE;
	}



	private static Map<Integer,String> initRecordCase(){
		Map<Integer,String>  maps = new HashMap<Integer,String>();
		maps.put(1, "第一");
		maps.put(2, "第二");
		maps.put(3, "第三");
		maps.put(4, "第四");
		maps.put(5, "第五");
		maps.put(6, "第六");
		maps.put(7, "第七");
		maps.put(8, "第八");
		maps.put(9, "第九");
		maps.put(10, "第十");
		maps.put(11, "第十一");
		maps.put(12, "第十二");
		maps.put(13, "第十三");
		maps.put(14, "第十四");
		maps.put(15, "第十五");
		maps.put(16, "第十六");
		maps.put(17, "第十七");
		maps.put(18, "第十八");
		maps.put(19, "第十九");
		maps.put(20, "第二十");
		return maps;
	}


	public static final Map<String,String>  GET_HOUR_TYPE= new HashMap<String,String>(){
		{
			put("1", "07");
			put("2", "08");
			put("3", "09");
			put("4", "10");
			put("5", "11");
			put("6", "12");
			put("7", "13");
			put("8", "14");
			put("9", "15");
			put("10", "16");
			put("11", "17");
			put("12", "18");
			put("13", "19");
		}
	};

	//站内信消息提醒类型
	public static final Map<String,String>  MESSAGE_TITLE= new HashMap<String,String>(){
		{
			put("6", "随访提醒");
			put("7", "超窗提醒");
			put("8", "预警提醒");
			put("9", "随访意见提醒");
			put("10", "质疑提醒");
			put("11", "入组提醒");
			put("12", "质疑未处理");
			put("13", "预警未处理");
			put("14", "患者随访未提交");
			put("15", "满意度评价");
			put("16", "随访处理任务提醒");
			put("17", "超窗任务提醒");
			put("18", "预警处理任务提醒");
			put("19", "入组提醒");
			put("20", "取消入组");
		}
	};

	//短信模板
	public static final Map<String,String>  XUNFEI_MESSAGE_TEMPLATE= new HashMap<String,String>(){
		{
			put("1", "12383");//医生提醒（带入居民姓名）超窗模板1
			put("2", "10427");//提醒医生居民血压情况通知 超窗模板2 用这个
			put("3", "19786");//唐都医院随访短信提醒
			put("4", "16606");//唐都医院预警提醒
		}
	};



	/**
	 * 问卷状态：进行中
	 */
	public static final String EXAM_STATUS_1 = "1";

	/**
	 * 问卷状态：已完成
	 */
	public static final String EXAM_STATUS_2 = "2";
}
