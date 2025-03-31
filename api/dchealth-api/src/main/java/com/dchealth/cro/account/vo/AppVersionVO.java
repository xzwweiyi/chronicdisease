package com.dchealth.cro.account.vo;

import java.io.Serializable;

import lombok.Data;
@Data
public class AppVersionVO  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String appVersion;//版本号
	private String downloadUrl;//下载路径
	
	private String status;//是否需要强更
	
    
    private Boolean isUpdate = false;//是否需要更新 
    
    private String remark;//更新日志
    
    private Integer versionCode;//版本编号

	
	
	

}
