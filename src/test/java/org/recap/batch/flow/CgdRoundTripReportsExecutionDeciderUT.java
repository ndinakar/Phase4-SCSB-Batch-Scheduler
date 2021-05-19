package org.recap.batch.flow;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.PropertyKeyConstants;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 12/May/2021
 */
public class CgdRoundTripReportsExecutionDeciderUT {

    @Mock
    CgdRoundTripReportsExecutionDecider cgdRoundTripReportsExecutionDecider;

    @Value("${" + PropertyKeyConstants.INCLUDE_CGD_ROUND_TRIP_REPORTS_IN_SEQUENCE_JOB + "}")
    private boolean includeCgdRoundTripReportsInSequenceJob;

    @Test
    public void testCgdRoundTripReportsExecutionDecider() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"), new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        ReflectionTestUtils.setField(cgdRoundTripReportsExecutionDecider, "includeCgdRoundTripReportsInSequenceJob", true);
        Mockito.when(cgdRoundTripReportsExecutionDecider.decide(jobExecution, execution)).thenCallRealMethod();
        FlowExecutionStatus status = cgdRoundTripReportsExecutionDecider.decide(jobExecution, execution);
        assertNotNull(status);
    }

    @Test
    public void testMatchingAlgorithmExecutionDecider_else() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"), new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(cgdRoundTripReportsExecutionDecider.decide(jobExecution, execution)).thenCallRealMethod();
        FlowExecutionStatus status = cgdRoundTripReportsExecutionDecider.decide(jobExecution, execution);
        assertNotNull(status);
    }
}
