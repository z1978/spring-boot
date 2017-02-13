//package com.example;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//
//
//@Configuration
//@PropertySource({ "classpath:beans.properties" })
//@ComponentScan({ "com.example" })
//public class Main {
//    private static final Logger log = LoggerFactory.getLogger(Main.class);
//
//    public static void main(String[] args) throws Exception {
//        log.debug("Batch Framework starts.");
//        
//        String jobName = args[0].toString();
//        
//        ApplicationContext applicationContext = SpringApplication.run(Main.class, args);
//        JobLauncher jobLauncher = (JobLauncher) applicationContext.getBean("jobLauncher");
//        Job job = (Job) applicationContext.getBean(jobName);
//        jobLauncher.run(job, new JobParameters());
//        
//        System.err.println("Game over !!!");
//        //
//        System.exit(0);
//    }
//}