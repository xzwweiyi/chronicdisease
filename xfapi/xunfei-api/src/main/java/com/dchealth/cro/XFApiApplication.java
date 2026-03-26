package com.dchealth.cro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
//@ComponentScan("com.dchealth.cro")
@MapperScan({"com.dchealth.cro.cms.mapper","com.dchealth.cro.medical.mapper","com.dchealth.cro.account.mapper","com.dchealth.cro.base.mapper","com.dchealth.cro.core.mapper","com.dchealth.cro.research.mapper","com.dchealth.cro.minprogram.mapper","com.dchealth.cro.form.mapper","com.dchealth.cro.warn.mapper","com.dchealth.cro.question.mapper"})
@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@EnableTransactionManagement
public class XFApiApplication {

	@ResponseBody
	@RequestMapping(value = "isok", method = RequestMethod.GET)
	public String sendJoinResearch() {
		return "ok";
	}
	public static void main(String[] args) {
		SpringApplication.run(XFApiApplication.class, args);
	}

}



