package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.MatchingAlgorithmService;
import org.main.recap.batch.service.UpdateJobDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * Created by rajeshbabuk on 3/4/17.
 */
public class MatchingAlgorithmTasklet implements Tasklet{

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmTasklet.class);

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private MatchingAlgorithmService matchingAlgorithmService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    /**
     * This method starts the execution of the matching algorithm job.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing MatchingAlgorithmTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
        logger.info("Job Parameter in Matching Algorithm Tasklet : {}", jobNameParam);
        if(!jobName.equalsIgnoreCase(jobNameParam)) {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate);
        }

        String status = matchingAlgorithmService.initiateMatchingAlgorithm(solrClientUrl, createdDate);
        logger.info("Matching algorithm status : {}", status);
        return RepeatStatus.FINISHED;
    }
}
