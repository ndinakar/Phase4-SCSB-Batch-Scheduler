package org.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
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

/**
 * Created by angelind on 9/5/17.
 */
public class AccessionTasklet extends JobCommonTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(AccessionTasklet.class);

    /**
     * This method starts the execution of the accession job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Executing AccessionTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        updateJob(jobExecution, "Accession Tasklet", Boolean.TRUE);
        String resultStatus = getResultStatus(jobExecution, stepExecution, logger, executionContext, ScsbCommonConstants.ACCESSION_JOB_INITIATE_QUEUE, ScsbCommonConstants.ACCESSION_JOB_COMPLETION_OUTGOING_QUEUE, ScsbConstants.ACCESSION_STATUS_NAME);

        logger.info("Job Id : {} Accession Job Result Status : {}", jobExecution.getId(), resultStatus);
        if (!StringUtils.containsIgnoreCase(resultStatus, ScsbConstants.SUCCESS) && !ScsbCommonConstants.ACCESSION_NO_PENDING_REQUESTS.equals(resultStatus)) {
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, ScsbConstants.ACCESSION_STATUS_NAME + " " + resultStatus);
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ScsbConstants.ACCESSION_STATUS_NAME + " " + resultStatus));
        } else {
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.SUCCESS);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, ScsbConstants.ACCESSION_STATUS_NAME + " " + resultStatus);
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.SUCCESS, ScsbConstants.ACCESSION_STATUS_NAME + " " + resultStatus));
        }
    return RepeatStatus.FINISHED;
}
}
