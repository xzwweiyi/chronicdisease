package com.dchealth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 * 
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableScheduling
public class DchealthEtlApplication
{
	 private static final Logger log = LoggerFactory.getLogger(DchealthEtlApplication.class);
    public static void main(String[] args)
    {
    	
        ConfigurableApplicationContext context = SpringApplication.run(DchealthEtlApplication.class, args);
        Environment env = context.getBean(Environment.class);
        log.info("启动成功");
        log.info("系统访问路径：http://localhost:"+env.getProperty("server.port"));
    }
}
