package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.junit.Assert.assertEquals;

/**
 * Created by Anitha V on 14/7/20.
 */

public class StartOrStopPollingLongRunningJobsTaskletUT extends BaseTestCaseUT {

    @Mock
    StartOrStopPollingLongRunningJobsTasklet startOrStopPollingLongRunningJobsTasklet;

    @Mock
    ChunkContext context;

    @Mock
    StepContext stepContext;

    @Mock
    StepExecution stepExecution;

    @Mock
    JobExecution jobExecution;

    @Mock
    JobParameters jobParameters;

    @Test
    public void testexecuteStart() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        Mockito.when(context.getStepContext()).thenReturn(stepContext);
        Mockito.when(stepContext.getStepExecution()).thenReturn(stepExecution);
        Mockito.when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        Mockito.when(jobExecution.getJobParameters()).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(ScsbConstants.POLLING_ACTION)).thenReturn(ScsbConstants.START);
        Mockito.when(startOrStopPollingLongRunningJobsTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status=startOrStopPollingLongRunningJobsTasklet.execute(contribution,context);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecuteStop() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        Mockito.when(context.getStepContext()).thenReturn(stepContext);
        Mockito.when(stepContext.getStepExecution()).thenReturn(stepExecution);
        Mockito.when(stepExecution.getJobExecution()).thenReturn(jobExecution);
        Mockito.when(jobExecution.getJobParameters()).thenReturn(jobParameters);
        Mockito.when(jobParameters.getString(ScsbConstants.POLLING_ACTION)).thenReturn(ScsbConstants.STOP);
        Mockito.when(startOrStopPollingLongRunningJobsTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status=startOrStopPollingLongRunningJobsTasklet.execute(contribution,context);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
