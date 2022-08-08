package org.recap.batch.job;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.recap.batch.service.DataExportJobSequenceService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;


import static org.junit.Assert.assertEquals;

public class DataExportTriggerJobTaskletUT extends BaseTestCaseUT {

    @InjectMocks
    DataExportTriggerJobTasklet tasklet;

    @Mock
    DataExportJobSequenceService service;

    @Test
    public void taskletExecute() throws Exception {
        Mockito.when(service.dataExportTriggerJob(Mockito.anyString())).thenReturn(ScsbConstants.SUCCESS);
        StepContribution contribution = new StepContribution(new StepExecution("tasklet", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        RepeatStatus execute=tasklet.execute(contribution,context);
        assertEquals(RepeatStatus.FINISHED,execute);
    }

}
