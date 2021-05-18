package org.recap.batch.job;

import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Created by rajeshbabuk on 13/May/2021
 */
public class IncrementalExportForInstitutionTasklet extends IncrementalExportTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportForInstitutionTasklet.class);

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
        logger.info("Executing - IncrementalExportForInstitutionTasklet");
        String exportInstitution = getExportInstitutionFromParameters(chunkContext.getStepContext().getStepExecution().getJobExecution());
        return executeIncrementalExport(chunkContext, logger, exportInstitution);
    }
}
