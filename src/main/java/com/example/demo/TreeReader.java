package com.example.demo;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

@Component
@StepScope
public class TreeReader implements ItemReader<String> {

    String treeId;
    List<String> list = new LinkedList<>();

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
        treeId = parameters.getString("treeId");
        IntStream.range(0,10000).boxed().forEach(i -> list.add("segment:" + i));
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!list.isEmpty()) {
            return list.remove(0);
        }
        return null;
    }
}
