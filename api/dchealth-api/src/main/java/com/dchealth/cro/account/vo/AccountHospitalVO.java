package com.dchealth.cro.account.vo;

import lombok.Data;

/**
 * 医生医院信息对象 account_hospital
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-06-24
 */
@Data
public class AccountHospitalVO {
	private Long selHospitalId;//选择医院ID
	private Long hospitalId;//医院ID
	private String hospitalName;//医院名称 
	private String hospitalCode;//医院名称 

}
