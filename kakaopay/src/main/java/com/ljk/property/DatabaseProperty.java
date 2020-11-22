package com.ljk.property;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@MapperScan(basePackages= {"com.ljk.pay.mapper"})
@EnableTransactionManagement
public class DatabaseProperty {

	@Autowired
	GlobalProperty globalProperty;
	
	@Bean
	@Primary
	public DataSource customDataSource() {
		return DataSourceBuilder.create()
								.url(globalProperty.getUrl())
								.driverClassName(globalProperty.getDriverClassName())
								.username(globalProperty.getUsername())
								.password(globalProperty.getPassword())
								.build();
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource customDataSource) throws Exception {
		final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(customDataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sessionFactory.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
		
		return sessionFactory.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
		final SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		
		return sqlSessionTemplate;
	}
	
}
