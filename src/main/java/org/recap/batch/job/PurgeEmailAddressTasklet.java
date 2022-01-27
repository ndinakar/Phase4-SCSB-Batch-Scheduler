package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.PurgeEmailAddressService;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by rajeshbabuk on 28/3/17.
 */
@Slf4j
public class PurgeEmailAddressTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private PurgeEmailAddressService purgeEmailAddressService;

    /**
     * This method starts the execution of purging email addresses job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing PurgeEmailAddressTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution, "PurgeEmailAddressTasklet", Boolean.FALSE);
            Map<String, String> resultMap = purgeEmailAddressService.purgeEmailAddress(scsbCoreUrl);
            String status = resultMap.get(ScsbCommonConstants.STATUS);
            String message = ScsbCommonConstants.PURGE_EDD_REQUEST + ":" + resultMap.get(ScsbCommonConstants.PURGE_EDD_REQUEST)
                    + ", " + ScsbCommonConstants.PURGE_PHYSICAL_REQUEST + ":" + resultMap.get(ScsbCommonConstants.PURGE_PHYSICAL_REQUEST);
            log.info("Purge Email Addresses status : {}", status);
            log.info("Purge Email Addresses status message : {}", message);
            executionContext.put(ScsbConstants.JOB_STATUS, status);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, message);
            stepExecution.setExitStatus(new ExitStatus(status, message));
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.PURGE_EMAIL_ADDRESS_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
