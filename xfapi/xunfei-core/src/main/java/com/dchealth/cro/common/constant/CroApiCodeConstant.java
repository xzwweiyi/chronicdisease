package com.dchealth.cro.common.constant;

public enum CroApiCodeConstant {
	/**
	 * 系统/操作
	 */
	SUCCESS(1000, "成功"),
	FAIL(1001, "操作失败"),
	SYSTEM_SIGN_ERROR(1002, "签名有误"),
	ERROR_PARAM(1003, "参数有误"),
	JSON_READ_ERROR(1004,"JSON格式数据解释失败"),
	SYSTEM_ERROR(1005,"服务器繁忙，请稍后重试"),
	ILLEGAL_REQUEST_PARAMETER_EXCEPTION(1006,"参数格式有误"),

	OVER_TIME(1011,"无效请求"),

	PARAMETER_ERROR(10001,"参数有误"),
	VALIDATION_HAS_EXPIRED(10002,"验证已失效"),
	MISSING_CONFIGURATION(10003,"机构缺失厂商来源配置，请联系管理员"),
	SIGN_ERROR(10004,"Sign错误"),
	RESIDENTS_FILTERED(20001,"居民被全部过滤"),
	PLAN_ID_NULL(20002,"planId对应的随访方案为空"),
	PLAN_ID_WRONG(20003,"planId对应的随访方案有误"),
	SPEECH_ID_NULL(20004,"speechId对应的电话模版为空"),
	SMS_ID_NULL(20005,"smsId对应的业务短信为空"),
	TIME_QUITE_LATE(20006,"任务时间+重拨间隔不能进入晚9早6的限制内"),
	EXECUTION_TIME_LESS_NOW(20007,"执行时间不能小于当前时间"),
	INTRODUCTION_TOO_LONG(20008,"自我介绍变量长度超额"),
	SENSITIVE_WORD_RESTRICTIONS(20009,"敏感词限制"),
	MISSING_CORRESPONGING_CONFIGURATION(20010,"该电话模版缺失对应平台配置"),
	EXCESSIVE_UNIT_SERVICE(20011,"单位服务量超额"),
	UNIT_SERVICE_OVERDUE(20012,"单位服务量超期"),
	PHONE_TEMPLATE_BECOMES_MISMATCHED(20013,"电话模版变量不匹配"),
	TEMPLATE_VARIABLE_LENGTH_EXCEEDED(20014,"电话模版变量长度超额"),
	SMS_VARIABLE_MISMATCH(20015,"短信变量不匹配"),
	MESSAGE_LENGTH_EXCESS(20016,"短信长度超额"),
	DATE_DOES_NOT_EXIST(30001,"数据不存在"),
	CONTACE_ADMINISTRATOR(99999,"联系管理员查询失败原因");


	/**
	 * 错误码
	 */
	public int code;

	/**
	 * 错误信息
	 */
	public String msg;

	private CroApiCodeConstant(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	// 普通方法
	public static String getMsg(int code) {
		for (CroApiCodeConstant c : CroApiCodeConstant.values()) {
			if (c.getCode() == code) {
				return c.msg;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
