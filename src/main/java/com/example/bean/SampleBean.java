
package com.example.bean;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SampleBean {

    @Value("${sample.name}")
    private String name;

    @Value("${sample.age}")
    private int age;

    public String getMessage() {
        return String.format("Hello! My name is %s. I'm %d years old.", name, age);
    }

}
