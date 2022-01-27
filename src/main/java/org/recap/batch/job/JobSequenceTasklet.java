package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by angelind on 8/5/17.
 */
@Slf4j
public class JobSequenceTasklet extends JobCommonTasklet implements Tasklet {
    /**
     * This method starts the execution of sequential processing job.
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing JobSequenceTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            executionContext.put(ScsbConstants.JOB_NAME, jobExecution.getJobInstance().getJobName());
            updateJob(jobExecution, "JobSequenceTasklet", Boolean.FALSE);
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS, ScsbConstants.SUCCESS));
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.JOB_SEQUENCE_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}