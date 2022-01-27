package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by rajeshbabuk on 14/9/17.
 */
@Slf4j
public class StartOrStopPollingLongRunningJobsTasklet implements Tasklet {

    /**
     * This method starts or stops the execution of polling log running jobs.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing StartOrStopPollingLongRunningJobsTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        String action = jobExecution.getJobParameters().getString(ScsbConstants.POLLING_ACTION);
        if (ScsbConstants.START.equalsIgnoreCase(action)) {
            ScsbConstants.POLL_LONG_RUNNING_JOBS = true;
            log.info("Started polling long running jobs");
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS,  "Started polling long running jobs"));
        } else if (ScsbConstants.STOP.equalsIgnoreCase(action)) {
            ScsbConstants.POLL_LONG_RUNNING_JOBS = false;
            log.info("Stopped polling long running jobs");
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS,  "Stopped polling long running jobs"));
        }
        return RepeatStatus.FINISHED;
    }
}
