package org.recap.batch.job;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.DeletedRecordsExportCulService;
import org.recap.batch.service.DeletedRecordsExportNyplService;
import org.recap.batch.service.DeletedRecordsExportPulService;
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

public class DeletedRecordsExportTasklet extends JobCommonTasklet {

    @Autowired
    private DeletedRecordsExportCulService deletedRecordsExportCulService;

    @Autowired
    private DeletedRecordsExportNyplService deletedRecordsExportNyplService;

    @Autowired
    private DeletedRecordsExportPulService deletedRecordsExportPulService;
    
    
    
    

    /**
     * This method starts the execution of deleted records export job for Columbia.
     * @param contribution
     * @param chunkContext
     * @return
     * @throws Exception
     */
    public RepeatStatus executeDeletedRecordsExport(StepContribution contribution, ChunkContext chunkContext, Logger logger, String exportInstitution, Boolean check) throws Exception {
        logger.info("Executing DeletedRecordsExport"+exportInstitution+"Tasklet");
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
            Date createdDate = jobExecution.getCreateTime();
            updateJob(jobExecution,"Deleted Records Export " + exportInstitution + " Tasklet", check);
            String resultStatus = null;
            if (exportInstitution.equalsIgnoreCase("CUL")) {
                resultStatus = deletedRecordsExportCulService.deletedRecordsExportCul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_CUL, createdDate, exportStringDate);
                logger.info("Deleted Records Export CUL status : {}", resultStatus);
            }
            if (exportInstitution.equalsIgnoreCase("PUL")) {
                resultStatus = deletedRecordsExportPulService.deletedRecordsExportPul(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_PUL, createdDate, exportStringDate);
                logger.info("Deleted Records Export PUL status : {}", resultStatus);
            }
            if (exportInstitution.equalsIgnoreCase("NYPL")) {
                resultStatus = deletedRecordsExportNyplService.deletedRecordsExportNypl(scsbEtlUrl, RecapConstants.DELETED_RECORDS_EXPORT_NYPL, createdDate, exportStringDate);
                logger.info("Deleted Records Export NYPL status : {}", resultStatus);
            }
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