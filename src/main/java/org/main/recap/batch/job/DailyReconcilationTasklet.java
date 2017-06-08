package org.main.recap.batch.job;

import org.main.recap.batch.service.DailyReconcilationService;
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
 * Created by akulak on 10/5/17.
 */
public class DailyReconcilationTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DailyReconcilationTasklet.class);

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Autowired
    private DailyReconcilationService dailyReconcilationService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    /**
     * This method starts the execution of the daily reconciliation job.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing DailyReconcilation");
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        Date createdDate = chunkContext.getStepContext().getStepExecution().getJobExecution().getCreateTime();
        updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate);

        String status = dailyReconcilationService.dailyReconcilation(scsbCircUrl);
        logger.info("Daily Reconciliation Job status : {}", status);
        return RepeatStatus.FINISHED;
    }
}
