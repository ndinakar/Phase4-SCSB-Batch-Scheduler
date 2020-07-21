package org.recap.batch.job;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.IncrementalExportCulService;
import org.recap.batch.service.IncrementalExportNyplService;
import org.recap.batch.service.IncrementalExportPulService;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class IncrementalExportTasklet extends JobCommonTasklet {
   
    @Autowired
    private IncrementalExportNyplService incrementalExportNyplService;

    @Autowired
    private IncrementalExportCulService incrementalExportCulService;

    @Autowired
    private IncrementalExportPulService incrementalExportPulService;

    public RepeatStatus executeIncrementalExport(StepContribution contribution, ChunkContext chunkContext, Logger logger, String exportInstitution) {
        if (exportInstitution.equalsIgnoreCase("NYPL")) {
            logger.info("Executing IncrementalExportNyplTasklet");
        } else if (exportInstitution.equalsIgnoreCase("CUL")) {
            logger.info("Executing IncrementalExportCULTasklet");
        }
        else if (exportInstitution.equalsIgnoreCase("PUL")) {
            logger.info("Executing IncrementalExportPulTasklet");
        }
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
            Date createdDate = jobExecution.getCreateTime();
            updateJob(jobExecution,  "Incremental Export "+  exportInstitution + " Tasklet ", Boolean.TRUE);
            String resultStatus = null;
            if (exportInstitution.equalsIgnoreCase("NYPL")) {
                resultStatus = incrementalExportNyplService.incrementalExportNypl(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_NYPL, createdDate, exportStringDate);
            } else if (exportInstitution.equalsIgnoreCase("CUL")) {
                resultStatus = incrementalExportCulService.incrementalExportCul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_CUL, createdDate, exportStringDate);
            } else if (exportInstitution.equalsIgnoreCase("PUL")) {
                resultStatus = incrementalExportPulService.incrementalExportPul(scsbEtlUrl, RecapConstants.INCREMENTAL_RECORDS_EXPORT_PUL, createdDate, exportStringDate);
            }
            logger.info("Incremental Export {} status : {}" , exportInstitution , resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            logger.error(RecapCommonConstants.LOG_ERROR, ExceptionUtils.getMessage(ex));
            executionContext.put(RecapConstants.JOB_STATUS, RecapConstants.FAILURE);
            executionContext.put(RecapConstants.JOB_STATUS_MESSAGE, ExceptionUtils.getMessage(ex));
            stepExecution.setExitStatus(new ExitStatus(RecapConstants.FAILURE, ExceptionUtils.getFullStackTrace(ex)));
        }
        return RepeatStatus.FINISHED;
    }
}
