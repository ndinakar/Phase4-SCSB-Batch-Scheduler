package org.recap.batch.flow;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest
public class SubmitCollectionExecutionDeciderUT {

    @Mock
    SubmitCollectionExecutionDecider submitCollectionExecutionDecider;

    @Value("${" + PropertyKeyConstants.INCLUDE_SUBMIT_COLLECTION_IN_SEQUENCE_JOB + "}")
    private boolean includeSubmitCollectionInSequenceJob;

    @Test
    public void testSubmitCollectionExecutionDecider() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        ReflectionTestUtils.setField(submitCollectionExecutionDecider,"includeSubmitCollectionInSequenceJob",true);
        Mockito.when(submitCollectionExecutionDecider.decide(jobExecution,execution)).thenCallRealMethod();
        FlowExecutionStatus status = submitCollectionExecutionDecider.decide(jobExecution,execution);
        assertNotNull(status);
    }
    @Test
    public void testSubmitCollectionExecutionDecider_else() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(submitCollectionExecutionDecider.decide(jobExecution,execution)).thenCallRealMethod();
        FlowExecutionStatus status = submitCollectionExecutionDecider.decide(jobExecution,execution);
        assertNotNull(status);
    }
}
