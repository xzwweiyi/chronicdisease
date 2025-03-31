package com.dchealth.cro.core.tencent;

public enum TencentSmsTemplateEnum {
	REGISTER_CODE("1958921","您的注册验证码：{1}，如非本人操作，请忽略本短信！"),//注册密码
	FORGOT_PASSWORD("1172342","您的动态验证码为：{1}，您正在进行密码重置操作，如非本人操作，请忽略本短信！"),//忘记密码
	LOGIN_CODE("1958921","{1}为您的登录验证码，请于{2}分钟内填写，如非本人操作，请忽略本短信。"),//登录密码

	FOLLOW_TIP("1172308","您有一个随访提醒，请注意查收");

	public String templateId;

	public String msg;

	private TencentSmsTemplateEnum(String templateId, String msg) {
		this.templateId = templateId;
		this.msg = msg;
	}

	// 普通方法
	public static String getMsg(String templateId) {
		for (TencentSmsTemplateEnum c : TencentSmsTemplateEnum.values()) {
			if (c.getTemplateId().equals(templateId) ) {
				return c.msg;
			}
		}
		return null;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
