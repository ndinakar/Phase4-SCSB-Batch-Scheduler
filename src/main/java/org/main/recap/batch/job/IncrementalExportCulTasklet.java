package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.IncrementalExportCulService;
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
 * Created by rajeshbabuk on 23/6/17.
 */
public class IncrementalExportCulTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportCulTasklet.class);

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Autowired
    private IncrementalExportCulService incrementalExportCulService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    /**
     * This method starts the execution of incremental export job for Columbia.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing IncrementalExportCulTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        long jobInstanceId = jobExecution.getJobInstance().getInstanceId();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
        logger.info("Job Parameter in Incremental Export Cul Tasklet : {}", jobNameParam);
        if (!jobName.equalsIgnoreCase(jobNameParam)) {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
        }

        String status = incrementalExportCulService.incrementalExportCul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_CUL, createdDate);
        logger.info("Incremental Export CUL status : {}", status);
        return RepeatStatus.FINISHED;
    }
}
