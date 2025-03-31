package com.dchealth.cro.account.form;

import lombok.Data;

import java.util.List;

@Data
public class MenuRedDotVo {

	private String followRedDotVoNum;//随访红点0展示1不展示

	private String warnRedDotVoNum;//预警点0展示1不展示

	private String overRedDotVoNum;//超窗红点0展示1不展示

	private List<ConfirmEnrollmentVo> confirmEnrollmentVoList;//入组确认

}
