package com.example.demo;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher(JobRepository jobRepository) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(taskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(4);
        return taskExecutor;
    }

    @Bean
    Job job(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory
                .get("generateTree")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        System.out.println("JOB BEFORE");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        System.out.println("JOB AFTER");
                    }
                })
                .build();
    }

    @Bean
    public org.springframework.batch.core.scope.StepScope stepScope() {
        final org.springframework.batch.core.scope.StepScope stepScope = new org.springframework.batch.core.scope.StepScope();
        stepScope.setAutoProxy(true);
        return stepScope;
    }

    @Bean
    @StepScope
    public Step step(StepBuilderFactory stepBuilders, TreeReader treeReader) {
        return stepBuilders.get("segmentStep")
                .<String, String>chunk(1000)
                .reader(treeReader)
                .processor(processSegment())
                .writer(persistSegment())
                .taskExecutor(taskExecutor())
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        System.out.println("BEFORE");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        // publish a kafka message etc...
                        System.out.println("AFTER");
                        return ExitStatus.COMPLETED;
                    }
                })
                .build();
    }

    private ItemWriter<? super String> persistSegment() {
        return (ItemWriter<String>) segment -> System.out.println("persisting:" + segment);
    }

    private ItemProcessor<String, String> processSegment() {
        return segment -> "processed:" + segment;
    }

}
