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
    /** 全局驱动类名，用于判断是否需要执行 MySQL 专有的连接初始化语句 */
    @org.springframework.beans.factory.annotation.Value("${spring.datasource.driverClassName:}")
    private String driverClassName;

    /**
     * 连接初始化语句：
     * 1) MySQL 需要 SET NAMES utf8mb4 防乱码；
     * 2) KingbaseES 需要把 public 显式排在 sys_catalog 前面——否则 sys_user / sys_role / sys_config
     *    等与系统视图同名的业务表会被解析到 sys_catalog 下的系统视图，导致查询报“列不存在”；
     *    同时统一会话时区，避免数据库默认 UTC 导致时间少 8 小时。
     */
    private void applyCharsetIfMysql(DruidDataSource dataSource)
    {
        boolean isMysql = driverClassName != null && driverClassName.toLowerCase().contains("mysql");
        if (isMysql)
        {
            dataSource.setConnectionInitSqls(java.util.Collections.singletonList("SET NAMES utf8mb4"));
            log.info("=== MySQL driver detected, connectionInitSqls: SET NAMES utf8mb4 ===");
        }
        else
        {
            // 用 SELECT set_config() 而非 SET 语句：Druid 统计过滤器对
            // "SET x TO y" 语法解析报错（merge sql error 刷屏），SELECT 形式两者都能解析，
            // 且 set_config(...,false) 对当前会话持续生效，语义与 SET 完全等价
            dataSource.setConnectionInitSqls(java.util.Arrays.asList(
                    "SELECT set_config('search_path', 'public,sys_catalog', false)",
                    "SELECT set_config('timezone', 'Asia/Shanghai', false)"));
            log.info("=== KingbaseES driver detected, init search_path=public,sys_catalog & timezone=Asia/Shanghai ===");
        }
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource(DruidProperties druidProperties)
    {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        applyCharsetIfMysql(dataSource);
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
        applyCharsetIfMysql(dataSource);
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
