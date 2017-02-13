package com.example.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 時間処理を使用する為のUtil
 */
@Component
public class ZacTimeUtil {
    private static final Logger log = LoggerFactory.getLogger(ZacTimeUtil.class);

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    public static void main(String[] args) throws Exception {
        String tsStr = "";
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            // 方法一
            tsStr = simpleDateFormat.format(ts);
            System.out.println(tsStr);
            // 方法二
            tsStr = ts.toString();
            System.out.println(tsStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String dateToString(Date date) {
        String dateStr = simpleDateFormat.format(date);
        log.debug(dateStr);
        return dateStr;
    }

    public String longToString(long time) {
        String dateStr = simpleDateFormat.format(new Timestamp(time));
        log.debug(dateStr);
        return dateStr;
    }
    
    public long getExecTime(long startTime, long endTime) {
        long diffTime = endTime - startTime;
        return diffTime;
    }
}
