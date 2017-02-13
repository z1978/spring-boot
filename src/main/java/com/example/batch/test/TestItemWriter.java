package com.example.batch.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.entity.User;

@Component(value="testItemWriter")
public class TestItemWriter implements ItemWriter<User> {
    private static final Logger log = LoggerFactory.getLogger(TestItemWriter.class);

    @Override
    public void write(List<? extends User> userList) throws Exception {
        for (int i = 0; i < userList.size(); i++) {
            User record = userList.get(i);
            record.setId(9999 + i);

//            log.info(record.getId().toString());
//            log.info(record.getFirstName());

        }
    }
}