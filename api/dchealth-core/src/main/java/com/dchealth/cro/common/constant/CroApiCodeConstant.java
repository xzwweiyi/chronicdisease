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
	SYSTEM_SMS_ERROR(1007,"短信发送失败"),
	SMS_TOO_MANY_ERROR(1008,"发送超过限制"),
	SMS_MANY_ERROR(1009,"短时间内不能再次发送"),
	OVER_VERSION_ERROR(1010,"APP新版本已发布，请更新"),
	OVER_TIME(1011,"无效请求"),
	IMAGE_NOT_EXIST(1012, "图片不能为空"),
	ARTICLE_NOT_EXIST(1013, "资讯不存在或已删除"),
	ARTICLE_NOT_ALLOWCOMMENT(1014, "资讯不支持评论"),
	AUTH_NOT_ALLOW(1015, "权限不足"),
	WX_OPEN_ID_ERROR(1016, "获取OpenId失败"),
	DOCTOR_ERROR(1017, "当前为医生端APP，请使用患者端APP"),
	PATINET_ERROR(1018, "当前为患者端APP，请使用医生端APP"),
	LLLEGAL_CONTENT(1019,"内容不合法"),
	COMMENT_NOT_EXIST(1020, "评论不存在"),
	APPLY_SUCCESS(1021, "申请成功！"),
	SEND_MESSAGE_ERROR(1022, "请绑定就诊卡号"),

	/**
	 * 用户
	 */
	USER_ERROR(2000, "账号或密码有误"),
	USER_NOT_LOGIN(2001, "用户未登录"),
	VERFIY_CODE_ERROR(2002, "验证码有误"),
	USER_EXIST(2003, "用户名已被注册"),
	OLD_PASSWORD_ERROR(2004,"旧密码有误"),
    LOGIN_FAIL(2005,"登录失败"),
    USER_NOT_EXIST(2006,"账号未注册"),
    USER_TO_INSUFFICIENT(2007,"用户余额不足"),
    VERFIY_CODE_CAN_NOT_USER(2008,"验证码失效或不存在"),
    AUTH_ERROR(2009,"未授权的访问"),
    ACCOUNT_PASS_WRONG(2010,"账号密码有误"),
    BLOCK_UP_ERROR(2011,"账号已停用"),
    PHONE_ALREADY(2012,"手机号已被使用"),
    MAILBOX_EXIST(2013,"邮箱已被使用"),
    PHONE_NOT_EXIST(2015,"账号未注册"),
    MAIL_FAIL(2016,"邮箱发送失败"),
    ACCOUNT_AUTH_EXIST(2017,"该账号已授权"),
    AUTH_EXIST(2018,"授权信息已被占用"),
    PHONE_ERROR(2019,"手机号格式不正确"),
    CARD_EXIST(2021,"银行卡已存在"),
    TO_REAL_NAME(2022,"请先实名后再提现！"),
    WAIT_REAL_NAME(2023,"您当前实名审核失败，请重新提交"),
    NO_BALANCE(2024,"您还没有收益！"),
    TOO_LOW_CASH(2025,"提现金额不得低于50元"),
    PAY_PASSWORD_ERROR(2026, "支付密码有误"),
    EMAIL_NOT_EXIST(2027,"邮箱不存在"),
    OLD_PAY_PASSWORD_ERROR(2028, "旧支付密码有误"),
    DOCTOR_NOT_EXIST(2029,"医生或专家不存在"),
    PROJECT_NOT_EXIST(2030,"项目不存在"),
    PATIENT_NOT_EXIST(2031,"患者不存在"),
    PATIENT_EXIST_DOCTOR(2032,"患者已被其它医生注册"),
    ARCHIVE_NOT_EXIST(2033,"患者档案不存在"),
    ARCHIVE_NOT_UPDATE(2034,"患者档案已完成不可更新"),
    PATINET_NOT_AUTH(2035,"无权限查看患者档案！"),
	MOBILE_EQ_DOCTOR(2036,"手机号与医生重复"),
	MOBILE_EQ_EXPERTS(2037,"手机号与专家重复"),
	UPDATE_PAYPASSWORD(2038,"初始支付密码不能进行支付，请更改支付密码"),
	REMOVE_ARCHIVE_CAN_NOT(2039,"请先删除最新档案记录"),
	QUESTION_NOT_EXIST(2040,"答疑不存在"),
	ARCHIVE_CAN_NOT_CREATE(2041,"患者已脱落无法再创建"),
	FILE_TYPE_EXIST(2042,"文档类型不存在"),
	ARCHIVE_EARLY_EXIT_NOT_FALL(2043,"已创建研究结束无法再创建"),
	ARCHIVE_FOLLOW_EXIT(2044,"已创建随访，无法创建出院前"),
	ARCHIVE_LEAVE_HOSPITAL_EXIT(2045,"已创建出院前，无法再次创建"),
	FINISH_EXIT(2045,"已提前终止，无法随访记录"),
	FINISH_EXIT2(2046,"提前终止已存在，无法重复创建"),
	FOLLOW_EXIST(2047,"随访已存在，无法重复创建"),
	SUBJECT_NOT_EXIST(2048,"问卷不能为空"),
	LEAVE_HOSPITAL_NOT_EXIST(2049,"患者未出院，不能创建随访"),
	STAGE_NOT_EXIST(2050,"阶段值不能为空"),
	CAN_NOT_EXIST(2051,"当前无法创建随访"),
	PATIENT_MOBILE_OVER(2052,"当前住院号已绑定手机号数量已满，无法绑定新手机号"),
	PATIENT_MOBILE_EXIST(2053,"当前手机号已绑定其它患者，无法绑定再次绑定"),
	HOSPITAL_INFO_ERROR(2054,"中心信息有误"),
	OPERATION_TIME_ERROR(2055,"手术时间为空，无法创建随访"),
	MONTH_ERROR(2056,"当前时间无法创建随访"),
	ALREADY_BINDING(2057,"已和当前用户绑定：请勿重复添加成员"),
	ALREADY_BINDING_OTHER(2058,"已和其他用户绑定：成员已被其他用户添加"),
	ALREADY_BINDING_OTHERS(2059,"患者的身份证与填写的身份证不一致：请确认患者的就诊ID与身份证一致"),
	BINDING_SUCCESS(2060,"绑定成功：已成功添加成员"),
	PATIENT_NOT_EXIST_PLEASE_FILL_CORRECT_ID_NUMBER_AND_REGISTRY_SIGNS(2061,"未查询到该患者信息，请填写正确的身份证号和就诊ID"),
	ACCOUNT_CANNOT_BE_EMPTY(2062,"账号不能为空"),
	THE_DEPARTMENT_WHERE_THE_ACCOUNT_IS_LOCATED_IS_EMPTY(2063,"账号所在部门为空"),
	ALREADY_BINDING_ERROR(2064,"账号已绑定过成员、请勿添加成员"),
	/**
	 * 患者端
	 */
	BINDING_FAIL(4000, "绑定患者失败"),
	PATIENT_RECORD_NOT_EXIT(4001, "患者档案周期不存在"),
	PATIENT_RECORD_DATA_NULL(4002, "请填写表单数据"),
	ONLY_ONE_SWITCH_IS_ALLOWED_FOR_THE_SAME_QUEUE(4003,"同一个队列只允许切换一次"),

	WARN_NOT_HANDLE(4004,"此档案存在未处理预警,不能发表随访意见"),

	PATIENT_ALREADY_BIND(4005,"此身份证,就诊ID已绑定"),
	PATIENT_ALREADY_EXIST(4006,"此身份证已存在"),

	/**
	 * 订单
	 */
	ORDER_SUCCESS(3000, "下单成功"),
	ORDER_FAIL(3001, "下单失败！"),
	ORDER_PERSISTENCE_EXCEPTION(3002, "订单保存异常"),
	ORDER_HANDLE_EXCEPTION(3003, "订单处理异常"),
	ORDER_TO_PAY_ERROR(3004, "订单支付失败"),
	ORDER_NOT_EXIST(3005, "订单不存在"),
	PAY_METHOD_ERROR(3006, "支付方式错误"),
	PAY_AREADLY_ERROR(3007, "订单已支付"),
	GOODS_NOT_EXIST(3008, "货品不存在"),
	GOODS_IS_SHELF(3009, "货品已下架"),
	GOODS_NOT_HAVE(3010, "货品库存不足"),
	PRODUCT_NOT_EXIST(3011, "商品不存在"),
	PRODUCT_IS_SHELF(3012, "商品已下架"),
	PRODUCT_NOT_HAVE(3013, "商品库存不足"),
	OVER_SCORE(3014, "积分不足"),
	GOODS_ADDRESS_NOT_EXIST(3015, "收货地址不存在"),
	WX_PAY_SING_ERROR(3016, "微信签名有误"),
	ALI_PAY_SING_ERROR(3017, "阿里签名有误"),


	WRONG_TYPE_OF_SUBMISSION(4000,"档案提交类型异常"),

	
	IP_NOT_ALLOW(1007,"IP地址无效"),

	/**
	 * 分页
	 */
	PAGING_PARAMETER_EXCEPTION(5000,"分页参数异常"),
	ALREADY_IN_GROUP (5001,"您已经入组");

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
