package com.example.batch.csv;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.bean.User2;
import com.example.listener.JobCompletionNotificationListener;


@Configuration
@EnableBatchProcessing
public class CsvUserItemConfiguration {
    private static final Logger log = LoggerFactory.getLogger(CsvUserItemConfiguration.class);

    @Value("${sample.name}")
    private String sampleName;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean//如果不写(name="csvUserItemStep")就要小心reader方法不能重名，否者会有冲突
    public FlatFileItemReader<User2> reader() {
        FlatFileItemReader<User2> reader = new FlatFileItemReader<User2>();
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
        BeanWrapperFieldSetMapper<User2> fieldSetMapper = new BeanWrapperFieldSetMapper<User2>();
        fieldSetMapper.setTargetType(User2.class);

        DefaultLineMapper<User2> lineMapper = new DefaultLineMapper<User2>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public JdbcBatchItemWriter<User2> writer() {
        JdbcBatchItemWriter<User2> writer = new JdbcBatchItemWriter<User2>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User2>());
        writer.setSql("INSERT INTO user (first_name, last_name, age, create_time, update_time) VALUES (:firstName, :lastName, :age, :createTime, :updateTime)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public CsvUserItemProcessor processor() {
        return new CsvUserItemProcessor();
    }

    @Bean
    public JobExecutionListener listener2() {
        return new JobCompletionNotificationListener();
    }

    @Bean
    public Step step() {

        log.info(driverClassName);
        log.info(sampleName);
        
        return stepBuilderFactory.get("csvUserItemStep")
                .<User2, User2> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(name="csvUserItemJob")
    public Job job() {
        return jobBuilderFactory.get("csvUserItemJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener2())
                .flow(step())
                .end()
                .build();
    }
}

