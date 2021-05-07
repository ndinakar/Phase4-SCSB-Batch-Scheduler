package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.PurgeExceptionRequestsService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

public class PurgeExceptionRequestTaskletUT extends BaseTestCase {

    @Mock
    PurgeExceptionRequestsService purgeExceptionRequestsService;

    @Value("${scsb.circ.url}")
    String scsbCircUrl;

    @Mock
    PurgeExceptionRequestTasklet purgeExceptionRequestTasklet;

    @Test
    public void testexecute_Exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(
                123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Map<String, String> resultMap= new HashMap<>();
        Mockito.when(purgeExceptionRequestsService.purgeExceptionRequests(scsbCircUrl)).thenThrow(new NullPointerException());
        Mockito.when(purgeExceptionRequestTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = purgeExceptionRequestTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Map<String, String> resultMap= new HashMap<>();
        resultMap.put(ScsbCommonConstants.STATUS, ScsbConstants.SUCCESS);
        resultMap.put(ScsbCommonConstants.MESSAGE, ScsbConstants.SUCCESS);

        ReflectionTestUtils.setField(purgeExceptionRequestTasklet,"purgeExceptionRequestsService",purgeExceptionRequestsService);
        Mockito.when(purgeExceptionRequestsService.purgeExceptionRequests(scsbCircUrl)).thenReturn(resultMap);
        Mockito.when(purgeExceptionRequestTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = purgeExceptionRequestTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
