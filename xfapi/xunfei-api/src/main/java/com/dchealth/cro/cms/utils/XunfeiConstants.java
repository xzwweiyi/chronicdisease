package com.dchealth.cro.cms.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用常量信息
 * 
 * @author genemax
 */
public class XunfeiConstants {

	//查询电话模版列表
	public static final String  XUNFEI_SPEECH_LIST = "/speech-api/v1/pb/openapi/speech-list";

	//查询电话模板自定义变量
	public static final String  XUNFEI_SPEECH_VARIABLE = "/speech-api/v1/pb/openapi/speech-variable";

	//外呼开放接口（用电话模版）
	public static final String  XUNFEI_CALL_TASK = "/speech-api/v1/pb/openapi/call-task";

	//外呼开放接口（用短信）
	public static final String  XUNFEI_SMS_TASK = "/speech-api/v1/pb/openapi/sms-task";

	//查询批次汇总数据
	public static final String  XUNFEI_BATCH_LIST = "/report-api/v2/pb/openapi/batch-list";

	//查询电话记录详情
	public static final String  XUNFEI_RECORD_DETAIL = "/report-api/v2/pb/openapi/record-detail";


	//短信模板
	public static final Map<String,String> XUNFEI_MESSAGE_TEMPLATE= new HashMap<String,String>(){
		{
			put("1", "12383");//医生提醒（带入居民姓名）超窗模板1
			put("2", "10427");//提醒医生居民血压情况通知 超窗模板2 用这个
			put("3", "19786");//唐都医院随访短信提醒
			put("4", "16606");//唐都医院预警提醒
		}
	};
	public static final String XUNFEI_MESSAGE_LOGIN = "10446";//短信登录验证码
	public static final String XUNFEI_MESSAGE_NOTIFY = "12107";//短信通知
}
