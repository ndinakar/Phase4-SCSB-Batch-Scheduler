package org.main.recap.batch.job;

import org.main.recap.batch.service.PurgeAccessionRequestsService;
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
 * Created by rajeshbabuk on 22/5/17.
 */
public class PurgeAccessionRequestTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(PurgeAccessionRequestTasklet.class);

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Autowired
    private PurgeAccessionRequestsService purgeAccessionRequestsService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing PurgeAccessionRequestTasklet");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();
        updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate);

        String status = purgeAccessionRequestsService.purgeAccessionRequests(scsbCircUrl);
        logger.info("Purge Accession Requests status : {}", status);
        return RepeatStatus.FINISHED;
    }
}
