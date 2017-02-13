package com.zac.test;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.ExecutorType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.batch.test.StepRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.zac.Application;
import com.example.zac.ZacConfiguration;
import com.example.zac.ZacDataSourceConfig;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing(modular = true)
@MapperScan("com.example.dao")
//@PropertySource({ "classpath:beans.properties", "classpath:zac-env-pro.properties", "classpath:/conf/common/zac-common.properties" })
@PropertySource({ "classpath:beans.properties", "classpath:zac-env-local.properties", "classpath:/conf/common/zac-common.properties" })

// 不加载主程序的class,使用自己的设定
@ComponentScan(excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ZacDataSourceConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ZacConfiguration.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = Application.class)
        }, basePackages = { "com.example" })
public class ZacTestBatchConfiguration {
    private static final Logger log = LoggerFactory.getLogger(ZacTestBatchConfiguration.class);

    @Value("${zac.test.jdbc.driverClassName}")
    String driverClassName;
    @Value("${zac.test.jdbc.url}")
    String url;
    @Value("${zac.test.jdbc.username}")
    String userName;
    @Value("${zac.test.jdbc.password}")
    String passWord;

    @Bean
    @Autowired
    public StepRunner stepRunner(JobLauncher jobLauncher, JobRepository jobRepository) {
        StepRunner sr = new StepRunner(jobLauncher, jobRepository);
        return sr;
    }

    @Bean(destroyMethod = "close")
    public BasicDataSource testDataSource() {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passWord);
        return dataSource;
    }

    @Bean()
    public SqlSessionFactoryBean testSqlSessionFactoryBean() {

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(testDataSource());
        return bean;

    }

    @Bean
    public SqlSessionTemplate testSqlSessionFactoryTemplate() throws Exception {

        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(testSqlSessionFactoryBean().getObject(), ExecutorType.BATCH);
        return sqlSessionTemplate;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        return new DataSourceTransactionManager(testDataSource());
    }

    // Manual config Spring Batch
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository() {
        try {
            MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean();
            mapJobRepositoryFactoryBean.setTransactionManager(transactionManager());
            return mapJobRepositoryFactoryBean.getObject();
        } catch (Exception e) {
            log.error("Failed to init jobRepository!", e);
            return null;
        }
    }

    @Bean
    public JobLauncher jobLauncher() {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository());
        return simpleJobLauncher;
    }

    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry());
        return jobRegistryBeanPostProcessor;
    }
}
