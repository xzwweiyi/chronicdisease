package com.dchealth.cro.medical.contants;

import java.util.HashMap;
import java.util.Map;

public class MedicalContants {
	// 图片类型 
	public static final String IMAGE_TYPE_1 = "1";
	public static final String IMAGE_TYPE_2 = "2";
	public static final String IMAGE_TYPE_3 = "3";
	public static final String IMAGE_TYPE_4 = "4";
	public static final String IMAGE_TYPE_5 = "5";
	public static final String IMAGE_TYPE_6 = "6";
	public static final String IMAGE_TYPE_7 = "7";
	public static final String IMAGE_TYPE_8 = "8";
	public static final String IMAGE_TYPE_9 = "9";
	public static final String IMAGE_TYPE_10 = "10";
	public static final String IMAGE_TYPE_11 = "11";
	public static final String IMAGE_TYPE_12 = "12";
	
	public static final String PROGRESS_RATE = "档案已更新，等待审核中";
	public static final String DEFALUT_SHOW_MESSAGE = "请尽量填写完整准确的病例信息，以便专家更好的对病情做出判断";
	//模板类型 :10:鼓楼
	public static final String TEMPLATE_TYPE_1 = "1";
	public static final String TEMPLATE_TYPE_2 = "2";
	public static final String TEMPLATE_TYPE_3 = "3";
	public static final String TEMPLATE_TYPE_4 = "4";
	public static final String TEMPLATE_TYPE_5 = "5";
	public static final String TEMPLATE_TYPE_6 = "6";
	public static final String TEMPLATE_TYPE_7 = "7";
	public static final String TEMPLATE_TYPE_8 = "8";
	public static final String TEMPLATE_TYPE_9 = "9";
	public static final String TEMPLATE_TYPE_10 = "10";
	
	//1 可编辑  2 可查看  3无限权 
	public static final String PAT_AUTH_TYPE_1 = "1";
	public static final String PAT_AUTH_TYPE_2 = "2";
	public static final String PAT_AUTH_TYPE_3 = "3";
	
	//角色
	public static final String ROLE_TYPE_1 = "1";
	public static final String ROLE_TYPE_2 = "2";
	public static final String ROLE_TYPE_3 = "3";
	public static final String ROLE_TYPE_4 = "4";
	public static final String ROLE_TYPE_5 = "5";
	public static final String ROLE_TYPE_6 = "6";
	public static final String ROLE_TYPE_7 = "7";
	public static final String ROLE_TYPE_8 = "8";
	public static final String ROLE_TYPE_9 = "9";
	public static final String ROLE_TYPE_10 = "10";
	public static final String ROLE_TYPE_11 = "11";
	public static final String ROLE_TYPE_12 = "12";
	public static final String ROLE_TYPE_13 = "13";
	public static final String ROLE_TYPE_14 = "14";

	//审核是否通过
	public static final String AUDIT_STATUS_Y = "Y";
	public static final String AUDIT_STATUS_N = "N";

	// 类型1 默认所有医生 2 医生自己加入
	public static final String DOCTOR_ALL_TYPE = "1";
	public static final String DOCTOR_JOIN_TYPE = "2";
	public static final String CONS_STATUS_0 = "0";//0 待支付 
    public static final String CONS_STATUS_1 = "1";//
	public static final String CONS_STATUS_2 = "2";//
	public static final String CONS_STATUS_3 = "3";//
	public static final String CONS_STATUS_4 = "4";//
	public static final String CONS_STATUS_5 = "5";//
	public static final String CONS_STATUS_6 = "6";//
	public static final String CONS_STATUS_7 = "7";//
	public static final String CONS_STATUS_8 = "8";//
	public static final String CONS_STATUS_9 = "9";//
	public static final String CONS_STATUS_10 = "10";//
	public static final String CONS_STATUS_11 = "11";//
	public static final String CONS_STATUS_12= "12";//
	public static final String CONS_STATUS_13 = "13";//
	public static final String CONS_STATUS_14 = "14";//
	public static final String CONS_STATUS_15 = "15";//
	public static final String CONS_STATUS_16 = "16";//
	public static final String CONS_STATUS_17 = "17";//
	public static final String CONS_STATUS_18 = "18";//
	public static final String CONS_STATUS_19 = "19";//
	public static final String CONS_STATUS_20 = "20";//
	public static final String CONS_STATUS_100 = "100";//支付完成
	public static final String CONS_STATUS_101 = "101";//101 推送复诊信息
	//支付方式 0免支付 1 支付宝 2 微信
	public static final String PAY_TYPE_0 = "0";
	public static final String PAY_TYPE_1 = "1";
	public static final String PAY_TYPE_2 = "2";
	//支付状态 1未支付 2 已支付 3 支付失败  4 待退款 5 已退款 6免支付 7 已审核
	public static final String PAY_STATUS_1 = "1";
	public static final String PAY_STATUS_2 = "2";
	public static final String PAY_STATUS_3 = "3";
	public static final String PAY_STATUS_4 = "4";
	public static final String PAY_STATUS_5 = "5";
	public static final String PAY_STATUS_6 = "6";
	public static final String PAY_STATUS_7 = "7";
	public static final String PAY_STATUS_8 = "8";
	public static final Map<String,String>  RECORDMAP= new HashMap<String,String>(){
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
            put("1-1", "入院");
            put("2-1", "围术期（住院）");
            put("3-1", "第3个月随访");
            put("3-2", "第6个月随访");
            put("3-3", "第12个月随访");
            put("3-4", "第24个月随访");
            put("4-1", "提前终止");
            put("5", "凝血检查");
            put("6", "随访");
        }
        };
        
        public static final Map<Integer,String>  STAGEMAP= new HashMap<Integer,String>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
        		 put(1, "入院");
                 put(2, "围术期（住院）");
                 put(3, "访视期");
                 put(4, "提前终止");
                 put(5, "随访");
                 put(6, "随访");
        	} 
        };
        public static final Map<Integer,String>  TABLES_1= new HashMap<Integer,String>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
        		put(1, "pd");
        		put(2, "hopcs");
        		put(3, "ss");
        		put(4, "vls");
        		put(5, "nyha");
        	} 
        };
        public static final Map<Integer,String>  TABLES_2= new HashMap<Integer,String>(){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
        		put(1, "sti");
        		put(2, "pi");
        		put(3, "pc");
        		put(4, "lhi");
        		put(5, "afai");
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



	//站内信消息提醒类型
	public static final Map<String,String>  MESSAGE_TITLE= new HashMap<String,String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
			put("21", "患者拒绝入组");
		}
	};
        
    

      
}
