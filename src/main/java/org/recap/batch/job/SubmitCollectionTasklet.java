package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;


/**
 * Created by harikrishnanv on 19/6/17.
 */
@Slf4j
public class SubmitCollectionTasklet extends JobCommonTasklet implements Tasklet {

    /**
     * This method starts the execution of the submit collection job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing submit collection");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        updateJob(jobExecution, "Submit Collection Tasklet", Boolean.TRUE);
        String resultStatus = getResultStatus(jobExecution, stepExecution, log, executionContext, ScsbCommonConstants.SUBMIT_COLLECTION_JOB_INITIATE_QUEUE, ScsbCommonConstants.SUBMIT_COLLECTION_JOB_COMPLETION_OUTGOING_QUEUE, ScsbConstants.SUBMIT_COLLECTION_STATUS_NAME);

        log.info("Job Id : {} Submit Collection Job Result Status : {}", jobExecution.getId(), resultStatus);
        setExecutionContext(executionContext, stepExecution, ScsbConstants.SUBMIT_COLLECTION_STATUS_NAME + " " + resultStatus);
        return RepeatStatus.FINISHED;
    }
}
