package com.example.samples;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FjsonSession {

    private String sessionKey;

    private long userId;

    private boolean isVip;

    private int flag;

    private List<String> mobileList;

}
