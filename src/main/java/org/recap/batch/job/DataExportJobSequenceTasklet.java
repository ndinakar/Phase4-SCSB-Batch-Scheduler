package org.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.DataExportJobSequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by rajeshbabuk on 10/7/17.
 */
public class DataExportJobSequenceTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DataExportJobSequenceTasklet.class);

    @Autowired
    private DataExportJobSequenceService dataExportJobSequenceService;

    /**
     * This method starts the execution of incremental and delete data export.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing DataExportJobSequenceTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String exportStringDate = jobExecution.getJobParameters().getString(ScsbConstants.FROM_DATE);
            Date createdDate = jobExecution.getCreateTime();
            updateJob(jobExecution,"Data Export Job Sequence Tasklet", Boolean.TRUE);
            String resultStatus = dataExportJobSequenceService.dataExportJobSequence(scsbEtlUrl, createdDate, exportStringDate);
            logger.info("Incremental and delete data export status : {}", resultStatus);
            if (StringUtils.containsIgnoreCase(ScsbCommonConstants.FAIL, resultStatus)) {
                executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
                executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, ScsbConstants.DATA_EXPORT_STATUS_NAME + " " + resultStatus);
                stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ScsbConstants.DATA_EXPORT_STATUS_NAME + " " + resultStatus));
            } else {
                executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.SUCCESS);
                executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, ScsbConstants.DATA_EXPORT_STATUS_NAME + " " + resultStatus);
                stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS, ScsbConstants.DATA_EXPORT_STATUS_NAME + " " + resultStatus));
            }
        } catch (Exception ex) {
            logger.error("{} {}", ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, ScsbConstants.DATA_EXPORT_STATUS_NAME + " " + ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
