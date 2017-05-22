package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.MatchingAlgorithmService;
import org.main.recap.batch.service.UpdateJobDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
public class MatchingAlgorithmTasklet implements Tasklet, StepExecutionListener{

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmTasklet.class);

    @Value("${server.protocol}")
    private String serverProtocol;

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private MatchingAlgorithmService matchingAlgorithmService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    private Date jobCreatedDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing MatchingAlgorithmTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        if(jobCreatedDate != null) {
            logger.info("Job Created Date : " + jobCreatedDate);
            createdDate = jobCreatedDate;
        } else {
            updateJobDetailsService.updateJob(serverProtocol, solrClientUrl, jobName, createdDate);
        }

        String status = matchingAlgorithmService.initiateMatchingAlgorithm(serverProtocol, solrClientUrl, createdDate);
        logger.info("Matching algorithm status : {}", status);
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        jobCreatedDate = (Date) executionContext.get(RecapConstants.JOB_CREATED_DATE);
        logger.info("Date Before Execution: {}", jobCreatedDate);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        if(jobCreatedDate != null) {
            logger.info("Date After Execution : {}",jobCreatedDate);
            executionContext.put(RecapConstants.JOB_CREATED_DATE, jobCreatedDate);
        }
        return null;
    }
}
