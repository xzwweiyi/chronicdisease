package com.dchealth.etl.config;

/**
 * @author caihe
 * @since 2023/3/16
 */
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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.dchealth.etl.dao.record",sqlSessionFactoryRef = "recordSqlSessionFactory")
public class DataSourceConfigDBRecord {

    // 将这个对象放入Spring容器中
    @Bean(name = "recordDataSource")
    // 表示这个数据源是默认数据源
    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.druid.dbrecord")
    public DataSource getDateSource()
    {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "recordSqlSessionFactory")
    // 表示这个数据源是默认数据源
    // @Qualifier表示查找Spring容器中名字为targetDataSource的对象
    public SqlSessionFactory recordSqlSessionFactory(@Qualifier("recordDataSource") DataSource datasource)
            throws Exception
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/record/*.xml"));
        return bean.getObject();
    }

    @Bean("recordSqlSessionTemplate")
    public SqlSessionTemplate recordSqlSessionTemplate(
            @Qualifier("recordSqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }

}
