package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.recap.ScsbConstants;
import org.recap.batch.service.GenerateReportsService;
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
 * Created by angelind on 2/5/17.
 */
@Slf4j
public class AccessionReportsTasklet extends JobCommonTasklet implements Tasklet {

    @Autowired
    private GenerateReportsService generateReportsService;

    /**
     * This method starts the execution of the accession reports job.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing AccessionReportsTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            Date createdDate = getCreatedDate(jobExecution);
            updateJob(jobExecution, "Accession Reports Tasklet", Boolean.TRUE);
            String resultStatus = generateReportsService.generateReport(solrClientUrl, createdDate, ScsbConstants.GENERATE_ACCESSION_REPORT_JOB);
            log.info("Accession Report status : {}", resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.ACCESSION_REPORT_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
