package com.example.batch.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;



@Component
public class TestTaskletReader implements Tasklet {
    private static final Logger log = LoggerFactory.getLogger(TestTaskletReader.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ClientConfig cc = new ClientConfig();
        Client client = ClientBuilder.newClient(cc);

        return null;
    }
}