package org.recap.batch.job;

import org.junit.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.batch.service.AccessionReconcilationService;
import org.recap.batch.service.IncrementalExportCulService;
import org.recap.batch.service.IncrementalExportNyplService;
import org.recap.batch.service.IncrementalExportPulService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Anitha V on 14/7/20.
 */

public class IncrementalExportTaskletUT extends BaseTestCase {
    private static final Logger logger = LoggerFactory.getLogger(IncrementalExportTaskletUT.class);


    @Mock
    IncrementalExportNyplService incrementalExportNyplService;

    @Mock
    IncrementalExportCulService incrementalExportCulService;

    @Mock
    IncrementalExportPulService incrementalExportPulService;

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Mock
    IncrementalExportTasklet incrementalExportTasklet;

    @Test
    public void testexecute_cul() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        ReflectionTestUtils.setField(incrementalExportTasklet,"incrementalExportCulService",incrementalExportCulService);
        Mockito.when(incrementalExportCulService.incrementalExportCul(scsbEtlUrl,RecapConstants.INCREMENTAL_RECORDS_EXPORT_CUL,createdDate, exportStringDate)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"CUL")).thenCallRealMethod();
        RepeatStatus status = incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"CUL");
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute_pul() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        ReflectionTestUtils.setField(incrementalExportTasklet,"incrementalExportPulService",incrementalExportPulService);
        Mockito.when(incrementalExportPulService.incrementalExportPul(scsbEtlUrl,RecapConstants.INCREMENTAL_RECORDS_EXPORT_PUL,createdDate, exportStringDate)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"PUL")).thenCallRealMethod();
        RepeatStatus status = incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"PUL");
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute_nypl() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        ReflectionTestUtils.setField(incrementalExportTasklet,"incrementalExportNyplService",incrementalExportNyplService);
        Mockito.when(incrementalExportNyplService.incrementalExportNypl(scsbEtlUrl,RecapConstants.INCREMENTAL_RECORDS_EXPORT_NYPL,createdDate, exportStringDate)).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"NYPL")).thenCallRealMethod();
        RepeatStatus status = incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"NYPL");
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute_exception() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("StatusReconcilationStep", new JobExecution(new JobInstance(25L, "PeriodicLASItemStatusReconciliation"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(RecapConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(incrementalExportPulService.incrementalExportPul(scsbEtlUrl,RecapConstants.INCREMENTAL_RECORDS_EXPORT_PUL,createdDate, exportStringDate)).thenThrow(new NullPointerException());
        Mockito.when(incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"PUL")).thenCallRealMethod();
        RepeatStatus status = incrementalExportTasklet.executeIncrementalExport(contribution,context,logger,"PUL");
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
