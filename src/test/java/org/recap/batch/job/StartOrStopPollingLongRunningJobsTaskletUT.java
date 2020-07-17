package org.recap.batch.job;

import org.junit.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Created by Anitha V on 14/7/20.
 */

public class StartOrStopPollingLongRunningJobsTaskletUT extends BaseTestCase {

    @Mock
    StartOrStopPollingLongRunningJobsTasklet startOrStopPollingLongRunningJobsTasklet;

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String action= RecapConstants.START;
//        ReflectionTestUtils.setField(startOrStopPollingLongRunningJobsTasklet,"action",RecapConstants.START);
      //  Mockito.when(jobExecution.getJobParameters().getString(RecapConstants.POLLING_ACTION)).thenReturn(action);
        Mockito.when(startOrStopPollingLongRunningJobsTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status=startOrStopPollingLongRunningJobsTasklet.execute(contribution,context);
    }


}
