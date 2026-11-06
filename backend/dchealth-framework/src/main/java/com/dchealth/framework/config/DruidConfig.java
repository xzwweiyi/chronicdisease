package com.dchealth.framework.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import com.dchealth.common.enums.DataSourceType;
import com.dchealth.common.utils.spring.SpringUtils;
import com.dchealth.framework.config.properties.DruidProperties;
import com.dchealth.framework.datasource.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.servlet.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * druid 配置多数据源
 *
 * @author ruoyi
 */
@Slf4j
@Configuration
public class DruidConfig
{
    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        // Enforce utf8mb4 charset on every connection to prevent Chinese garbled characters
        dataSource.setConnectionInitSqls(java.util.Collections.singletonList("SET NAMES utf8mb4"));
        log.info("=== Master DataSource connectionInitSqls set: SET NAMES utf8mb4 ===");
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.hismysql")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.hismysql", name = "enabled", havingValue = "true")
    public DataSource hisMysqlDataSource(DruidProperties druidProperties) {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setConnectionInitSqls(java.util.Collections.singletonList("SET NAMES utf8mb4"));
        return druidProperties.dataSource(dataSource);
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.slave")
    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setConnectionInitSqls(java.util.Collections.singletonList("SET NAMES utf8mb4"));
        return druidProperties.dataSource(dataSource);
    }
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource dataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("hisMysqlDataSource") @Autowired(required = false) DataSource hisMysqlDataSource,
            @Qualifier("slaveDataSource") @Autowired(required = false) DataSource slaveDataSource) {

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);

        if (hisMysqlDataSource != null) {
            targetDataSources.put(DataSourceType.HISMYSQL.name(), hisMysqlDataSource);
            log.info("注册HIS数据源到动态数据源");
        }

        if (slaveDataSource != null) {
            targetDataSources.put(DataSourceType.SLAVE.name(), slaveDataSource);
            log.info("注册从数据源到动态数据源");
        }

        log.info("动态数据源初始化完成 | 默认数据源: {} | 可用数据源: {}",
                DataSourceType.MASTER.name(), targetDataSources.keySet());

        return new DynamicDataSource(masterDataSource, targetDataSources);
    }
    /**
     * 设置数据源
     *
     * @param targetDataSources 备选数据源集合
     * @param sourceName 数据源名称
     * @param beanName bean名称
     */
    public void setDataSource(Map<Object, Object> targetDataSources, String sourceName, String beanName)
    {
        try
        {
            DataSource dataSource = SpringUtils.getBean(beanName);
            targetDataSources.put(sourceName, dataSource);
        }
        catch (Exception e)
        {
        }
    }

    /**
     * 去除监控页面底部的广告
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    @ConditionalOnProperty(name = "spring.datasource.druid.statViewServlet.enabled", havingValue = "true")
    public FilterRegistrationBean removeDruidFilterRegistrationBean(DruidStatProperties properties)
    {
        // 获取web监控页面的参数
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        // 提取common.js的配置路径
        String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
        String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
        final String filePath = "support/http/resources/js/common.js";
        // 创建filter进行过滤
        Filter filter = new Filter()
        {
            @Override
            public void init(javax.servlet.FilterConfig filterConfig) throws ServletException
            {
            }
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException
            {
                chain.doFilter(request, response);
                // 重置缓冲区，响应头不会被重置
                response.resetBuffer();
                // 获取common.js
                String text = Utils.readFromResource(filePath);
                // 正则替换banner, 除去底部的广告信息
                text = text.replaceAll("<a.*?banner\"></a><br/>", "");
                text = text.replaceAll("powered.*?shrek.wang</a>", "");
                response.getWriter().write(text);
            }
            @Override
            public void destroy()
            {
            }
        };
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns(commonJsPattern);
        return registrationBean;
    }
}
