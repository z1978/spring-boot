package com.example.zac;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
// @MapperScan注解(annotation)不写会找不到指定mapper，Mapped Statements collection does not contain value for com.example.dao.UserMapper.selectByPrimaryKey
@MapperScan("com.example.dao")
public class ZacDataSourceConfig {
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String userName;
	@Value("${spring.datasource.password}")
	private String passWord;

	/**
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean(destroyMethod = "close")
	public BasicDataSource basicDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(userName);
		dataSource.setPassword(passWord);
		return dataSource;
	}

	@Bean()
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(basicDataSource());
		return (SqlSessionFactory) bean.getObject();
	}

	@Bean
	//http://stackoverflow.com/questions/7621920/scopeprototype-bean-scope-not-creating-new-bean
	//http://outofmemory.cn/java/spring/spring-bean-scope
	@Scope("prototype")
	public SqlSessionTemplate sqlSessionFactoryTemplate() throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory(), ExecutorType.BATCH);
	}

	@Bean
	public DataSourceTransactionManager dataSourceTransactionManager() {
		return new DataSourceTransactionManager(basicDataSource());
	}
}
