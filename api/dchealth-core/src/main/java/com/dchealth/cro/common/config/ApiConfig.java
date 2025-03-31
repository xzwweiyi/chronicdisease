package com.dchealth.cro.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * 拦截器
 */
@Configuration
public class ApiConfig implements WebMvcConfigurer {
	 @Bean
	 SignHandlerInterceptor signHandlerInterceptor(){
	        return new SignHandlerInterceptor();
	    }

	/**
	 * 注册 拦截器
	 */

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(signHandlerInterceptor()).addPathPatterns("/**");
	}

//	@Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("*")
//                .maxAge(168000)
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
	
}
