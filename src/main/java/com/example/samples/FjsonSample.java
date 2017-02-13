package com.example.samples;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class FjsonSample {

    public static void main(String[] args) {

        toJSONString();

    }

    public static void toJSONString() {

        FjsonUserInfo user = new FjsonUserInfo();

        user.setMessageCount(1);

        FjsonSession session = new FjsonSession();

        session.setFlag(1);

        List<String> mobileList = new ArrayList<String>();

        mobileList.add("13438351234");

        mobileList.add("13438364231");

        session.setMobileList(mobileList);

        session.setSessionKey("07dKmRTD6FqXsz0r");

        session.setUserId(10501L);

        session.setVip(false);

        user.setSession(session);

        user.setUserName("helloworld");

        user.setImage("cukOgXPuaqypOUBLG89vobY_E-Q.jpg");

        // toJSONString

        String json = JSON.toJSONString(user);

        String prettyJson = JSON.toJSONString(user, true);

        System.out.println(json);

        System.out.println(prettyJson);

    }

}
