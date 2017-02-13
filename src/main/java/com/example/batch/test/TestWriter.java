package com.example.batch.test;
//package com.example.batch.mybatis;
//
//import java.util.List;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.example.dao.UserMapper;
//import com.example.entity.User;
//
//@Component
//public class MybatisWriter implements ItemWriter<User>{
//    private static final Logger log = LoggerFactory.getLogger(MybatisWriter.class);
//    
//    @Autowired
//    private UserMapper userMapper;
//    @Override
//    public void write(List<? extends User> user) throws Exception {
//        for (int i = 0; i < user.size(); i++) {
//            User record = user.get(i);
//            log.info(record.getFirstName());
//
////            generateUserTbl(user.get(i));
//        }
//    }
//    
////    private void generateUserTbl(User record){
////        userMapper.insertSelective(record);
////    }
//}
