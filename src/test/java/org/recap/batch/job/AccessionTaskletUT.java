package org.recap.batch.job;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.slf4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest
public class AccessionTaskletUT  {

    @InjectMocks
    AccessionTasklet maccessionTasklet;

    @Mock
    AccessionTasklet accessionTasklet;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testexecute_Exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("AccessionStep", new JobExecution(new JobInstance(123L, "Accession"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(accessionTasklet.getResultStatus(Mockito.any(JobExecution.class),Mockito.any(StepExecution.class),Mockito.any(Logger.class),Mockito.any(ExecutionContext.class),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(null);
        Mockito.when(accessionTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = accessionTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("AccessionStep", new JobExecution(new JobInstance(123L, "Accession"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(accessionTasklet.getResultStatus(Mockito.any(JobExecution.class),Mockito.any(StepExecution.class),Mockito.any(Logger.class),Mockito.any(ExecutionContext.class),Mockito.anyString(),Mockito.anyString(),Mockito.anyString())).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(accessionTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = accessionTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
