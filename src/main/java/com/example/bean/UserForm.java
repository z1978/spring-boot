package com.example.bean;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserForm implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull // (1)
  @Size(min = 1, max = 20) // (2)
  private String name;

  @NotNull
  @Size(min = 1, max = 50)
  @Email // (3)
  private String email;

  @NotNull // (4)
  @Min(0) // (5)
  @Max(200) // (6)
  private Integer age;

  // omitted setter/getter
}
