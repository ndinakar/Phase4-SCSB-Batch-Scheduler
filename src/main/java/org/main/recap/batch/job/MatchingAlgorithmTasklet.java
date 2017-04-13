package org.main.recap.batch.job;

import org.main.recap.batch.service.MatchingAlgorithmService;
import org.main.recap.batch.service.UpdateJobDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MatchingAlgorithmTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmTasklet.class);

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Autowired
    private MatchingAlgorithmService matchingAlgorithmService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing MatchingAlgorithmTasklet");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();
        updateJobDetailsService.updateJob(serverProtocol, solrClientUrl, jobName, createdDate);

        String status = matchingAlgorithmService.initiateMatchingAlgorithm(serverProtocol, solrClientUrl);
        logger.info("Matching algorithm status : " + status);
        return RepeatStatus.FINISHED;
    }
}
