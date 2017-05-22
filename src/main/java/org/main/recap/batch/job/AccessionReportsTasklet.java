package org.main.recap.batch.job;

import org.main.recap.RecapConstants;
import org.main.recap.batch.service.GenerateReportsService;
import org.main.recap.batch.service.UpdateJobDetailsService;
import org.main.recap.jpa.JobDetailsRepository;
import org.main.recap.model.jpa.JobEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by angelind on 2/5/17.
 */
public class AccessionReportsTasklet implements Tasklet, StepExecutionListener{

    private static final Logger logger = LoggerFactory.getLogger(AccessionReportsTasklet.class);

    @Value("${server.protocol}")
    private String serverProtocol;

    @Value("${scsb.solr.client.url}")
    private String solrClientUrl;

    @Autowired
    private UpdateJobDetailsService updateJobDetailsService;

    @Autowired
    private GenerateReportsService generateReportsService;

    private Date jobCreatedDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing AccessionReportsTasklet");
        JobExecution jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
        String jobName = jobExecution.getJobInstance().getJobName();
        Date createdDate = jobExecution.getCreateTime();
        if(jobCreatedDate != null) {
            logger.info("Job Created Date : " + jobCreatedDate);
            createdDate = jobCreatedDate;
        } else {
            updateJobDetailsService.updateJob(serverProtocol, solrClientUrl, jobName, createdDate);
        }

        String status = generateReportsService.generateReport(serverProtocol, solrClientUrl, getFromDate(createdDate), jobName);
        logger.info("Accession Report status : {}", status);
        return RepeatStatus.FINISHED;
    }

    public Date getFromDate(Date createdDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdDate);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
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
