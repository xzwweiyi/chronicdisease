package com.dchealth.cro.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * 公钥
 */
@Component
@ConfigurationProperties(prefix = "key")
@Data
public class KeyConfig {
	public  String publicKey;
	public  String privateKey;

}
