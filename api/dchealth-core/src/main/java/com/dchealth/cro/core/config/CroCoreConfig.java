package com.dchealth.cro.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties()
@PropertySource("classpath:cro-core.yml")
@Data
public class CroCoreConfig {
	private  String testFastPath;
	private  String prodFastPath;
	private  String tencentSecretId;
	private  String tencentSecretKey;
	private  String userHead;
	private String ocrPath;


	
	
	
}
