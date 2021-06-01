package org.recap.batch.job;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbConstants;
import org.recap.batch.service.GenerateReportsService;
import org.recap.batch.service.UpdateJobDetailsService;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

public class CgdRoundTripReportsTaskletUT extends BaseTestCaseUT {

    @InjectMocks
    CgdRoundTripReportsTasklet cgdRoundTripReportsTasklet;

    @Mock
    UpdateJobDetailsService updateJobDetailsService;

    @Mock
    GenerateReportsService generateReportsService;


    @Test
    public void testExecute() throws Exception {
        Mockito.when(generateReportsService.generateCgdReport(Mockito.anyString(),Mockito.any(),Mockito.anyString())).thenReturn(ScsbConstants.SUCCESS);
        StepContribution contribution = new StepContribution(new StepExecution("cgdRroundTripReportsTasklet", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        RepeatStatus execute=cgdRoundTripReportsTasklet.execute(contribution,context);
        assertEquals(RepeatStatus.FINISHED,execute);
    }

    @Test
    public void testExecuteException() throws Exception {
        ReflectionTestUtils.setField(cgdRoundTripReportsTasklet,"generateReportsService",null);
        StepContribution contribution = new StepContribution(new StepExecution("cgdRroundTripReportsTasklet", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        RepeatStatus execute=cgdRoundTripReportsTasklet.execute(contribution,context);
        assertEquals(RepeatStatus.FINISHED,execute);
    }
}
