package com.dchealth.cro.common.datasoure;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
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

@Configuration
@MapperScan(basePackages = { "com.dchealth.cro.cms.mapper", "com.dchealth.cro.medical.mapper",
		"com.dchealth.cro.account.mapper", "com.dchealth.cro.base.mapper", "com.dchealth.cro.core.mapper",
		"com.dchealth.cro.research.mapper", "com.dchealth.cro.minprogram.mapper", "com.dchealth.cro.form.mapper",
		"com.dchealth.cro.warn.mapper", "com.dchealth.cro.question.mapper",
		"com.dchealth.cro.health.mapper", "com.dchealth.cro.researchPatient.mapper"}, sqlSessionFactoryRef = "primarySqlSessionFactory")
public class DataSourcePrimaryConfig {
	// 将这个对象放入Spring容器中
	@Bean(name = "primaryDataSource")
	// 表示这个数据源是默认数据源
	@Primary
	// 读取application.properties中的配置参数映射成为一个对象
	// prefix表示参数的前缀
	@ConfigurationProperties(prefix = "spring.datasource.druid.primary")
	public DataSource getDateSource() {
		return DataSourceBuilder.create().type(DruidDataSource.class).build();
	}

	@Bean(name = "primarySqlSessionFactory")
	// 表示这个数据源是默认数据源
	@Primary
	// @Qualifier表示查找Spring容器中名字为targetDataSource的对象
	public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource datasource)
			throws Exception {
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		bean.setDataSource(datasource);
		bean.setMapperLocations(
				// 设置mybatis的xml所在位置
				new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/dchealth/**/*Mapper.xml"));
		return bean.getObject();
	}

	@Bean("targetSqlSessionTemplate")
	// 表示这个数据源是默认数据源
	@Primary
	public SqlSessionTemplate targetSqlSessionTemplate(
			@Qualifier("primarySqlSessionFactory") SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}

}
