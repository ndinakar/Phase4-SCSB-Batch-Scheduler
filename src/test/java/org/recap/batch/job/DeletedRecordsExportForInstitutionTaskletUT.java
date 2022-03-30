package org.recap.batch.job;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.batch.service.RecordsExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
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


    @Value("${" + PropertyKeyConstants.SCSB_ETL_URL + "}")
    String scsbEtlUrl;

    @Mock
    DeletedRecordsExportForInstitutionTasklet deletedRecordsExportForInstitutionTasklet;

    @Mock
    RecordsExportService recordsExportService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(deletedRecordsExportForInstitutionTasklet, "scsbEtlUrl", scsbEtlUrl);
        ReflectionTestUtils.setField(deletedRecordsExportForInstitutionTasklet, "recordsExportService", recordsExportService);
    }

    @Test
    public void testExecute() throws Exception {
        StepContribution contribution = new StepContribution(new StepExecution("cgdRroundTripReportsTasklet", new JobExecution(new JobInstance(123L, "job"),new JobParameters())));
        StepExecution execution = MetaDataInstanceFactory.createStepExecution();
        execution.setCommitCount(2);
        ChunkContext context = new ChunkContext(new StepContext(execution));
        Mockito.when(deletedRecordsExportForInstitutionTasklet.execute(contribution,context)).thenCallRealMethod();
        Mockito.when(deletedRecordsExportForInstitutionTasklet.executeDeletedRecordsExport(Mockito.any(),Mockito.any(),Mockito.anyString(),Mockito.any())).thenReturn(RepeatStatus.FINISHED);
        Mockito.when(deletedRecordsExportForInstitutionTasklet.getExportInstitutionFromParameters(Mockito.any())).thenReturn(ScsbCommonConstants.PRINCETON);
        RepeatStatus status = deletedRecordsExportForInstitutionTasklet.execute(contribution,context);
        assertNotNull(status);
        assertEquals(status,RepeatStatus.FINISHED);
    }

}