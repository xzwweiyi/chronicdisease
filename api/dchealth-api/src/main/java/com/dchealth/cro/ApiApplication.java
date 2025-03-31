package com.dchealth.cro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan("com.dchealth.cro")
@SpringBootApplication
@EnableTransactionManagement
public class ApiApplication {
	 private static final Logger log = LoggerFactory.getLogger(ApiApplication.class);
	public static void main(String[] args) {
		 ConfigurableApplicationContext context = SpringApplication.run(ApiApplication.class, args);
	        Environment env = context.getBean(Environment.class);
	        log.info("启动成功{}",env.getActiveProfiles());
	}

}



