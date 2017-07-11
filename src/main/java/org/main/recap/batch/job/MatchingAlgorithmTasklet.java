package org.main.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
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

import java.text.SimpleDateFormat;
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
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
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
            logger.info("Job Parameter in Matching Algorithm Tasklet : {}", jobNameParam);
            if (!jobName.equalsIgnoreCase(jobNameParam)) {
                updateJobDetailsService.updateJob(solrClientUrl, jobName, jobExecution.getCreateTime(), jobInstanceId);
            }
            String resultStatus = matchingAlgorithmService.initiateMatchingAlgorithm(solrClientUrl, createdDate);
            logger.info("Matching algorithm status : {}", resultStatus);
            if (StringUtils.containsIgnoreCase(resultStatus, RecapConstants.FAIL)) {
                executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
                executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, resultStatus);
                stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, resultStatus));
            } else {
                executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.SUCCESS);
                executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, resultStatus);
                stepExecution.setExitStatus(new ExitStatus(RecapConstants.SUCCESS, resultStatus));
            }
        } catch (Exception ex) {
            logger.error(RecapConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
