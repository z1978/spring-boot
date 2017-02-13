package com.example.batch.csv;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.bean.User2;
import com.example.util.ZacValidator;

public class CsvUserItemProcessor implements ItemProcessor<User2, User2> {
    private static final Logger log = LoggerFactory.getLogger(CsvUserItemProcessor.class);
    
    @Autowired
    protected ZacValidator zacValidator;
    
    @Override
    public User2 process(final User2 user) throws Exception {
        List<User2> items = new ArrayList<User2>();
        items.add(user);
//        items = JSON.parseArray(user.getFirstName(), User.class);
        // パラメータチェック
        if (!zacValidator.listValidator(items)){
            log.warn("");
        }
        
        final String firstName = user.getFirstName();
        final String lastName  = user.getLastName();
        final Integer age = user.getAge();
        final Date createTime = (new Date());
        final Date updateTime = (new Date());
        
        final User2 transformedUser = new User2(firstName, lastName, age, createTime, updateTime);

        return transformedUser;
    }
}
