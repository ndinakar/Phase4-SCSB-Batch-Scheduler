package org.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.RecordsExportService;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class IncrementalExportTasklet extends JobCommonTasklet {

    @Autowired
    private RecordsExportService recordsExportService;

    /**
     * This method starts the execution of Incremental records export job for passed institution.
     *
     * @param chunkContext chunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    public RepeatStatus executeIncrementalExport(ChunkContext chunkContext, Logger logger, String exportInstitution) throws Exception {
        logger.info("Executing IncrementalExport for {}", exportInstitution);
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String resultStatus = ScsbConstants.NO_REQUESTING_INSTITUTION;
            if (StringUtils.isNotBlank(exportInstitution)) {
                String exportStringDate = jobExecution.getJobParameters().getString(ScsbConstants.FROM_DATE);
                Date createdDate = jobExecution.getCreateTime();
                updateJob(jobExecution, "IncrementalExport" + StringUtils.capitalize(exportInstitution.toLowerCase()) + "Tasklet", Boolean.TRUE);
                resultStatus = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.INCREMENTAL_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase()), createdDate, exportStringDate, exportInstitution);
            }
            logger.info("Incremental Export {} status : {}", exportInstitution, resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            logger.error("{} {}", ScsbCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(ScsbConstants.JOB_STATUS, ScsbConstants.FAILURE);
            executionContext.put(ScsbConstants.JOB_STATUS_MESSAGE, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(ScsbConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
