package com.example.bean;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User2 {
    @NotBlank(message="lastName can't be Blank")
    private String lastName;
    @NotBlank(message="firstName can't be Blank")
    private String firstName;
    @NotNull(message="age can't be Null")
    private Integer age;
    private Date createTime;
    private Date updateTime;
}
