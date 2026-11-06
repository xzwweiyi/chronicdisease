package com.dchealth.etl.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import javax.sql.DataSource;

/**
 * @author caihe
 * @since 2023/3/16
 */
@Configuration
@MapperScan(basePackages = "com.dchealth.etl.dao.target",sqlSessionFactoryRef = "targetSqlSessionFactory")
public class DataSourceConfigDBTarget {
    // 将这个对象放入Spring容器中
    @Bean(name = "targetDataSource")
    // 表示这个数据源是默认数据源
    @Primary
    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.druid.dbtarget")
    public DataSource getDateSource()
    {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "targetSqlSessionFactory")
    // 表示这个数据源是默认数据源
    @Primary
    // @Qualifier表示查找Spring容器中名字为targetDataSource的对象
    public SqlSessionFactory targetSqlSessionFactory(@Qualifier("targetDataSource") DataSource datasource)
            throws Exception
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/target/*.xml"));
        return bean.getObject();
    }

    @Bean("targetSqlSessionTemplate")
    // 表示这个数据源是默认数据源
    @Primary
    public SqlSessionTemplate targetSqlSessionTemplate(
            @Qualifier("targetSqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }

}
