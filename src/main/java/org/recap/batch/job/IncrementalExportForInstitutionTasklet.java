package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by rajeshbabuk on 13/May/2021
 */
@Slf4j
public class IncrementalExportForInstitutionTasklet extends IncrementalExportTasklet implements Tasklet {

    /**
     * This method starts the execution of incremental export job for the given institution.
     *
     * @param contribution StepContribution
     * @param chunkContext ChunkContext
     * @return RepeatStatus
     * @throws Exception Exception Class
     */
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing - IncrementalExportForInstitutionTasklet");
        String exportInstitution = getExportInstitutionFromParameters(chunkContext.getStepContext().getStepExecution().getJobExecution());
        return executeIncrementalExport(chunkContext, log, exportInstitution);
    }
}
