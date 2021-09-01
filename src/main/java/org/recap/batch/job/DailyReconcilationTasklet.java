package org.recap.batch.job;

import org.recap.ScsbConstants;
import org.recap.batch.service.DailyReconcilationService;
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
 * Created by akulak on 10/5/17.
 */
public class DailyReconcilationTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(DailyReconcilationTasklet.class);

    @Autowired
    private DailyReconcilationService dailyReconcilationService;

   
    /**
     * This method starts the execution of the daily reconciliation job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing DailyReconciliationTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"DailyReconciliationTasklet", Boolean.FALSE);
            String resultStatus = dailyReconcilationService.dailyReconcilation(scsbCoreUrl);
            logger.info("Daily Reconciliation Job status : {}", resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.DAILY_RECONCILIATION_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
