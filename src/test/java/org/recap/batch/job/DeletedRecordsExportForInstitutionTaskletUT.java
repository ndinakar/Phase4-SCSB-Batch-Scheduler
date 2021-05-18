package org.recap.batch.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCaseUT;
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
 * Created by rajeshbabuk on 17/May/2021
 */
public class DeletedRecordsExportForInstitutionTaskletUT extends BaseTestCaseUT {

    private static final Logger logger = LoggerFactory.getLogger(DeletedRecordsExportForInstitutionTaskletUT.class);

    @Value("${scsb.etl.url}")
    String scsbEtlUrl;

    @Mock
    DeletedRecordsExportForInstitutionTasklet deletedRecordsExportForInstitutionTasklet;

    @Mock
    RecordsExportService recordsExportService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(deletedRecordsExportForInstitutionTasklet, "scsbEtlUrl", scsbEtlUrl);
        ReflectionTestUtils.setField(deletedRecordsExportForInstitutionTasklet, "recordsExportService", recordsExportService);
    }


    @Test
    public void testExecute_cul() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_NYPL, createdDate, null, ScsbCommonConstants.COLUMBIA)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.COLUMBIA, true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.COLUMBIA, true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED, status);
    }

    @Test
    public void testExecute_pul() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_PUL, createdDate, null, ScsbCommonConstants.PRINCETON)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.PRINCETON, true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.PRINCETON, true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED, status);
    }

    @Test
    public void testExecute_nypl() throws Exception {
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_NYPL, createdDate, null, ScsbCommonConstants.NYPL)).thenReturn(ScsbConstants.SUCCESS);
        Mockito.when(deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.NYPL, true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.NYPL, true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED, status);
    }

    @Test
    public void testExecute_exception() throws Exception {
        ReflectionTestUtils.setField(deletedRecordsExportForInstitutionTasklet, "recordsExportService", null);
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        JobExecution jobExecution = execution.getJobExecution();
        String exportStringDate = jobExecution.getJobParameters().getString(ScsbConstants.FROM_DATE);
        Date createdDate = jobExecution.getCreateTime();
        Mockito.when(recordsExportService.exportRecords(scsbEtlUrl, ScsbConstants.DELETED_RECORDS_EXPORT_NYPL, createdDate, exportStringDate, ScsbCommonConstants.PRINCETON)).thenThrow(NullPointerException.class);
        Mockito.when(deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.PRINCETON, true)).thenCallRealMethod();
        RepeatStatus status = deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(context, logger, ScsbCommonConstants.PRINCETON, true);
        assertNotNull(status);
        assertEquals(RepeatStatus.FINISHED, status);
    }

}