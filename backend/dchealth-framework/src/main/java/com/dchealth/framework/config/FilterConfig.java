package com.dchealth.framework.config;

import com.dchealth.common.filter.RepeatableFilter;
import com.dchealth.common.filter.XssFilter;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.framework.aspectj.ImageRequestWebFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Filter配置
 *
 * @author ruoyi
 */
@Configuration
public class FilterConfig
{
    @Value("${xss.excludes}")
    private String excludes;

    @Value("${xss.urlPatterns}")
    private String urlPatterns;


    @Value("${image.requestPath}")
    private String requestPath;
    @Value("${image.oldDomain}")
    private String oldDomain;
    @Value("${image.newDomain}")
    private String newDomain;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    @ConditionalOnProperty(value = "xss.enabled", havingValue = "true")
    public FilterRegistrationBean xssFilterRegistration()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(StringUtils.split(urlPatterns, ","));
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("excludes", excludes);
        registration.setInitParameters(initParameters);
        return registration;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FilterRegistrationBean someFilterRegistration()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.LOWEST_PRECEDENCE);
        return registration;
    }


    @Bean
    FilterRegistrationBean<ImageRequestWebFilter> imageRequestFilterRegistrationBean() {
        FilterRegistrationBean<ImageRequestWebFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new ImageRequestWebFilter(oldDomain,newDomain));
        String[] split = requestPath.split(",");
        List<String> urlPatterns = Arrays.asList(split);
        bean.setOrder(-1);
        bean.setUrlPatterns(urlPatterns);
        return bean;
    }
}
