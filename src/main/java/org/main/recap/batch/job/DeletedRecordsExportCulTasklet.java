package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.DeletedRecordsExportCulService;
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
 * Created by rajeshbabuk on 29/6/17.
 */
public class DeletedRecordsExportCulTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DeletedRecordsExportCulTasklet.class);

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Autowired
    private DeletedRecordsExportCulService deletedRecordsExportCulService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    /**
     * This method starts the execution of deleted records export job for Columbia.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing DeletedRecordsExportCulTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        long jobInstanceId = jobExecution.getJobInstance().getInstanceId();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
        logger.info("Job Parameter in Deleted Records Export Cul Tasklet : {}", jobNameParam);
        if (!jobName.equalsIgnoreCase(jobNameParam)) {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, createdDate, jobInstanceId);
        }

        String status = deletedRecordsExportCulService.deletedRecordsExportCul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL, createdDate);
        logger.info("Deleted Records Export CUL status : {}", status);
        return RepeatStatus.FINISHED;
    }
}