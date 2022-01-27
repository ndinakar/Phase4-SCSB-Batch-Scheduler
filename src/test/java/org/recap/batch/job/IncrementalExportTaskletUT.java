package org.recap.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.batch.service.RecordsExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
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
@Slf4j
public class IncrementalExportTaskletUT extends BaseTestCaseUT {

    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    String scsbEtlUrl;

    @Mock
    IncrementalExportTasklet incrementalExportTasklet;
    
    @Mock
    RecordsExportService recordsExportService;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(incrementalExportTasklet,"scsbEtlUrl",scsbEtlUrl);
        ReflectionTestUtils.setField(incrementalExportTasklet,"recordsExportService",recordsExportService);
    }

    @Test
    public void testExecuteIncrementalExport() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        String exportInstitution = ScsbCommonConstants.PRINCETON;
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.INCREMENTAL_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase()),createdDate, null, exportInstitution)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(incrementalExportTasklet.executeIncrementalExport(context,log,exportInstitution)).thenCallRealMethod();
        RepeatStatus status = incrementalExportTasklet.executeIncrementalExport(context,log,exportInstitution);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }

    @Test
    public void testExecuteIncrementalExportException() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(ScsbConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        String exportInstitution = ScsbCommonConstants.PRINCETON;
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.INCREMENTAL_RECORDS_EXPORT + StringUtils.capitalize(exportInstitution.toLowerCase()), createdDate, exportStringDate, exportInstitution)).thenThrow(new NullPointerException());
        Mockito.when(incrementalExportTasklet.executeIncrementalExport(context,log,exportInstitution)).thenCallRealMethod();
        ReflectionTestUtils.setField(incrementalExportTasklet,"recordsExportService",null);
        RepeatStatus status = incrementalExportTasklet.executeIncrementalExport(context,log,exportInstitution);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED,status);
    }


}
