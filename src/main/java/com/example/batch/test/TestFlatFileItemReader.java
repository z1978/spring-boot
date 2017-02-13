//package com.example.batch.test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//
//import com.example.entity.User;
//
//public class TestFlatFileItemReader extends FlatFileItemReader<User> {
//    private static final Logger log = LoggerFactory.getLogger(TestFlatFileItemReader.class);
//
//    @Autowired
//    public TestFlatFileItemReader() throws Exception{
//        FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
//        reader.setResource(new ClassPathResource("sample-data2.csv"));
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
//}