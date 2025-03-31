package com.dchealth.cro.common.datasoure;

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
@MapperScan(basePackages = "com.dchealth.cro.his.mapper",sqlSessionFactoryRef = "secondSqlSessionFactory")
public class DataSourceSecondConfig {

    // 将这个对象放入Spring容器中
    @Bean(name = "secondDataSource")
    // 表示这个数据源是默认数据源
    // 读取application.properties中的配置参数映射成为一个对象
    // prefix表示参数的前缀
    @ConfigurationProperties(prefix = "spring.datasource.druid.second")
    public DataSource getDateSource()
    {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }

    @Bean(name = "secondSqlSessionFactory")
    // 表示这个数据源是默认数据源
    // @Qualifier表示查找Spring容器中名字为targetDataSource的对象
    public SqlSessionFactory secondSqlSessionFactory(@Qualifier("secondDataSource") DataSource datasource)
            throws Exception
    {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(datasource);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/his/**/*Mapper.xml"));
        return bean.getObject();
    }

    @Bean("secondSqlSessionTemplate")
    public SqlSessionTemplate secondSqlSessionTemplate(
            @Qualifier("secondSqlSessionFactory") SqlSessionFactory sessionFactory)
    {
        return new SqlSessionTemplate(sessionFactory);
    }

}
