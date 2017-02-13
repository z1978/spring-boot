package com.example.batch.test;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.entity.User;
import com.example.util.ZacValidator;

// @Component注解(annotation)告诉spring，要加载此类
@Component(value="testItemProcessor")
public class TestItemProcessor implements ItemProcessor<User, User> {
    private static final Logger log = LoggerFactory.getLogger(TestItemProcessor.class);
    
    @Autowired
    protected ZacValidator zacValidator;
    
    @Override
    public User process(final User user) throws Exception {
        List<User> items = new ArrayList<User>();
        items.add(user);
//        items = JSON.parseArray(user.getFirstName(), User.class);
        // パラメータチェック
        if (!zacValidator.listValidator(items)){
            log.warn("");
        }
        
        final String firstName = user.getFirstName();
        final String lastName  = user.getLastName();
        final Integer age = user.getAge();

        final User transformedUser = new User();
        transformedUser.setFirstName(firstName);
        transformedUser.setLastName(lastName);
        transformedUser.setAge(age);
        transformedUser.setCreateTime(new Date());
        transformedUser.setUpdateTime(new Date());
        
        return transformedUser;
    }
}
