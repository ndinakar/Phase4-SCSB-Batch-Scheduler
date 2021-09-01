package org.recap.batch.job;

import org.apache.commons.lang.StringUtils;
import org.recap.ScsbConstants;
import org.recap.batch.service.RecordsExportService;
import org.slf4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class DeletedRecordsExportTasklet extends JobCommonTasklet {

    @Autowired
    private RecordsExportService recordsExportService;

    /**
     * This method starts the execution of deleted records export job for any passed institution.
     *
     * @param chunkContext chunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    public RepeatStatus executeDeletedRecordsExport(ChunkContext chunkContext, Logger logger, String exportInstitution, Boolean check) throws Exception {
        logger.info("Executing DeletedRecordsExport for {}", exportInstitution);
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        try {
            String resultStatus = ScsbConstants.NO_REQUESTING_INSTITUTION;
            if (StringUtils.isNotBlank(exportInstitution)) {
                String exportStringDate = jobExecution.getJobParameters().getString(ScsbConstants.FROM_DATE);
                Date createdDate = jobExecution.getCreateTime();
                updateJob(jobExecution, "DeletedRecordsExport" + StringUtils.capitalize(exportInstitution.toLowerCase()) + "Tasklet", check);
                resultStatus = recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase()), createdDate, exportStringDate, exportInstitution);
            }
            logger.info("Deleted Records Export {} status : {}", exportInstitution, resultStatus);
            setExecutionContext(executionContext, stepExecution, resultStatus);
        } catch (Exception ex) {
            updateExecutionExceptionStatus(stepExecution, executionContext, ex, ScsbConstants.DELETED_RECORDS_EXPORT + "-" + exportInstitution);
        }
        return RepeatStatus.FINISHED;
    }
}