package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.DataExportJobSequenceService;
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

@Slf4j
public class DataExportTriggerJobTasklet extends JobCommonTasklet implements Tasklet {

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
        log.info("Executing DataExportTriggerJobTasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            updateJob(jobExecution,"Data Export Trigger Job Tasklet", Boolean.TRUE);
            dataExportJobSequenceService.dataExportTriggerJob(scsbEtlUrl);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.DATA_EXPORT_STATUS_NAME);
        }
        return RepeatStatus.FINISHED;
    }
}
