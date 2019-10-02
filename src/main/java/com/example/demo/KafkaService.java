package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    @Qualifier("asyncJobLauncher")
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    @KafkaListener(topics = "test-topic")
    public void listen(String message) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        System.out.println("THIS MESSAGE WAS CONSUMED BY THE LISTENER: " + message);

        JobParameters jobParameters = new JobParametersBuilder().
                addString("treeId", message)
                .toJobParameters();

        System.out.println("triggering: " + message);
        jobLauncher.run(job, jobParameters);
        System.out.println("triggered: " + message);
    }

}
