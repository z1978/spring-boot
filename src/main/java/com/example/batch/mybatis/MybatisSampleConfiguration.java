package com.example.batch.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.entity.User;
import com.example.listener.JobCompletionNotificationListener;

@Configuration
@EnableAutoConfiguration
public class MybatisSampleConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MybatisSampleConfiguration.class);
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired 
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    
    @Autowired
    private DataSource dataSource;

    @Bean
    @StepScope
	public MyBatisPagingItemReader<User> mybatisSampleReader() {
    	MyBatisPagingItemReader<User> mybatisSampleReader = new MyBatisPagingItemReader<User>();
    	try {
			mybatisSampleReader.setSqlSessionFactory(sqlSessionFactory);
			mybatisSampleReader.setQueryId("com.example.dao.UserMapper.selectByPrimaryKey");
			mybatisSampleReader.setParameterValues(getParameterValues());
			mybatisSampleReader.setPageSize(10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mybatisSampleReader;
	}

	private Map<String, Object> getParameterValues() throws Exception {
	    // PrimaryKey指定検索
	    List<Object> users = sqlSessionTemplate.selectList("com.example.dao.UserMapper.selectByPrimaryKey", 152);
	    Integer cnt = users.size();
	    log.info(cnt.toString());
	    Integer id = 151;
	    Map<String, Object> parameters = new HashMap<>();
		parameters.put("id", id);
//		parameters.put("createTime", new Date());
//		parameters.put("updateTime", new Date());
		return parameters;
	}
	
//    @Bean
//    @StepScope
//    public FlatFileItemReader<User> mybatisSampleReader() {
//        FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
//        reader.setResource(new ClassPathResource("sample-data2.csv"));
//
//        // こっちの書き方の方がわかりやすいかも
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setNames(new String[] { "firstName", "lastName", "age" });
//        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<User>();
//        fieldSetMapper.setTargetType(User.class);
//
//        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<User>();
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        reader.setLineMapper(lineMapper);
//
//        return reader;
//    }

    @Bean
    @StepScope
    public MybatisSampleProcessor mybatisSampleProcessor() {
        return new MybatisSampleProcessor();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<User> mybatisSampleWriter() {
        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
        writer.setSql("INSERT INTO user (first_name, last_name, age, create_time, update_time) VALUES (:firstName, :lastName, :age, :createTime, :updateTime)");
        writer.setDataSource(dataSource);
        return writer;
    }
    
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    @Bean
    public Step mybatisSampleStep() {
        return stepBuilderFactory.get("mybatisSampleStep")
                .<User, User> chunk(10)
                .reader(mybatisSampleReader())
                .processor(mybatisSampleProcessor())
                .writer(mybatisSampleWriter())
                .build();
    }


    @Bean
    public Job mybatisSampleJob() {
        return jobBuilderFactory.get("mybatisSampleJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(mybatisSampleStep())
                .end()
                .build();
    }
}
