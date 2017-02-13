package com.example.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.util.ZacTimeUtil;

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Autowired
    protected ZacTimeUtil runTimeUtil;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String startTimeStr = runTimeUtil.dateToString(jobExecution.getStartTime());
        System.err.println(">>>>>>>>>> " + startTimeStr + "[" + jobExecution.getJobInstance().getJobName() + "] Job START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long runtime = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        log.info("Runtime:" + "[" + runtime + " milliseconds]");

        String endTimeStr = runTimeUtil.dateToString(jobExecution.getEndTime());
        System.err.println("<<<<<<<<<< " + endTimeStr + "[" + jobExecution.getJobInstance().getJobName() + "] Job END   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }
}
