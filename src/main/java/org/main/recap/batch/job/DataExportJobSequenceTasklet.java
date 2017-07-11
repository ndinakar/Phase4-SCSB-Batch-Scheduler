package org.main.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.main.recap.RecapConstants;
import org.main.recap.batch.service.DataExportJobSequenceService;
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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rajeshbabuk on 10/7/17.
 */
public class DataExportJobSequenceTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DataExportJobSequenceTasklet.class);

    @Value("${scsb.solr.client.url}")
    String solrClientUrl;

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Autowired
    private DataExportJobSequenceService dataExportJobSequenceService;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    /**
     * This method starts the execution of incremental and delete data export.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing DataExportJobSequenceTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        String fromDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate;
        if (StringUtils.isNotBlank(fromDate)) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(RecapConstants.FROM_DATE_FORMAT);
            createdDate = dateFormatter.parse(fromDate);
        } else {
            createdDate = jobExecution.getCreateTime();
        }
        long jobInstanceId = jobExecution.getJobInstance().getInstanceId();
        String jobName = jobExecution.getJobInstance().getJobName();
        String jobNameParam = (String) jobExecution.getExecutionContext().get(RecapConstants.JOB_NAME);
        logger.info("Job Parameter in Data Export Job Sequence Tasklet : {}", jobNameParam);
        if (!jobName.equalsIgnoreCase(jobNameParam)) {
            updateJobDetailsService.updateJob(solrClientUrl, jobName, jobExecution.getCreateTime(), jobInstanceId);
        }
        String status = dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl, createdDate);
        logger.info("Incremental and delete data export status : {}", status);
        return RepeatStatus.FINISHED;
    }
}
