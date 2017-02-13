//package com.example;
//
//import java.util.Date;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
//import com.example.util.ZacTimeUtil;
//
//
//@SpringBootApplication
////
//@ComponentScan({ "com.example" })
////
//@Configuration
//// 导入配置文件
//@PropertySource({ "classpath:beans.properties" })
//@EnableAutoConfiguration
//public class App {
//    private static final Logger log = LoggerFactory.getLogger(App.class);
//
//    public static void main(String[] args) throws Exception {
//        
////      String jobName = args[0].toString();
////      log.info(jobName);
//        
////      SpringApplication.run(App.class, args);
////        System.exit(SpringApplication.exit(SpringApplication.run(App.class, args)));
//        
//        ZacTimeUtil zacTimeUtil = new ZacTimeUtil();
//        
//        // ns start time
//        long myStartNanoTime = System.nanoTime();
//        log.info("End time = [" + zacTimeUtil.longToString(myStartNanoTime) + "]");
//        
//        // ms start time
//        long myStartCurrentTimeMillis = System.currentTimeMillis();
//        log.info("End time = [" + zacTimeUtil.longToString(myStartCurrentTimeMillis) + "]");
//        
//        // date start Time
//        Date startDate = new Date();
//        log.info("Start time = [" + zacTimeUtil.dateToString(startDate) + "]");
//
//        //---------- APP Run ----------
//        int flg = SpringApplication.exit(SpringApplication.run(App.class, args));
//        
//        // ns end time
//        long myEndNanoTime = System.nanoTime();
//        log.info("End time = [" + zacTimeUtil.longToString(myEndNanoTime) + "]");
//        
//        // ms end time
//        long myEndCurrentTimeMillis = System.currentTimeMillis();
//        log.info("End time = [" + zacTimeUtil.longToString(myEndCurrentTimeMillis) + "]");
//        
//        // date End Time
//        Date endDate = new Date();
//        log.info("End time = [" + zacTimeUtil.dateToString(endDate) + "]");
//
//
//        // running time
//        long ms = 0L;
//        ms = zacTimeUtil.getExecTime(myStartNanoTime, myEndNanoTime) / 1000000L;
//        System.err.println("Program running time(nanoTime) = [" + ms + "]ms");
//        System.err.println("Program running time(nanoTime) = [" + ms/1000f + "]s");
//        ms = zacTimeUtil.getExecTime(myStartCurrentTimeMillis, myEndCurrentTimeMillis);
//        System.err.println("Program running time(currentTimeMillis) = [" + ms + "]ms");
//        System.err.println("Program running time(currentTimeMillis) = [" + ms/1000f + "]s");
//        ms = zacTimeUtil.getExecTime(startDate.getTime(), endDate.getTime());
//        System.err.println("Program running time(new Date()) = [" + ms + "]ms");
//        System.err.println("Program running time(new Date()) = [" + ms/1000f + "]s");
//
//        
////        log.info("Program running time = [" + execTimeCurrentTimeMillis + "]ms");
////        log.info("Program running time = [" + execTimeCurrentTimeMillis/1000f + "]s");
//        
//        // The system exit
//        if ( flg ==0 ) {
//            System.err.println("[" + flg + "] APP Execution finished !!!");
//            System.exit(flg);
//        } else {
//            System.err.println("[" + flg + "] APP Execution failed !!!");
//            System.exit(flg);
//        }        
////        System.exit(0);
//    }
//}
