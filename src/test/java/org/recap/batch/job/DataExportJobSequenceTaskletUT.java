package org.recap.batch.job;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapCommonConstants;
import org.recap.RecapConstants;
import org.recap.batch.service.CommonService;
import org.recap.batch.service.DataExportJobSequenceService;
import org.recap.util.JobDataParameterUtil;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
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

public class DataExportJobSequenceTaskletUT extends BaseTestCase {

    @Mock
    DataExportJobSequenceService dataExportJobSequenceServiceMock;

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Mock
    DataExportJobSequenceTasklet dataExportJobSequenceTasklet;

    @Test
    public void testexecute_Exception() throws Exception {
        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2020-07-07";
        StepContribution contribution = new StepContribution(new StepExecution("DataExportJobSep", new JobExecution(new JobInstance(25L, "DataExportDecision"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(dataExportJobSequenceServiceMock.dataExportJobSequence(scsbEtlUrl, createdDate, exportStringDate)).thenThrow(new NullPointerException());
        Mockito.when(dataExportJobSequenceTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = dataExportJobSequenceTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute() throws Exception {
        Date createdDate = new Date(System.currentTimeMillis());
        String exportStringDate= "2020-07-07";
        StepContribution contribution = new StepContribution(new StepExecution("DataExportJobSep", new JobExecution(new JobInstance(25L, "DataExportDecision"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        ChunkContext context = new ChunkContext(new StepContext(execution));
        ReflectionTestUtils.setField(dataExportJobSequenceTasklet,"dataExportJobSequenceService",dataExportJobSequenceServiceMock);
        Mockito.when(dataExportJobSequenceServiceMock.dataExportJobSequence(Mockito.anyString(),Mockito.any(Date.class),Mockito.anyString())).thenReturn(RecapConstants.SUCCESS);
        Mockito.when(dataExportJobSequenceTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = dataExportJobSequenceTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testexecute_fail() throws Exception {

        StepContribution contribution = new StepContribution(new StepExecution("DataExportJobSep", new JobExecution(new JobInstance(25L, "DataExportDecision"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = "time=1594711136358";
        Date createdDate = jobExecution.getCreateTime();
        ReflectionTestUtils.setField(dataExportJobSequenceTasklet,"dataExportJobSequenceService",dataExportJobSequenceServiceMock);
        Mockito.when(dataExportJobSequenceServiceMock.dataExportJobSequence(scsbEtlUrl,createdDate,exportStringDate)).thenReturn(RecapCommonConstants.FAIL);
        Mockito.when(dataExportJobSequenceTasklet.execute(contribution,context)).thenCallRealMethod();
        RepeatStatus status = dataExportJobSequenceTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
