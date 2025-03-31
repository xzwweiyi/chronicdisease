package com.dchealth.medical.contants;

public enum TencentSmsAdminTemplateEnum {
	JOIN_RESEARCH("2074282","尊敬的{1}，欢迎您加入唐都慢病管理平台：1.微信关注“唐都医院”公众号；2.进入“诊疗服务”选择“慢病管理”；3.输入患者信息完成绑定，即可享受专业的慢病管理服务。唐都医院为您的健康保驾护航！"),;

	public String templateId;

	public String msg;

	private TencentSmsAdminTemplateEnum(String templateId, String msg) {
		this.templateId = templateId;
		this.msg = msg;
	}

	// 普通方法
	public static String getMsg(String templateId) {
		for (TencentSmsAdminTemplateEnum c : TencentSmsAdminTemplateEnum.values()) {
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
