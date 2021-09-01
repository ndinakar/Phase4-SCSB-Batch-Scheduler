package org.recap.batch.job;

import org.recap.ScsbConstants;
import org.recap.batch.service.StatusReconciliationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hemalathas on 1/6/17.
 */
public class StatusReconcilationTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(StatusReconcilationTasklet.class);

    @Autowired
    private StatusReconciliationService statusReconciliationService;

    /**
     * This method starts the execution of status reconciliation job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing Status Reconciliation");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"Status Reconciliation Tasklet", Boolean.FALSE);
            String resultStatus = statusReconciliationService.statusReconciliation(scsbCoreUrl);
            logger.info("Periodic LAS item status reconciliation status : {}", resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.STATUS_RECONCILIATION_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
