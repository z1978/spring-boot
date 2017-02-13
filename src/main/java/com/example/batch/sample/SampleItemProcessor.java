package com.example.batch.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

import com.example.bean.Person;
import com.example.util.ZacException;

public class SampleItemProcessor implements ItemProcessor<Person, Person> {
    private static final Logger log = LoggerFactory.getLogger(SampleItemProcessor.class);

    @Value("${zac.timezone}")
    private String zacTimezone;
    
    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName();
        final String lastName = person.getLastName();
        log.debug(lastName);

        final Person transformedPerson = new Person(firstName, lastName);

        // 異常を取得テスト
        try {
            testException();
        } catch (Exception e) {
            log.debug("Test exception done");
        }

        return transformedPerson;
    }

    // 異常を取得テスト
    protected void testException() throws ZacException {
        log.debug("Test exception");
        log.debug(zacTimezone);
        throw new ZacException("This is a error msg.", "testException");
    }

}
