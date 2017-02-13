//package com.example.batch.test;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.batch.MyBatisPagingItemReader;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.example.entity.User;
//
//@Component(value="testMyBatisPagingItemReader")
//public class TestMyBatisPagingItemReader extends MyBatisPagingItemReader<User> {
//    private static final Logger log = LoggerFactory.getLogger(TestMyBatisPagingItemReader.class);
//    
//    @Autowired
//    private TestMyBatisPagingItemReader( SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception{
//        this.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
//        this.setQueryId("com.example.dao.UserMapper.selectByPrimaryKey");
//        this.setParameterValues(getParameterValues());
//        this.setPageSize(10);
//    }
//    
//    private Map<String, Object> getParameterValues() throws Exception {
//        Integer id = 151;
//        Map<String, Object> parameters = new HashMap<>();
//        parameters.put("id", id);
////      parameters.put("createTime", new Date());
////      parameters.put("updateTime", new Date());
//        return parameters;
//    }
//}