package com.example.samples;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FjsonUserInfo {

    private String userName;

    private int messageCount;

    private String image;

    private FjsonSession session;
}
