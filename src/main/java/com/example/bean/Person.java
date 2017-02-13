package com.example.bean;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    @NotBlank(message="lastName can't be Blank")
    private String lastName;
    @NotBlank(message="firstName can't be Blank")
    private String firstName;
}
