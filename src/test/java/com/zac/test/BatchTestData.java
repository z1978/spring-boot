package com.zac.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author 
 * @version
 * @since 2.0.0
 */

public class BatchTestData {
    /**
     * Clean Tables
     * 
     * @param scriptRunner
     * @throws IOException
     */
    public static void cleanTables(ScriptRunner scriptRunner) throws IOException{
        scriptRunner.runScript(new InputStreamReader(new ClassPathResource("SQLs/prepareCleanDB.sql").getInputStream()));
    }
    
    /**
     * Prepare Data
     * 
     * @param scriptRunner
     * @param classPathUrl
     * @throws IOException
     */
    public static void prepareData(ScriptRunner scriptRunner, String classPathUrl) throws IOException{
        scriptRunner.runScript(new InputStreamReader(new ClassPathResource(classPathUrl).getInputStream()));
    }

    /**
     * Update user create_time
     * 
     * @param scriptRunner
     * @param testDay( eg. yyyy-MM-dd )
     * @param testTime( eg. 23:59:59 )
     * @param userId
     * @throws ParseException
     */
    public  static void updateCreate_time(ScriptRunner scriptRunner ,String testDay,String testTime,String userId) throws ParseException{
        String sqlStr = "UPDATE user SET create_time='" + testDay + " " + testTime + "' WHERE id in ('" + userId + "');";
        scriptRunner.runScript(new StringReader(sqlStr));
    }
    
    /**
     * Update user update_time
     * 
     * @param scriptRunner
     * @param testDay( eg. yyyy-MM-dd )
     * @param testTime( eg. 23:59:59 )
     * @param userId
     * @throws ParseException
     */
    public  static void updateUpdate_time(ScriptRunner scriptRunner ,String testDay,String testTime,String userId) throws ParseException{
        String sqlStr = "UPDATE user SET update_time='" + testDay + " " + testTime + "' WHERE id in ('" + userId + "');";
        scriptRunner.runScript(new StringReader(sqlStr));
    }

    /**
     * Get the date n days ago or after
     * 
     * @param days( eg. -3 or 3 )
     * @return formatted day("yyyy-MM-dd")
     * @throws ParseException
     */
    public static String getDay(int days) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format.format(cal.getTime());

        return formatted;
    }
}
