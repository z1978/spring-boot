package com.example.batch.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.entity.User;
import com.example.listener.JobCompletionNotificationListener;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class TestConfiguration02 {
    private static final Logger log = LoggerFactory.getLogger(TestConfiguration02.class);

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean(name="testReader02")
    public FlatFileItemReader<User> reader() {
        FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
        reader.setResource(new ClassPathResource("sample-data2.csv"));

        // こっちの書き方の方がわかりやすいかも
//        reader2.setLineMapper(new DefaultLineMapper<User>() {{
//            setLineTokenizer(new DelimitedLineTokenizer(){{
//                setNames(new String[] { "firstName", "lastName" });
//            }});
//            setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
//                    setTargetType(User.class);
//            }});
//        }});

        // こっちの書き方の方がわかりやすいかも
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames(new String[] { "firstName", "lastName", "age" });
        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<User>();
        fieldSetMapper.setTargetType(User.class);

        DefaultLineMapper<User> lineMapper = new DefaultLineMapper<User>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);

        return reader;
    }
//    @Autowired
//    private TestMyBatisPagingItemReader reader;
    
    @Autowired
    @Qualifier("testItemProcessor")
    private ItemProcessor<User, User> processor;
    
    @Autowired
    private TestItemWriter writer;
    
//    @Bean
//    public JdbcBatchItemWriter<User> writer() {
//        JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
//        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
//        writer.setSql("INSERT INTO user (first_name, last_name, age, create_time, update_time) VALUES (:firstName, :lastName, :age, :createTime, :updateTime)");
//        writer.setDataSource(dataSource);
//        return writer;
//    }
    
    @Bean
    public JobExecutionListener listener() {
        return new JobCompletionNotificationListener();
    }

    @Bean(name="testStep02")
    public Step step() {
        return stepBuilderFactory.get("testStep02")
                .<User, User> chunk(10)
                .reader(reader())
                .processor(processor)
                .writer(writer)
                .build();
    }
    
    @Bean(name="testJob02")
    public Job job() {
        return jobBuilderFactory.get("testJob02")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(step())
                .end()
                .build();
    }
}