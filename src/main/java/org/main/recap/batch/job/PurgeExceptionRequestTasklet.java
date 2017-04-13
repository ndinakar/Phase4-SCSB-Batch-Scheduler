package org.main.recap.batch.job;

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
 * Created by rajeshbabuk on 23/3/17.
 */
public class PurgeExceptionRequestTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(PurgeExceptionRequestTasklet.class);

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing PurgeExceptionRequestTasklet");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();
        updateJobDetailsService.updateJob(serverProtocol, solrClientUrl, jobName, createdDate);

        return RepeatStatus.FINISHED;
    }
}
