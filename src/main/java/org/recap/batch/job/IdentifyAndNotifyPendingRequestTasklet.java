package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.recap.batch.service.IdentifyPendingRequestService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class IdentifyAndNotifyPendingRequestTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private IdentifyPendingRequestService identifyPendingRequestService;

    /**
     * This method starts the execution for checking the requests if they are in "Pending" status and notify them through sending email.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Identifying requests that are in Pending status ...");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"IdentifyAndNotifyPendingRequestTasklet", Boolean.FALSE);
            identifyPendingRequestService.identifyPendingRequestService(scsbCircUrl);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.IDENTIFY_AND_NOTIFY_PENDING_REQUEST_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
