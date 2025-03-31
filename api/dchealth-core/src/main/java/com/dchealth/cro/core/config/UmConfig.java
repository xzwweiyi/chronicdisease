package com.dchealth.cro.core.config;

import lombok.Data;

@Data
public class UmConfig {
	private  String umAndroidAppkey;
	private  String umAndroidAppMasterSecret;
	private  String umIosAppkey;
	private  String umIosAppMasterSecret;
	private  String umAliasType;
}
