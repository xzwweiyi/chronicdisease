package com.dchealth.cro.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/9/5 12:32
 * @Version: 1.0
 */
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 针对哪些API路径
                        .allowedOrigins(
                                "https://test.dchealthclinical.com",
                                "https://td.dchealthclinical.com"
                        ) // 允许的来源
                        .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}